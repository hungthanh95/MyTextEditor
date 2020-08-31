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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TextEditor extends JFrame implements ActionListener {
    private HashMap componentMap;
    private JTextArea textArea;
    private String filePath;
    private JTextField searchField;
    private JFileChooser chooser;
    private List<Integer> listSearch;
    private int currentIndex = -1;

    public TextEditor() {
        listSearch = new ArrayList<>();
        setTitle("Text Editor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 500);
        setVisible(true);
        chooser = new JFileChooser();
//        chooser.setFileHidingEnabled(true);
        chooser.setName("FileChooser");
        add(chooser);

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

        searchField = new JTextField();
        searchField.setName("SearchField");

        Icon saveIcon = new ImageIcon("src\\resources\\save-icon.png");
        JButton saveButton = new JButton(saveIcon);
        saveButton.setName("SaveButton");
        saveButton.setBorder(new EmptyBorder(2, 4, 2,4));

        Icon openIcon = new ImageIcon("src\\resources\\open-icon.png");
        JButton openButton = new JButton(openIcon);
        openButton.setName("OpenButton");
        openButton.setBorder(new EmptyBorder(2, 4, 2,4));

        Icon searchIcon = new ImageIcon("src\\resources\\search-icon.png");
        JButton searchButton = new JButton(searchIcon);
        searchButton.setName("StartSearchButton");
        searchButton.setBorder(new EmptyBorder(2, 4, 2,4));

        Icon nextIcon = new ImageIcon("src\\resources\\next-icon.png");
        JButton nextButton = new JButton(nextIcon);
        nextButton.setName("NextMatchButton");
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
                        .addComponent(searchField)
                        .addComponent(searchButton)
                        .addComponent(previousButton)
                        .addComponent(nextButton)
                        .addComponent(regexCheckbox)
        );
        groupLayout.setVerticalGroup(
                groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(openButton)
                        .addComponent(saveButton)
                        .addComponent(searchField)
                        .addComponent(searchButton)
                        .addComponent(previousButton)
                        .addComponent(nextButton)
                        .addComponent(regexCheckbox)
        );

        searchField.addActionListener(this);
        saveButton.addActionListener(event -> saveFile());
        openButton.addActionListener(event -> readFile());
        searchButton.addActionListener(event -> {
            currentIndex = 0;
            findByIndex();
        });
        nextButton.addActionListener(event -> {
            incrementIndex();
            findByIndex();
        });
        previousButton.addActionListener( event -> {
            decrementIndex();
            findByIndex();
        });
        add(panel, BorderLayout.NORTH);
    }
    void findByIndex() {
        String content = textArea.getText();
        String keySearch = searchField.getText();
        if ((content != null && !"".equals(content)) && (keySearch != null && !"".equals(keySearch))) {
            searchAll(content, keySearch);
            int index = listSearch.get(currentIndex);
            if (index != -1) {
                textArea.setCaretPosition(index + keySearch.length());
                textArea.select(index, index + keySearch.length());
                textArea.grabFocus();
            }
        }
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
        searchMenu.setName("MenuSearch");

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
    private void incrementIndex() {
        if (currentIndex == -1) {
            currentIndex = 0;
        } else {
            currentIndex++;
            if (currentIndex >= listSearch.size()) {
                currentIndex = listSearch.size() - 1;
            }
        }
    }

    private void decrementIndex() {
        currentIndex--;
        if (currentIndex < 0) {
            currentIndex = 0;
        }
    }
    private void searchAll(String content, String key) {
        listSearch.clear();
        for (int i = -1; (i = content.indexOf(key, i + 1)) != -1; i++) {
            listSearch.add(i);
        }
    }
    private void displayFileChooser() {
//        chooser.setFileHidingEnabled(false);
        int opt = chooser.showOpenDialog(this);
        if (opt == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            this.filePath = file.getPath();
        }
//        chooser.setFileHidingEnabled(true);
    }

    private void readFile() {
        textArea.setText("");
        currentIndex = -1;
        displayFileChooser();
        if (filePath != null && !"".equals(filePath)) {
            textArea.setText(readFileAsString(filePath));
        } else {
            textArea.setText("");
        }
    }

    private void saveFile() {
        String content = textArea.getText();
        displayFileChooser();
        if (filePath != null && !"".equals(filePath)) {
            writeFileAsString(filePath, content);
        }
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