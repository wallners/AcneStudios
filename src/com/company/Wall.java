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


    public Wall(String filename) {

        try {
            isWall = new boolean[30][100];
            Scanner scanner = new Scanner(new File(filename));
            while (scanner.hasNext()) {
                x = 0;
                char[] row = scanner.nextLine().toCharArray();
                for (char c : row) {
                    if (c != ' ') {
                        isWall[y][x]= true;
                    } else {
                        isWall[y][x] = false;
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
    public void renderGameObjects(Terminal terminal){
        for (int y = 0; y < 30; y++) {
            for (int x = 0; x < 100; x++) {
                if (isWall[y][x]) {
                    terminal.moveCursor(x, y);
                    terminal.putCharacter('*');
                }
            }
        }
    }
}