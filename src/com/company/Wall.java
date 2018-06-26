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

    public Wall(Terminal terminal, String filename) {

        try {
            List<List<Boolean>> yWallList = new ArrayList<List<Boolean>>();
            Scanner scanner = new Scanner(new File(filename));
            while (scanner.hasNext()) {
                List<Boolean> xWallList = new ArrayList<Boolean>();
                char[] row = scanner.nextLine().toCharArray();
                x = 0;
                for (char c : row) {
                    terminal.moveCursor(x, y);
                    if (c != ' ') {
                        terminal.putCharacter(c);
                        xWallList.add(true);
                    }
                    else {
                        xWallList.add(false);
                    }
                    x++;
                }
                yWallList.add(xWallList);
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