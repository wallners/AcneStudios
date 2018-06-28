package com.company;

import com.googlecode.lanterna.TerminalFacade;
import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        // write your code here
        Terminal terminal = TerminalFacade.createTerminal(System.in, System.out, Charset.forName("UTF8"));
        terminal.enterPrivateMode();
        MP3Player mp3Player = new MP3Player();
        mp3Player.play("first-screen.mp3");



        Key key;
        renderScreenMessage("start-screen", terminal, 4, 25);
        printText(42,24,"Remove all pimples.",terminal);
        printText(41,26,"Press any key to play.",terminal);

        terminal.setCursorVisible(false);

        while (true) {
            key = terminal.readInput();
            if (key != null) {
                terminal.clearScreen();
                mp3Player.stopAll();
                break;
            }
        }

        //Create the player and place it in the middle of the game
        Player player = new Player(50, 20);

        //Create the enemies
        Enemy[] enemies = new Enemy[5];
        enemies[0] = new Enemy(15, 15);
        enemies[1] = new Enemy(95, 27);
        enemies[2] = new Enemy(5, 27);
        enemies[3] = new Enemy(95, 15);
        enemies[4] = new Enemy(5, 15);

        terminal.setCursorVisible(false);
        Wall wall = new Wall("maze-wall");
        wall.renderWall(terminal);

        int timeLeft = 100000;
        boolean gameOver = false;

        while (!gameOver && timeLeft > 0 && wall.coinsLeft > 0) {

            try {
                Thread.sleep(10);
                timeLeft -= 10;
                Enemy.counter++;
                wall.renderCoin(terminal);
                key = terminal.readInput();
                updateScreen(player, terminal, enemies, wall);
                if (key != null) {
                    movePlayer(player, wall, key);
                }
                if (Enemy.counter == 10) {
                    gameOver = gameLogic(player, enemies, wall, mp3Player);
                    Enemy.counter = 0;
                }

                printText(52, 0, "Time left: " + Integer.toString(timeLeft / 10) + " ", terminal);
                printText(32, 0, " Pimples left: " + Integer.toString(wall.coinsLeft) + "   ", terminal);

            } catch
                    (InterruptedException e) {
            }
        }
        try {
            terminal.clearScreen();
            Thread.sleep(0);
            if (wall.coinsLeft == 0) {
                renderScreenMessage("well-done", terminal, 10, 19);
            } else {
                renderScreenMessage("you-suck", terminal, 10, 19);
            }
        } catch (InterruptedException e) {
        }
    }

    private static void renderScreenMessage(String endMessageFileName, Terminal terminal, int yShift, int xShift) {
        terminal.clearScreen();
        int x;
        try {
            Scanner scanner = new Scanner(new File(endMessageFileName));
            int y = yShift;
            while (scanner.hasNext()) {
                x = xShift;
                char[] row = scanner.nextLine().toCharArray();
                for (char c : row) {
                    terminal.moveCursor(x, y);
                    terminal.putCharacter(c);
                    x++;
                }
                y++;
            }

        } catch (FileNotFoundException e) {
            System.err.println("FileNotFoundException");
            System.exit(0);
        }

    }


    private static void printText(int x, int y, String message, Terminal terminal) {

        for (int i = 0; i < message.length(); i++) {
            terminal.moveCursor(x++, y);
            terminal.putCharacter(message.charAt(i));
        }
    }

    // Move all the enemies and return true if a monster has killed the player
    private static boolean gameLogic(Player player, Enemy[] enemies, Wall wall, MP3Player mp3Player) {
        int diffx;
        int diffy;
        for (Enemy enemy : enemies) {
            if (enemy.x != player.x) {

                //Switch logic enemy randomly.
                if (Math.random() < 0.8) {
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
                if (Math.random() < 0.8) {
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
            if (wall.isCoin[player.y][player.x]) {
                mp3Player.play("splash-sound.mp3");
                wall.removeCoin(player.x, player.y);
                wall.coinsLeft--;
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
                if (!wall.isWall[y][x] && !wall.isCoin[y][x]) {
                    terminal.moveCursor(x, y);
                    terminal.putCharacter(' ');
                }
            }
        }

        //Print out the player
        terminal.moveCursor(player.x, player.y);
        terminal.putCharacter(player.displaychar); // refaktorisera

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