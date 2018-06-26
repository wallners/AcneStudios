package com.company;


import com.googlecode.lanterna.terminal.Terminal;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Wall {
    int x;
    int y;

    public Wall(Terminal terminal, String filename){

        terminal.moveCursor(x, y);
        terminal.putCharacter('O'); // refaktorisera

        try {

            Scanner scanner = new Scanner(new File(filename));
        } catch (FileNotFoundException e) {
            System.err.println("FileNotFoundException");
            System.exit(0);
        }


    }


    private void readWallFromFile() {



    }
}