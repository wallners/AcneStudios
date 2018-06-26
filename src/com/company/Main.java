package com.company;

import com.googlecode.lanterna.TerminalFacade;
import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.terminal.Terminal;

import java.nio.charset.Charset;

public class Main {

    public static void main(String[] args)  {
        // write your code here
        Terminal terminal = TerminalFacade.createTerminal(System.in, System.out, Charset.forName("UTF8"));
        terminal.enterPrivateMode();

        //Create the player and place it in the middle of the game
        Player player = new Player(50,10);

        //Create the enemies
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

    // Move all the enemies and return true if a monster has killed the player
    private static boolean gameLogic(Player player, Enemy[] enemies) {

        //Move the enemies towards the player
        for (Enemy enemy : enemies) {

            if (enemy.x != player.x) {
                int dx = player.x - (int)enemy.x;
                enemy.x += (dx > 0 ? 1 : -1); // Förenkla logiken
            }

            if (enemy.y != player.y) {
                int dy = player.y - (int)enemy.y;
                enemy.y += (dy > 0 ? 1 : -1);
            }

            //Check if we are game over?
            if ((int)enemy.x == player.x && (int)enemy.y == player.y) // ta bort int? null??
                return true;
        }
        return false;
    }


    //Render the player and the enemies
    private static void updateScreen(Player player, Terminal terminal, Enemy[] enemies) {

        terminal.clearScreen();

        //Print out the player
        terminal.moveCursor(player.x, player.y);
        terminal.putCharacter('O'); // refaktorisera

        //print out the enemies
        for (Enemy enemy : enemies) {
            terminal.moveCursor((int)enemy.x, (int)enemy.y);
            terminal.putCharacter(enemy.displaychar); // lägga in eller ta ut från emeny
        }

        //Put the cursor on a fixed position after rendering to avoid flickering
        terminal.moveCursor(0,0);

    }

    //Check the keyboard and move the player one step
    private static void movePlayer(Player player, Terminal terminal) {

        // lägg in en try/catch

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
                if (player.y < 25)
                    player.y = player.y + 2;
                break;
            case ArrowUp:
                if (player.y > 5)
                    player.y = player.y - 2;
                break;
            case ArrowLeft:
                if (player.x > 5)
                    player.x = player.y - 2;
                break;
            case ArrowRight:
                if (player.x < 95)
                    player.x = player.x + 2;
                break;
        }


        System.out.println(key.getCharacter()+ " " + key.getKind());
    }
}

