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
        Player player = new Player(10,20);

        //Create the enemies
        Enemy[] enemies = new Enemy[4];
        enemies[0] = new Enemy(5,5);
        enemies[1] = new Enemy(15,5);
        enemies[2] = new Enemy(5,15);
        enemies[3] = new Enemy(15,15);



        boolean gameOver = false;

        while(!gameOver){

            updateScreen(player, terminal, enemies);
            Wall wall = new Wall(terminal, "maze-wall");
            movePlayer(player, terminal, wall);
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
                int diffx = player.x - enemy.x;
                if (diffx > 0)
                    enemy.x = enemy.x + 1;
                else
                    enemy.x = enemy.x - 1;
//               enemy.x += (diffx > 0 ? 1 : -1);
            }

            if (enemy.y != player.y) {
                int diffy = player.y - enemy.y;
                if (diffy > 0)
                    enemy.y = enemy.y + 1;
                else
                    enemy.y = enemy.y - 1;
                // enemy.y += (diffy > 0 ? 1 : -1);
            }

            //Check if we are game over?
            if (enemy.x == player.x && enemy.y == player.y)
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
            terminal.moveCursor(enemy.x, enemy.y);
            terminal.putCharacter(enemy.displaychar); // lägga in eller ta ut från emeny
        }

        //Put the cursor on a fixed position after rendering to avoid flickering
        terminal.moveCursor(0,0);

    }

    //Check the keyboard and move the player one step
    private static void movePlayer(Player player, Terminal terminal, Wall wall) {

        // lägg in en try/catch

        //Wait for a key to be pressed
        Key key;
        do{
            //Thread.sleep(5);
            key =terminal.readInput();
        }
        while(key == null);


        System.out.println(player.y + " " + player.x);
        switch(key.getKind())
        {
            case ArrowDown:

                if (wall.wallList[player.y +1][player.x]) {
                    player.y = player.y + 1;
                }

                break;
            case ArrowUp:
                if (true)
                    player.y = player.y - 1;
                break;
            case ArrowLeft:
                if (true)
                    player.x = player.x - 1;
                break;
            case ArrowRight:
                if (true)
                    player.x = player.x + 1;
                break;
        }


        System.out.println(key.getCharacter()+ " " + key.getKind());
    }
}

