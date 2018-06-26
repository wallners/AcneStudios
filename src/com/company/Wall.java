package com.company;


import com.googlecode.lanterna.terminal.Terminal;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Wall {
    int x;
    int y = 0;

    public boolean[][] isWall;


    public Wall(Terminal terminal, String filename) {

        try {
            isWall = new boolean[30][100];
            Scanner scanner = new Scanner(new File(filename));
            while (scanner.hasNext()) {
                x = 0;
                char[] row = scanner.nextLine().toCharArray();
                for (char c : row) {
                    terminal.moveCursor(x, y);
                    if (c != ' ') {
                        terminal.putCharacter(c);
                        isWall[y][x]= false;
                    } else {
                        isWall[y][x] = true;
                    }
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