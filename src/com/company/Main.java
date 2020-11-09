package com.company;

import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        try {
            Lexer lexer = new Lexer();
            FileReader file = new FileReader("test.txt");
            BufferedReader varRead = new BufferedReader(file);
            String line;
            while ((line = varRead.readLine()) != null) {
                lexer.eatLine(line);
            }
            lexer.DiplayResults();
        }
        catch (FileNotFoundException e) {
            System.out.println("File is not found.");
        }
    }
}