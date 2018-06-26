package com.company;


import com.googlecode.lanterna.terminal.Terminal;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Wall {
    int x;
    int y = 0;

    public Wall(Terminal terminal, String filename) {
        try {

            Scanner scanner = new Scanner(new File(filename));
            while (scanner.hasNext()) {
                char[] row = scanner.nextLine().toCharArray();
                x = 0;
                for (char c : row) {
                    terminal.moveCursor(x, y);
                    terminal.putCharacter(c);
                    x++;
                }
                y++;
            }

        } catch (FileNotFoundException e) {
            System.err.println("FileNotFoundException");
            System.exit(0);
        }


    }


    private void readWallFromFile() {


    }
}