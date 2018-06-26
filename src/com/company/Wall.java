package com.company;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Wall {
    private void createWall() {

        try {

            Scanner scanner = new Scanner(new File("wall.txt"));
        } catch (FileNotFoundException e) {
            System.err.println("FileNotFoundException");
            System.exit(0);
        }
    }
}