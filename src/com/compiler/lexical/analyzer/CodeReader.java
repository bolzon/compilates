package com.compiler.lexical.analyzer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class CodeReader {

    private int line = 1;
    private int column = 0;
    private File file = null;
    private RandomAccessFile reader = null;

    public CodeReader(String filePath) throws FileNotFoundException, IOException {
        file = new File(filePath);
        reader = new RandomAccessFile(file, "r");
        loadBuffers();
    }

    public String getFileName() {
        return file.getName();
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    public void close() {
        try {
            reader.close();
        }
        catch (Exception e) {
        }
    }

    public void back(int len) {
        skip(-len);
    }

    public void back() {
        skip(-1);
        column--;
    }

    public char readChar() {
        char ch = (char) -1;

        try {
            ch = (char) reader.read();

            if (ch == '\n') {
                line++;
                column = 0;
            } else if (ch != '\r') {
                column++;
            }
        } catch (Exception e) {
            // TODO: unhandled exception
        }

        return ch;
    }

    public char readPreviousChar() {
        back();
        return readChar();
    }

    private void skip(int len) {
        try {
            long offset = reader.getFilePointer();
            reader.seek(offset + len);
        }
        catch (Exception e) {
            // TODO: unhandled exception
            System.err.println(e.getMessage());
        }
    }

    private void loadBuffers() {
        // TODO: load buffers (double-buffer)
    }
}
