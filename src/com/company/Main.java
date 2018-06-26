package com.company;

import com.googlecode.lanterna.TerminalFacade;
import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.terminal.Terminal;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        // write your code here
        Terminal terminal = TerminalFacade.createTerminal(System.in, System.out, Charset.forName("UTF8"));
        terminal.enterPrivateMode();

        //Create the player and place it in the middle of the game
        Player player = new Player(10,10);

        //Create the enemie
        Enemy[] enemies = new Enemy[4];
        enemies[0] = new Enemy(5,5);
        enemies[1] = new Enemy(15,5);
        enemies[2] = new Enemy(5,15);
        enemies[3] = new Enemy(15,15);

        boolean gameOver = false;

        while(!gameOver){

            updateScreen(player, terminal, enemies);
            movePlayer(player, terminal);
            gameOver = gameLogic(player, enemies);
        }

        printText(5,5,"Game Over", terminal);
    }

private static void printText(int x, int y, String message, Terminal terminal) {

    for (int i=0;i<message.length();i++)
    {
        terminal.moveCursor(x++, y);
        terminal.putCharacter(message.charAt(i));
    }
}

    // Move all the enemies and return true if a monster have killed the plauer
    private static boolean gameLogic(Player player, Enemy[] enemies) {

        //Move the enemies towards the player
        for (Enemy enemy : enemies) {

            if (enemy.x != player.x) {
                int dx = player.x - (int)enemy.x;
                enemy.x += (dx > 0 ? 1 : -1);
            }

            if (enemy.y != player.y) {
                int dy = player.y - (int)enemy.y;
                enemy.y += (dy > 0 ? 1 : -1);
            }

            //Check if we are game over?
            if ((int)enemy.x == player.x && (int)enemy.y == player.y)
                return true;
        }
        return false;
    }


    //Render theplayer and the enemies
    private static void updateScreen(Player player, Terminal terminal, Enemy[] enemies) {

        terminal.clearScreen();

        //Print out the player
        terminal.moveCursor(player.x, player.y);
        terminal.putCharacter('O');

        //print out the enemies
        for (Enemy enemy : enemies) {
            terminal.moveCursor((int)enemy.x, (int)enemy.y);
            terminal.putCharacter(enemy.displaychar);
        }

        //Put the cursor on a fixed position after rendering to avoid flickering
        terminal.moveCursor(0,0);

    }

    //Check the keyboard and move the player one step
    private static void movePlayer(Player player, Terminal terminal) throws InterruptedException {

        //Wait for a key to be pressed
        Key key;
        do{
            //Thread.sleep(5);
            key =terminal.readInput();
        }
        while(key == null);

        switch(key.getKind())
        {
            case ArrowDown:
                player.y = (player.y<20) ? player.y+2 : 20;
                break;
            case ArrowUp:
                player.y = (player.y>0) ? player.y-2 : 0;
                break;
            case ArrowLeft:
                player.x = (player.x>0) ? player.x-2 : 0;
                break;
            case ArrowRight:
                player.x = (player.x<20) ? player.x+2 : 20;
                break;
        }


        System.out.println(key.getCharacter()+ " " + key.getKind());
    }
}

