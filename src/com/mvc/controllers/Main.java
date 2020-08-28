package com.mvc.controllers;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public void tutorial_readfile() {
        int  sum = 0;
        File f = new File("C:\\Users\\Admin\\Documents\\Java Project\\MyTextEditor\\src\\com\\mvc\\models\\dataset_91033.txt        ");
        try (Scanner scanner = new Scanner((f))) {
            while (scanner.hasNext()) {
                sum += scanner.nextInt();
            }
        } catch (FileNotFoundException e) {
            System.out.println("No file found!!");
        }
        System.out.println(sum);
    }
    public void tutorial_readfile2() {
        long  max = 0;
        int index = 0;
        ArrayList<Long> population = new ArrayList<Long>();
        File f = new File("C:\\Users\\Admin\\Documents\\Java Project\\MyTextEditor\\src\\com\\mvc\\models\\dataset_91069.txt");
        try (Scanner scanner = new Scanner((f))) {
            scanner.nextLine();
            while (scanner.hasNext()) {
                long input = scanner.nextLong();
                if (input > 2016) {
                    population.add(input);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("No file found!!");
        }
        for (int i = 1; i < population.size(); i++) {
            if (max < population.get(i) - population.get(i - 1)) {
                index = i;
                max = population.get(i) - population.get(i - 1);
            }
        }
        System.out.println(population.get(index));
    }

    public static void main(String[] args) throws IOException {
        File file = new File("file.txt"); // some file
        FileWriter fileWriter = new FileWriter(file);
        PrintWriter printWriter = new PrintWriter(file);
//        fileWriter.write("Lorem ipsum dolor sit amet");
//        printWriter.printf("Lorem %s dolor %s", "ipsum", "sit amet");
//        printWriter.printf("%s", "Lorem ipsum dolor sit amet");
//        printWriter.printf("%s dolor sit %s", "Lorem", "ipsum", "amet");
        printWriter.print("Lorem ipsum "); printWriter.print("dolor sit amet");
        fileWriter.close();
        printWriter.close();
    }
}
