package com.company;


import com.googlecode.lanterna.terminal.Terminal;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Wall {
    int x;
    int y = 0;
    public static boolean[][] isWall;
    public static boolean[][] isCoin;
    public int coinsLeft;

    public Wall(String filename) {

        try {
            isWall = new boolean[30][100];
            isCoin = new boolean[30][100];
            Scanner scanner = new Scanner(new File(filename));
            while (scanner.hasNext()) {
                x = 0;
                char[] row = scanner.nextLine().toCharArray();
                for (char c : row) {
                    if (c == '\u058D') {
                        isWall[y][x] = true;
                    } else {
                        isWall[y][x] = false;
                    }
                    if (c == '+') {
                        isCoin[y][x] = true;
                        coinsLeft++;
                    } else {
                        isCoin[y][x] = false;
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

    public void renderWall(Terminal terminal) {
        for (int y = 0; y < 30; y++) {
            for (int x = 0; x < 100; x++) {
                if (isWall[y][x]) {
                    terminal.moveCursor(x, y);
                    terminal.putCharacter('\u2587');
                }
            }
        }

    }

    public void renderCoin(Terminal terminal) {
        for (int y = 0; y < 30; y++) {
            for (int x = 0; x < 100; x++) {
                if (isCoin[y][x]) {
                    terminal.moveCursor(x, y);
                    terminal.putCharacter('+');
                }
            }
        }
    }
    public void removeCoin(int x, int y){
        isCoin[y][x] = false;
    }
}