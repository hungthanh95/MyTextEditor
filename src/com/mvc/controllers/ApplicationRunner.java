package com.mvc.controllers;
import com.mvc.views.TextEditor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ApplicationRunner {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new TextEditor();
            }
        });

    }
}