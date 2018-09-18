package com.compiler.gui;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

public class CompilerMenu extends JMenuBar implements ActionListener {
    private static final long serialVersionUID = 1L;

    // File
    private final String FILE_MENU = "File";
    private final String FILE_NEW = "New";
    private final String FILE_OPEN = "Open";
    private final String FILE_SAVE = "Save";
    private final String FILE_SAVE_AS = "Save as...";
    private final String FILE_EXIT = "Exit";

    // Edit
    private final String EDIT_UNDO = "Back";
    private final String EDIT_REDO = "Forward";
    private final String EDIT_MENU = "Edit";
    private final String EDIT_CUT = "Cut";
    private final String EDIT_COPY = "Copy";
    private final String EDIT_PASTE = "Paste";
    private final String EDIT_SELECT_ALL = "Select all";

    // Format
    private final String FORMAT_MENU = "Format";
    private final String FORMAT_WRAPLINES = "Wrap lines";

    // Compile
    private final String PROJECT_MENU = "Project";
    private final String PROJECT_CLEAR = "Clear status";
    private final String PROJECT_COMPILE = "Compile";

    // Help
    private final String HELP_MENU = "Help";
    private final String HELP_ABOUT = "About Compilates";

    private Font myFont = null;

    private JMenu fileMenu = null;
    private JMenu editMenu = null;
    private JMenu formatMenu = null;
    private JMenu projectMenu = null;
    private JMenu helpMenu = null;
    private JMenuItem undoMenu = null;
    private JMenuItem redoMenu = null;

    private CompilerMenuEvents compilerMenuEvents = null;

    public CompilerMenu(CompilerMenuEvents compilerMenuEvents) {
        myFont = new Font("Tahoma", Font.PLAIN, 11);
        this.compilerMenuEvents = compilerMenuEvents;
        constroy();
    }

    public CompilerMenu(CompilerMenuEvents compilerMenuEvents, Font font) {
        this.myFont = font;
        this.compilerMenuEvents = compilerMenuEvents;
        constroy();
    }

    public JMenuItem getUndoMenu() {
        return undoMenu;
    }

    public JMenuItem getRedoMenu() {
        return redoMenu;
    }

    private void constroy() {
        fileMenu = createFileMenu();
        fileMenu.setMnemonic(KeyEvent.VK_A);

        editMenu = createEditMenu();
        editMenu.setMnemonic(KeyEvent.VK_E);

        formatMenu = createFormatMenu();
        formatMenu.setMnemonic(KeyEvent.VK_F);

        projectMenu = createProjectMenu();
        projectMenu.setMnemonic(KeyEvent.VK_P);

        helpMenu = createHelpMenu();
        helpMenu.setMnemonic(KeyEvent.VK_J);

        add(fileMenu);
        add(editMenu);
        add(formatMenu);
        add(projectMenu);
        add(helpMenu);

        setMyFont(this);
    }

    private void setMyFont(JComponent c) {
        c.setFont(myFont);
        Component comps[] = c.getComponents();

        if (comps.length > 0) {
            for (int i = 0; i < comps.length; i++) {
                comps[i].setFont(myFont);
                setMyFont((JComponent) comps[i]);
            }
        }
        else if (c instanceof JMenu) {
            Component[] menus = ((JMenu) c).getMenuComponents();
            if (menus.length > 0) {
                for (int i = 0; i < menus.length; i++) {
                    menus[i].setFont(myFont);
                    setMyFont((JComponent) menus[i]);
                }
            }
        }
    }

    private JMenu createFileMenu() {
        JMenuItem item = null;
        JMenu menu = new JMenu(FILE_MENU);

        item = createMenuItem(FILE_NEW);
        item.setMnemonic(KeyEvent.VK_N);
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        menu.add(item);

        item = createMenuItem(FILE_OPEN);
        item.setMnemonic(KeyEvent.VK_A);
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        menu.add(item);

        item = createMenuItem(FILE_SAVE);
        item.setMnemonic(KeyEvent.VK_S);
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        menu.add(item);

        item = createMenuItem(FILE_SAVE_AS);
        item.setMnemonic(KeyEvent.VK_C);
        menu.add(item);

        menu.addSeparator();

        item = createMenuItem(FILE_EXIT);
        item.setMnemonic(KeyEvent.VK_R);
        menu.add(item);

        return menu;
    }

    private JMenu createEditMenu() {
        JMenuItem item = null;
        JMenu menu = new JMenu(EDIT_MENU);

        undoMenu = createMenuItem(EDIT_UNDO);
        undoMenu.setEnabled(false);
        undoMenu.setMnemonic(KeyEvent.VK_V);
        undoMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK));
        menu.add(undoMenu);

        redoMenu = createMenuItem(EDIT_REDO);
        redoMenu.setEnabled(false);
        redoMenu.setMnemonic(KeyEvent.VK_A);
        redoMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, ActionEvent.CTRL_MASK));
        menu.add(redoMenu);

        menu.addSeparator();

        item = createMenuItem(EDIT_CUT);
        item.setMnemonic(KeyEvent.VK_R);
        menu.add(item);

        item = createMenuItem(EDIT_COPY);
        item.setMnemonic(KeyEvent.VK_C);
        menu.add(item);

        item = createMenuItem(EDIT_PASTE);
        item.setMnemonic(KeyEvent.VK_O);
        menu.add(item);

        menu.addSeparator();

        item = createMenuItem(EDIT_SELECT_ALL);
        item.setMnemonic(KeyEvent.VK_E);
        menu.add(item);

        return menu;
    }

    private JMenu createFormatMenu() {
        JMenuItem item = null;
        JMenu menu = new JMenu(FORMAT_MENU);

        item = createCheckMenuItem(FORMAT_WRAPLINES);
        item.setMnemonic(KeyEvent.VK_Q);
        menu.add(item);

        return menu;
    }

    private JMenu createProjectMenu() {
        JMenuItem item = null;
        JMenu menu = new JMenu(PROJECT_MENU);

        item = createMenuItem(PROJECT_CLEAR);
        item.setMnemonic(KeyEvent.VK_L);
        item.setAccelerator(KeyStroke.getKeyStroke("F4"));
        menu.add(item);

        menu.addSeparator();

        item = createMenuItem(PROJECT_COMPILE);
        item.setMnemonic(KeyEvent.VK_C);
        item.setAccelerator(KeyStroke.getKeyStroke("F5"));
        menu.add(item);

        return menu;
    }

    private JMenu createHelpMenu() {
        JMenuItem item = null;
        JMenu menu = new JMenu(HELP_MENU);

        item = createMenuItem(HELP_ABOUT);
        item.setMnemonic(KeyEvent.VK_S);
        menu.add(item);

        return menu;
    }

    private JMenuItem createMenuItem(String name) {
        JMenuItem item = new JMenuItem(name);
        item.addActionListener(this);
        return item;
    }

    private JMenuItem createCheckMenuItem(String name) {
        JCheckBoxMenuItem item = new JCheckBoxMenuItem(name);
        item.addActionListener(this);
        return item;
    }

    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();

        if (obj instanceof JMenuItem) {
            JMenuItem menu = (JMenuItem) obj;
            String menuName = menu.getText();

            // File
            if (menuName.equals(FILE_NEW))
                compilerMenuEvents.onFileNew();
            else if (menuName.equals(FILE_OPEN))
                compilerMenuEvents.onFileOpen();
            else if (menuName.equals(FILE_SAVE))
                compilerMenuEvents.onFileSave();
            else if (menuName.equals(FILE_SAVE_AS))
                compilerMenuEvents.onFileSaveAs();
            else if (menuName.equals(FILE_EXIT))
                compilerMenuEvents.onFileExit();

            // Edit
            else if (menuName.equals(EDIT_UNDO))
                compilerMenuEvents.onEditUndo();
            else if (menuName.equals(EDIT_REDO))
                compilerMenuEvents.onEditRedo();
            else if (menuName.equals(EDIT_CUT))
                compilerMenuEvents.onEditCut();
            else if (menuName.equals(EDIT_COPY))
                compilerMenuEvents.onEditCopy();
            else if (menuName.equals(EDIT_PASTE))
                compilerMenuEvents.onEditPaste();
            else if (menuName.equals(EDIT_SELECT_ALL))
                compilerMenuEvents.onEditSelectAll();

            // Format
            else if (menuName.equals(FORMAT_WRAPLINES))
                compilerMenuEvents.onFormatWrapLines();

            // Project
            else if (menuName.equals(PROJECT_CLEAR))
                compilerMenuEvents.onProjectClear();
            else if (menuName.equals(PROJECT_COMPILE))
                compilerMenuEvents.onProjectCompile();

            // Help
            else if (menuName.equals(HELP_ABOUT))
                compilerMenuEvents.onHelpAbout();
        }
    }
}
