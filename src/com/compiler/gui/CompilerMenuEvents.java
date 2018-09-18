package com.compiler.gui;

public interface CompilerMenuEvents {

    // File
    public void onFileNew();
    public boolean onFileOpen();
    public boolean onFileSave();
    public boolean onFileSaveAs();
    public void onFileExit();

    // Edit
    public void onEditUndo();
    public void onEditRedo();
    public void onEditCut();
    public void onEditCopy();
    public void onEditPaste();
    public void onEditSelectAll();

    // Format
    public void onFormatWrapLines();

    // Project
    public void onProjectClear();
    public void onProjectCompile();

    /// Help
    public void onHelpAbout();
}
