package com.thanhle.texteditor;
import com.thanhle.texteditor.TextEditor;

public class ApplicationRunner {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new TextEditor();
            }
        });

    }
}