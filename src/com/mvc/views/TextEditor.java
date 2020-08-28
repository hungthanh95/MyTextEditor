package com.mvc.views;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.HashMap;

public class TextEditor extends JFrame implements ActionListener {
    private HashMap componentMap;
    private JTextArea textArea;
    private String filePath;
    private JTextField inputField;

    public TextEditor() {
        setTitle("Text Editor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 500);
        setVisible(true);
        displayTool();
        displayTextArea();
        displayMenuBar();
    }

    private void displayTextArea() {
        textArea = new JTextArea();
        textArea.setName("TextArea");
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setName("ScrollPane");

        add(scrollPane, BorderLayout.CENTER);
    }
    private void displayTool() {
        JPanel panel = new JPanel();
        GroupLayout groupLayout = new GroupLayout(panel);
        panel.setLayout(groupLayout);

        inputField = new JTextField();
        inputField.setName("FilenameField");

        Icon saveIcon = new ImageIcon("src\\resources\\save-icon.png");
        JButton saveButton = new JButton(saveIcon);
        saveButton.setName("SaveButton");
        saveButton.setBorder(new EmptyBorder(2, 4, 2,4));

        Icon openIcon = new ImageIcon("src\\resources\\open-icon.png");
        JButton openButton = new JButton(openIcon);
        openButton.setName("LoadButton");
        openButton.setBorder(new EmptyBorder(2, 4, 2,4));

        Icon searchIcon = new ImageIcon("src\\resources\\search-icon.png");
        JButton searchButton = new JButton(searchIcon);
        searchButton.setName("StartSearchButton");
        searchButton.setBorder(new EmptyBorder(2, 4, 2,4));

        Icon nextIcon = new ImageIcon("src\\resources\\next-icon.png");
        JButton nextButton = new JButton(nextIcon);
        nextButton.setName("StartSearchButton");
        nextButton.setBorder(new EmptyBorder(2, 4, 2,4));

        Icon previoustIcon = new ImageIcon("src\\resources\\previous-icon.png");
        JButton previousButton = new JButton(previoustIcon);
        previousButton.setName("PreviousMatchButton");
        previousButton.setBorder(new EmptyBorder(2, 4, 2,4));

        JCheckBox regexCheckbox = new JCheckBox("Use regex");
        regexCheckbox.setName("UseRegExCheckbox");

//        panel.add(inputField, BorderLayout.CENTER);
//        panel.add(saveButton, BorderLayout.LINE_START);
//        panel.add(openButton, BorderLayout.LINE_END);
        groupLayout.setAutoCreateGaps(true);
        groupLayout.setAutoCreateContainerGaps(true);
        groupLayout.setHorizontalGroup(
                groupLayout.createSequentialGroup()
                    .addComponent(openButton)
                    .addComponent(saveButton)
                    .addComponent(inputField)
                    .addComponent(searchButton)
                    .addComponent(previousButton)
                    .addComponent(nextButton)
                    .addComponent(regexCheckbox)
        );
        groupLayout.setVerticalGroup(
                groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(openButton)
                    .addComponent(saveButton)
                    .addComponent(inputField)
                    .addComponent(searchButton)
                    .addComponent(previousButton)
                    .addComponent(nextButton)
                    .addComponent(regexCheckbox)
        );


        inputField.addActionListener(this);
        saveButton.addActionListener(event -> saveFile());
        openButton.addActionListener(event -> readFile());
        searchButton.addActionListener(event -> {
            String content = textArea.getText();
            String keySearch = inputField.getText();
            int index = content.indexOf(keySearch);
            textArea.setCaretPosition(index + keySearch.length());
            textArea.select(index, index + keySearch.length());
            textArea.grabFocus();
        });
        add(panel, BorderLayout.NORTH);
    }


    private void displayMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        displayFileMenu(menuBar);
        displaySearchMenu(menuBar);
    }

    private  void displaySearchMenu(JMenuBar menuBar) {

        JMenu searchMenu = new JMenu("Search");
        searchMenu.setMnemonic(KeyEvent.VK_S);
        menuBar.add(searchMenu);

        JMenuItem searchMenuItem = new JMenuItem("Start Search");
        JMenuItem previousMenuItem = new JMenuItem("Previous Match");
        JMenuItem nextMenuItem = new JMenuItem("Next Match");
        JMenuItem regExMenuItem = new JMenuItem("Use RegExp");

        searchMenuItem.setName("MenuStartSearch");
        previousMenuItem.setName("MenuPreviousMatch");
        nextMenuItem.setName("MenuNextMatch");
        regExMenuItem.setName("MenuUseRegExp");

        searchMenu.add(searchMenuItem);
        searchMenu.add(previousMenuItem);
        searchMenu.add(nextMenuItem);
        searchMenu.add(regExMenuItem);
    }

    private void displayFileMenu(JMenuBar menuBar) {

        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        menuBar.add(fileMenu);

        JMenuItem newMenuItem = new JMenuItem("New");
        JMenuItem saveMenuItem = new JMenuItem("Save");
        JMenuItem openMenuItem = new JMenuItem("Open");
        JMenuItem exitMenuItem = new JMenuItem("Exit");

        fileMenu.setName("MenuFile");
        saveMenuItem.setName("MenuSave");
        openMenuItem.setName("MenuOpen");
        exitMenuItem.setName("MenuExit");

        fileMenu.add(newMenuItem);
        fileMenu.add(saveMenuItem);
        fileMenu.add(openMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(exitMenuItem);

        //add event listeners to Menu items
        exitMenuItem.addActionListener(event -> System.exit(0));
        newMenuItem.addActionListener(event -> {
            textArea.setText("");
            filePath = null;
        });
        saveMenuItem.addActionListener(event -> saveFile());
        openMenuItem.addActionListener(event -> readFile());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }

    private void displayFileChooser() {
        JFileChooser chooser = new JFileChooser();
        int opt = chooser.showOpenDialog(this);
        if (opt == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            this.filePath = file.getPath();
        }
    }

    private void readFile() {
        displayFileChooser();
        String content = readFileAsString(filePath);
        textArea.setText(content);
    }

    private void saveFile() {
        String content = textArea.getText();
        if (filePath == null || filePath == "") {
            displayFileChooser();
        } else {
            //do nothing
        }
        writeFileAsString(filePath, content);
    }

    private void writeFileAsString(String filename, String content) {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write(content);
        } catch (IOException | NullPointerException ex) {
            System.out.printf("An exception occurs %s \n", ex.getMessage());
        }
    }

    private String readFileAsString(String filename) {
        try {
            return new String(Files.readAllBytes(Paths.get(filename)));
        } catch (IOException |InvalidPathException | NullPointerException ex) {
            System.out.printf("An exception occurs %s \n", ex.getMessage());
            return "";
        }
    }
    //set all component
    private void createComponentMap() {
        componentMap = new HashMap<String,Component>();
        Component[] components = getContentPane().getComponents();
        for (int i=0; i < components.length; i++) {
            componentMap.put(components[i].getName(), components[i]);
        }
    }
    //get component follow name
    public Component getComponentByName(String name) {
        if (componentMap.containsKey(name)) {
            return (Component) componentMap.get(name);
        }
        else return null;
    }
}