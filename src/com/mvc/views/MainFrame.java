package com.mvc.views;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public MainFrame(String title) {
        super(title);
        //set Layout manager
        setLayout(new BorderLayout());

        //create Swing component
        final JTextArea textArea = new JTextArea();
        JTextField inputField = new JTextField("Link to file");
        JButton saveButton = new JButton("Save");
        JButton loadButton = new JButton("Load");

        //add Swing components to content pane
        Container c = getContentPane();
        c.add(inputField, BorderLayout.EAST);
//        c.add(button)
    }
}
