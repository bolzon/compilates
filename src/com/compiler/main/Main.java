package com.compiler.main;

import javax.swing.JDialog;
import javax.swing.JFrame;

import com.compiler.gui.CompilerFrame;

public class Main {

    public static void main(String[] args) {

        JFrame.setDefaultLookAndFeelDecorated(true);
        JDialog.setDefaultLookAndFeelDecorated(true);

        try {
            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception e) {
        }

        new CompilerFrame().setVisible(true);
    }
}
