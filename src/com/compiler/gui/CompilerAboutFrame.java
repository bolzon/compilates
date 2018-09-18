package com.compiler.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class CompilerAboutFrame extends JDialog {

    private static final long serialVersionUID = 1L;
    private final String VERSION = "1.1";

    private CompilerFrame parent = null;
    private JButton okButton = new JButton("OK");

    public CompilerAboutFrame(CompilerFrame parent) {
        super(parent, true);
        this.parent = parent;
        this.constroy();
    }

    private void constroy() {
        Container container = getContentPane();
        container.setLayout(new BorderLayout());
        container.add(this.createMainPanel(), BorderLayout.CENTER);
        this.adjustFrame();
    }

    private void adjustFrame() {
        addWindowListener(new WindowAdapter() {
            public void windowActivated(WindowEvent e) {
                super.windowActivated(e);
                okButton.requestFocus();
            }

            public void windowClosing(WindowEvent e) {
                parent.setEnabled(true);
            }
        });

        JPanel mainPanel = this.createMainPanel();
        getContentPane().add(mainPanel);
        pack();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        int x = (screenSize.width - getWidth()) / 2;
        int y = (screenSize.height - getHeight()) / 2;

        setLocation(x, y);
        setResizable(false);
        setTitle(parent.getWndTitle() + " - About");
        setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
    }

    private JPanel createMainPanel() {
        JPanel p = new JPanel();
        p.setLayout(new BorderLayout());
        p.add(this.createIcon(), BorderLayout.WEST);
        p.add(this.createLabels(), BorderLayout.CENTER);
        p.add(this.createOKButton(), BorderLayout.EAST);
        return this.getOnLeftPanel(p);
    }

    private JPanel createIcon() {
        JPanel p = new JPanel();
        URL iconURL = getClass().getResource(CompilerFrame.ICON);

        if (iconURL != null) {
            ImageIcon icon = new ImageIcon(iconURL);
            JLabel label = new JLabel(icon);

            p.setSize(icon.getIconWidth(), icon.getIconHeight());
            p.add(label);
        }

        return p;
    }

    private JPanel createLabels() {
        JLabel software = this.createSoftwareLabel();
        JLabel[] description = this.createDescriptionLabels();

        JPanel p = new JPanel(new GridLayout(description.length, 1));
        for (int i = 0; i < description.length; i++) {
            p.add(description[i]);
        }

        JPanel p1 = new JPanel(new BorderLayout());
        p1.add(software, BorderLayout.NORTH);
        p1.add(p, BorderLayout.CENTER);

        return this.getOnLeftPanel(p1);
    }

    private JLabel createSoftwareLabel() {
        String software = parent.getWndTitle() + " " + VERSION;
        JLabel label = new JLabel(software);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        return label;
    }

    private JLabel[] createDescriptionLabels() {
        String emailBol = "blzn@mail.ru";

        String[] strings = { "", "Author", "", "<html><b>Alexandre Bolzon</b></html>",
                "<html><a href='mailto:" + emailBol + "'>" + emailBol + "</a></html>", "", "December 2008" };

        JLabel[] labels = new JLabel[strings.length];
        for (int i = 0; i < strings.length; i++) {
            JLabel label = new JLabel(strings[i]);
            label.setFont(new Font("Tahoma", Font.PLAIN, 11));
            labels[i] = label;
        }

        return labels;
    }

    private JPanel createOKButton() {
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });

        okButton.setFont(new Font("Tahoma", Font.PLAIN, 11));
        return this.getOnLeftPanel(okButton);
    }

    private JPanel getOnLeftPanel(Component component) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p.add(component);
        return p;
    }
}
