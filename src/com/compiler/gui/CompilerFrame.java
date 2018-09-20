package com.compiler.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.Document;
import javax.swing.undo.UndoManager;

import com.compiler.status.StatusManager;
import com.compiler.status.StatusTransfer;
import com.compiler.syntactic.analyzer.SyntacticAnalyzer;
import com.compiler.syntactic.analyzer.SyntacticException;

public class CompilerFrame extends JFrame implements CompilerMenuEvents, StatusManager {

    private static final long serialVersionUID = 1L;

    public static final String ICON = "/img/compiler.png";
    private final String WND_TITLE = "Compilates";
    private final Font MENU_FONT = new Font("Tahoma", Font.PLAIN, 11);
    private final Font CONTENT_FONT = new Font("Courier New", Font.PLAIN, 12);
    private final Font RESULTS_FONT = new Font("Courier New", Font.PLAIN, 12);
    private final String LINE_SEPARATOR = System.getProperty("line.separator");

    private JScrollPane spResults = null;
    private JScrollPane spContents = null;
    private JTextArea taResults = new JTextArea();
    private JTextArea taContents = new JTextArea();

    private String lastDir = ".";
    private boolean newDoc = true;
    private File workingFile = null;
    private boolean savedDoc = true;
    private Runnable compilerThread = null;
    private CompilerMenu compilerMenu = null;
    private UndoManager undoManager = new UndoManager();

    public CompilerFrame() {
        constroy();
        StatusTransfer.getInstance().setStatusManager(this);

        compilerThread = new Runnable() {
            public void run() {
                final int ms = 200; // msec
                File file = getWorkingFile();

                if (file != null) {
                    setActive(false);
                    clearStatus();
                    appendStatus("Compiling \'" + file.getName() + "\'...");

                    try {
                        String filePath = file.getAbsolutePath();
                        (new SyntacticAnalyzer(filePath)).startAnalysis();

                        try {
                            Thread.sleep(ms);
                        }
                        catch (Exception e) {
                        }

                        appendStatus("Done.");
                    }
                    catch (SyntacticException se) {
                        try {
                            Thread.sleep(ms);
                        }
                        catch (Exception e) {
                        }

                        appendStatus("Syntax error token '" + se.getToken().getLexeme() + "' : " + se.getMessage());
                    }
                    catch (Exception e) {
                        try {
                            Thread.sleep(ms);
                        }
                        catch (Exception ee) {
                        }

                        appendStatus(e.getMessage());
                    }

                    Toolkit.getDefaultToolkit().beep();
                    setActive(true);
                }
            }
        };
    }

    private void constroy() {
        compilerMenu = new CompilerMenu(this, MENU_FONT);
        getContentPane().add(createMainPanel());
        setJMenuBar(compilerMenu);
        adjustFrame();
    }

    private void adjustFrame() {
        URL iconURL = getClass().getResource(ICON);
        if (iconURL != null) {
            ImageIcon icon = new ImageIcon(iconURL);
            setIconImage(icon.getImage());
        }

        final int perWidth = 95; // em %
        final int perHeight = 90; // em %

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        int width = screenSize.width * perWidth / 100;
        int height = screenSize.height * perHeight / 100;

        int x = (screenSize.width - width) / 2;
        int y = (screenSize.height - height) / 2 / 2;

        setWndTitle(null);
        setBounds(x, y, width, height);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                if (checkChangesToSave()) {
                    setVisible(false);
                    System.exit(0);
                }
            }
        });
    }

    private JPanel createMainPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(createTAContents(), BorderLayout.CENTER);
        panel.add(createTAResults(), BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createTAContents() {
        if (spContents == null) {
            final int inc = 5;
            taContents.setTabSize(4);
            taContents.setFont(CONTENT_FONT);
            taContents.setForeground(new Color(0x00, 0x00, 0x80));
            taContents.setBackground(new Color(0xff, 0xff, 0xff));
            taContents.setCaretColor(new Color(0x80, 0x80, 0x80));
            taContents.setMargin(new Insets(inc, inc, inc, inc));

            Document doc = taContents.getDocument();

            doc.addUndoableEditListener(new UndoableEditListener() {
                public void undoableEditHappened(UndoableEditEvent e) {
                    undoManager.addEdit(e.getEdit());
                    updateUndoRedoMenu();
                }
            });

            doc.addDocumentListener(new DocumentListener() {
                public void changedUpdate(DocumentEvent e) {
                    setSavedDoc(false);
                    updateWndTitle();
                }

                public void removeUpdate(DocumentEvent e) {
                    setSavedDoc(false);
                    updateWndTitle();
                }

                public void insertUpdate(DocumentEvent e) {
                    setSavedDoc(false);
                    updateWndTitle();
                }
            });

            spContents = new JScrollPane(taContents);
            spContents.setAutoscrolls(true);
        }

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(spContents, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createTAResults() {
        if (spResults == null) {
            final int inc = 5;
            taResults.setRows(10);
            taResults.setEditable(false);
            taResults.setFont(RESULTS_FONT);
            taResults.setForeground(new Color(0x00, 0x00, 0x00));
            taResults.setBackground(new Color(0xf0, 0xf0, 0xf0));
            taResults.setMargin(new Insets(inc, inc, inc, inc));

            spResults = new JScrollPane(taResults);
            spResults.setAutoscrolls(true);
        }

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(spResults, BorderLayout.CENTER);

        return panel;
    }

    private void setSavedDoc(boolean savedDoc) {
        this.savedDoc = savedDoc;
    }

    private boolean getSavedDoc() {
        return savedDoc;
    }

    private void setNewDoc(boolean newDoc) {
        this.newDoc = newDoc;
    }

    private boolean getNewDoc() {
        return newDoc;
    }

    private void setWorkingFile(File workingFile) {
        this.workingFile = workingFile;
    }

    private File getWorkingFile() {
        return workingFile;
    }

    private void updateWndTitle() {
        File file = getWorkingFile();
        if (file != null) {
            setWndTitle(file.getName(), getSavedDoc());
        }
        else {
            setWndTitle(null);
        }
    }

    private void setWndTitle(String title) {
        setWndTitle(title, true);
    }

    private void setWndTitle(String title, boolean saved) {
        String notSaved = "";
        if (saved == false) {
            notSaved = "* ";
        }

        String str = (title == null) ? "Untitled" : title;
        setTitle(notSaved + str + " - " + WND_TITLE);
    }

    public String getWndTitle() {
        return WND_TITLE;
    }

    private void setActive(boolean active) {
        taContents.setEnabled(active);
        getJMenuBar().setEnabled(active);
        setEnabled(active);
    }

    private JFileChooser createFileChooser() {
        JFileChooser fc = new JFileChooser(lastDir);
        fc.setMultiSelectionEnabled(false);
        fc.setAcceptAllFileFilterUsed(false);
        fc.addChoosableFileFilter(new FileFilter() {
            public boolean accept(File file) {
                String name = file.getName();
                if (file.isDirectory() || name.toLowerCase().endsWith(".txt")) {
                    return true;
                }

                return false;
            }

            public String getDescription() {
                return "Plain text files (*.txt)";
            }
        });

        return fc;
    }

    private boolean checkChangesToSave() {
        if (!getSavedDoc()) {
            String strFile;
            File file = getWorkingFile();

            if (getNewDoc() || (file == null)) {
                strFile = "Untitled";
            }
            else {
                strFile = file.getName();
            }

            int res = JOptionPane.showConfirmDialog(this, "Would you like to save file \"" + strFile + "\"?",
                getWndTitle(), JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);

            switch (res) {
            case JOptionPane.YES_OPTION:
                return onFileSave();

            case JOptionPane.NO_OPTION:
                return true;

            case JOptionPane.CANCEL_OPTION:
            case JOptionPane.CLOSED_OPTION:
            default:
                return false;
            }
        }

        return true;
    }

    private void renewUndoManager() {
        undoManager.discardAllEdits();
        undoManager = new UndoManager();
        updateUndoRedoMenu();
    }

    private void updateUndoRedoMenu() {
        boolean canUndo = undoManager.canUndo();
        if (canUndo == false) {
            setSavedDoc(true);
            updateWndTitle();
        }

        setUndoMenuEnabled(canUndo);
        setRedoMenuEnabled(undoManager.canRedo());
    }

    private void setUndoMenuEnabled(boolean enabled) {
        compilerMenu.getUndoMenu().setEnabled(enabled);
    }

    private void setRedoMenuEnabled(boolean enabled) {
        compilerMenu.getRedoMenu().setEnabled(enabled);
    }

    public void onFileNew() {
        if (checkChangesToSave()) {
            setActive(false);
            taContents.setText("");
            setNewDoc(true);
            setSavedDoc(true);
            setWorkingFile(null);
            updateWndTitle();
            clearStatus();
            taContents.requestFocus();
            renewUndoManager();
            setActive(true);
        }
    }

    public boolean onFileOpen() {
        if (checkChangesToSave()) {
            JFileChooser fc = createFileChooser();
            int res = fc.showOpenDialog(this);

            try {
                File file = fc.getSelectedFile();
                if (file != null) {
                    lastDir = file.getParent();
                }

                if (res == JFileChooser.APPROVE_OPTION) {
                    setActive(false);

                    FileReader fileReader = new FileReader(file);
                    BufferedReader buffer = new BufferedReader(fileReader);

                    taContents.setText("");
                    StringBuffer content = new StringBuffer();

                    try {
                        String line = buffer.readLine();
                        while (line != null) {
                            content.append(line);
                            content.append("\n");
                            line = buffer.readLine();
                        }
                    } finally {
                        buffer.close();
                        fileReader.close();
                    }

                    String strContent = new String(content);
                    taContents.setText(strContent);

                    setNewDoc(false);
                    setSavedDoc(true);
                    setWorkingFile(file);
                    updateWndTitle();

                    taContents.setCaretPosition(0);
                    renewUndoManager();
                }

                clearStatus();
                taContents.requestFocus();
                setActive(true);

                return true;
            }
            catch (FileNotFoundException fnfe) {
                JOptionPane.showMessageDialog(this, "File not found");
            }
            catch (Exception e) {
                JOptionPane.showMessageDialog(this, "ERROR: " + e.getMessage());
            }
        }

        return false;
    }

    public boolean onFileSave() {
        if (getSavedDoc() && !getNewDoc()) {
            return true;
        }

        File file = getWorkingFile();
        if (getNewDoc() || (file == null)) {
            return onFileSaveAs();
        }

        try {
            FileWriter fileWriter = new FileWriter(file);
            String text = taContents.getText();

            text = text.replaceAll("\n", LINE_SEPARATOR);
            fileWriter.write(text);
            fileWriter.close();

            setNewDoc(false);
            setSavedDoc(true);
            updateWndTitle();

            return true;
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(this, "ERROR: " + e.getMessage());
        }

        return false;
    }

    public boolean onFileSaveAs() {
        JFileChooser fc = createFileChooser();
        int res = fc.showSaveDialog(this);

        try {
            File file = fc.getSelectedFile();
            if (file != null) {
                lastDir = file.getParent();
            }

            if (res == JFileChooser.APPROVE_OPTION) {
                setActive(false);

                String filePath = file.getPath();
                String fileName = file.getName();

                if (fileName.endsWith(".txt") == false) {
                    filePath += ".txt";
                }

                file = new File(filePath);
                boolean fileOK = file.createNewFile();

                if (!fileOK || !file.canWrite()) {
                    throw new Exception("Cannot create file");
                }

                FileWriter fileWriter = new FileWriter(file);
                String text = taContents.getText();

                text = text.replaceAll("\n", LINE_SEPARATOR);
                fileWriter.write(text);
                fileWriter.close();

                setNewDoc(false);
                setSavedDoc(true);
                setWorkingFile(file);
                updateWndTitle();
            }

            setActive(true);
            return true;
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(this, "ERROR: " + e.getMessage());
        }

        return false;
    }

    public void onFileExit() {
        if (checkChangesToSave()) {
            setVisible(false);
            System.exit(0);
        }
    }

    public void onEditUndo() {
        try {
            undoManager.undo();
        }
        catch (Exception e) {
        }

        updateUndoRedoMenu();
    }

    public void onEditRedo() {
        try {
            undoManager.redo();
        }
        catch (Exception e) {
        }

        updateUndoRedoMenu();
    }

    public void onEditCut() {
        taContents.cut();
    }

    public void onEditCopy() {
        taContents.copy();
    }

    public void onEditPaste() {
        taContents.paste();
    }

    public void onEditSelectAll() {
        taContents.selectAll();
    }

    public void onFormatWrapLines() {
        taContents.setLineWrap(!taContents.getLineWrap());
        taContents.setCaretPosition(0);
    }

    public void onProjectClear() {
        clearStatus();
    }

    public void onProjectCompile() {
        if (onFileSave()) {
            (new Thread(compilerThread)).start();
        }
    }

    public void onHelpAbout() {
        (new CompilerAboutFrame(this)).setVisible(true);
    }

    public void appendStatus(String status) {
        taResults.append(status + "\n");
        taResults.setCaretPosition(taResults.getText().length());
        taContents.requestFocus();
    }

    public void clearStatus() {
        taResults.setText("");
    }
}
