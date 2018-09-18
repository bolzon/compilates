package com.compiler.main;

import javax.swing.JDialog;
import javax.swing.JFrame;

import com.compiler.gui.CompilerFrame;

public class Main {

    public static void main(String[] args) {

        JFrame.setDefaultLookAndFeelDecorated(true);
        JDialog.setDefaultLookAndFeelDecorated(true);

        /*
         * String look = javax.swing.UIManager.getSystemLookAndFeelClassName(); try {
         * javax.swing.UIManager.setLookAndFeel(look); } catch (Exception e) {}
         */

        (new CompilerFrame()).setVisible(true);
    }
}
