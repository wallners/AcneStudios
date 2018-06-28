package com.company;


import com.googlecode.lanterna.terminal.Terminal;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class GameObjects {
    int x;
    int y = 0;
    public static boolean[][] isWall;
    public static boolean[][] isPimple;
    public int pimplesLeft;

    public GameObjects(String filename) {

        try {
            isWall = new boolean[30][100];
            isPimple = new boolean[30][100];
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
                        isPimple[y][x] = true;
                        pimplesLeft++;
                    } else {
                        isPimple[y][x] = false;
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
                    terminal.putCharacter('\u2588');
                }
            }
        }

    }

    public void renderCoin(Terminal terminal) {
        terminal.applyForegroundColor(255, 153, 204);
        for (int y = 0; y < 30; y++) {
            for (int x = 0; x < 100; x++) {
                if (isPimple[y][x]) {
                    terminal.moveCursor(x, y);
                    terminal.putCharacter('\u25C9');
                }
            }
        }
    }
    public void removePimple(int x, int y){
        isPimple[y][x] = false;
    }
}