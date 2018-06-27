package com.company;

import com.googlecode.lanterna.TerminalFacade;
import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.terminal.Terminal;

import java.nio.charset.Charset;
import java.util.Random;

public class Main {

    public static void main(String[] args) {
        // write your code here
        Terminal terminal = TerminalFacade.createTerminal(System.in, System.out, Charset.forName("UTF8"));
        terminal.enterPrivateMode();

        //Create the player and place it in the middle of the game
        Player player = new Player(50, 20);

        //Create the enemies
        Enemy[] enemies = new Enemy[4];
        enemies[0] = new Enemy(5, 5);
        enemies[1] = new Enemy(15, 5);
        enemies[2] = new Enemy(5, 15);
        enemies[3] = new Enemy(15, 15);

        terminal.setCursorVisible(false);
        Wall wall = new Wall("maze-wall");
        wall.renderWall(terminal);


        boolean gameOver = false;

        while (!gameOver) {

            try {
                Thread.sleep(10);

                Enemy.counter++;
                wall.renderCoin(terminal);

                updateScreen(player, terminal, enemies, wall);
                Key key = terminal.readInput();
                if (key != null) {
                    movePlayer(player, wall, key);
                }
                if (Enemy.counter == 10) {
                    gameOver = gameLogic(player, enemies, wall);
                    Enemy.counter = 0;
                }

            } catch
                    (InterruptedException e) {
            }
        }
        printText(5, 5, "Game Over", terminal);
    }

    private static void printText(int x, int y, String message, Terminal terminal) {

        for (int i = 0; i < message.length(); i++) {
            terminal.moveCursor(x++, y);
            terminal.putCharacter(message.charAt(i));
        }
    }

    // Move all the enemies and return true if a monster has killed the player
    private static boolean gameLogic(Player player, Enemy[] enemies, Wall wall) {
        int diffx;
        int diffy;

        for (Enemy enemy : enemies) {
            if (enemy.x != player.x) {

                //Switch logic enemy randomly. 10 % chance enemy walks in the opposite direction.
                if (Math.random() < 0.9) {
                    diffx = player.x - enemy.x;
                } else {
                    diffx = enemy.x - player.x;
                }

                if (diffx > 0 && !wall.isWall[enemy.y][enemy.x + 1]
                        && !isOtherEnemyNearby(enemy, enemies, enemy.x + 1, enemy.y)) {
                    enemy.x = enemy.x + 1;
                } else if (!wall.isWall[enemy.y][enemy.x - 1] && !isOtherEnemyNearby(enemy, enemies, enemy.x - 1, enemy.y)) {
                    enemy.x = enemy.x - 1;
                }
            }

            if (enemy.y != player.y) {
                if (Math.random() < 0.9) {
                    diffy = player.y - enemy.y;
                } else {
                    diffy = enemy.y - player.y;
                }
                if (diffy > 0 && !wall.isWall[enemy.y + 1][enemy.x] && !isOtherEnemyNearby(enemy, enemies, enemy.x, enemy.y + 1)) {
                    enemy.y = enemy.y + 1;
                } else if (!wall.isWall[enemy.y - 1][enemy.x] && !isOtherEnemyNearby(enemy, enemies, enemy.x, enemy.y - 1)) {
                    enemy.y = enemy.y - 1;
                }
            }


            //Render the player and the enemies


            //Check the keyboard and move the player one step

            if (enemy.x == player.x && enemy.y == player.y)
                return true;
        }
        return false;
    }


    private static void updateScreen(Player player, Terminal terminal, Enemy[] enemies, Wall wall) {

        for (int y = 0; y < 30; y++) {
            for (int x = 0; x < 100; x++) {
                if (!wall.isWall[y][x]) {
                    terminal.moveCursor(x, y);
                    terminal.putCharacter(' ');
                }
            }
        }

        //Print out the player
        terminal.moveCursor(player.x, player.y);
        terminal.putCharacter('O'); // refaktorisera

        //print out the enemies
        for (Enemy enemy : enemies) {
            terminal.moveCursor(enemy.x, enemy.y);
            terminal.putCharacter(enemy.displaychar); // lägga in eller ta ut från emeny
        }

        //Put the cursor on a fixed position after rendering to avoid flickering
        terminal.moveCursor(0, 0);

    }

    private static void movePlayer(Player player, Wall wall, Key key) {

        // lägg in en try/catch

        //Wait for a key to be pressed


        switch (key.getKind()) {
            case ArrowDown:

                if (!wall.isWall[player.y + 1][player.x]) {
                    player.y = player.y + 1;
                }
                break;
            case ArrowUp:
                if (!wall.isWall[player.y - 1][player.x]) {
                    player.y = player.y - 1;
                }
                break;
            case ArrowLeft:
                if (!wall.isWall[player.y][player.x - 1]) {
                    player.x = player.x - 1;
                }
                break;
            case ArrowRight:
                if (!wall.isWall[player.y][player.x + 1]) {
                    player.x = player.x + 1;
                }
                break;
        }
    }

    private static boolean isOtherEnemyNearby(Enemy enemy, Enemy[] enemies, int nextX, int nextY) {
        for (Enemy otherEnemy : enemies) {
            if (nextX - otherEnemy.x == 0 && nextY - otherEnemy.y == 0 && enemy != otherEnemy) {
                return true;
            }
        }
        return false;
    }

}