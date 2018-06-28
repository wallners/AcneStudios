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
        Key key;
        Terminal terminal = TerminalFacade.createTerminal(System.in, System.out, Charset.forName("UTF8"));
        terminal.enterPrivateMode();
        terminal.setCursorVisible(false);

        MP3Player mp3Player = new MP3Player();
        mp3Player.play("first-screen.mp3");

        renderScreenMessage("start-screen", terminal, 4, 25);
        printText(42, 24, "Remove all pimples.", terminal);
        printText(41, 26, "Press any key to play.", terminal);

        while (true) {
            key = terminal.readInput();
            if (key != null) {
                terminal.clearScreen();
                mp3Player.stopAll();
                terminal.applyBackgroundColor(77, 40, 0);
                break;
            }
        }

        // Play background music during play screen
        PlayScreenMusicThread playScreenMusicThread = new PlayScreenMusicThread();
        playScreenMusicThread.start();

        Player player = new Player(50, 20);
        Enemy[] enemies = new Enemy[5];
        enemies[0] = new Enemy(15, 15);
        enemies[1] = new Enemy(95, 27);
        enemies[2] = new Enemy(5, 27);
        enemies[3] = new Enemy(95, 15);
        enemies[4] = new Enemy(5, 15);

        // Create walls and pimples
        GameObjects gameObjects = new GameObjects("maze-wall");
        gameObjects.renderWall(terminal);

        int timeLeft = 100000;
        boolean killedByMonster = false;

        while (!killedByMonster && timeLeft > 0 && gameObjects.pimplesLeft > 0) {
            try {
                Thread.sleep(10);
                timeLeft -= 10;
                Enemy.counter++;
                gameObjects.renderPimple(terminal);
                key = terminal.readInput();
                updateScreen(player, terminal, enemies, gameObjects);
                if (key != null) {
                    movePlayer(player, gameObjects, key);
                }
                if (Enemy.counter == 10) {
                    killedByMonster = monsterLogic(player, enemies, gameObjects, mp3Player);
                    Enemy.counter = 0;
                }
                printText(52, 0, "Time left: " + Integer.toString(timeLeft / 10) + " ", terminal);
                printText(32, 0, " Pimples left: " + Integer.toString(gameObjects.pimplesLeft) + "   ", terminal);

            } catch
                    (InterruptedException e) {
                System.err.println("InterruptedException");
                System.exit(0);
            }
        }
        playScreenMusicThread.stopMusic();
        mp3Player.play("final-screen.mp3");

        try {
            terminal.clearScreen();
            Thread.sleep(0);
            if (gameObjects.pimplesLeft == 0) {
                renderScreenMessage("well-done", terminal, 10, 19);
            } else {
                renderScreenMessage("you-suck", terminal, 10, 19);
            }
        } catch (InterruptedException e) {
        }
    }

    private static void renderScreenMessage(String endMessageFileName, Terminal terminal, int yShift, int xShift) {
        terminal.clearScreen();
        terminal.applyBackgroundColor(0, 0, 0);

        for (int y = 0; y < 30; y++) {
            for (int x = 0; x < 100; x++) {
                terminal.moveCursor(x, y);
                terminal.putCharacter(' ');
            }
        }

        try {
            Scanner scanner = new Scanner(new File(endMessageFileName));
            int x;
            int y;
            y = yShift;

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
        terminal.applyForegroundColor(255, 255, 255);

        for (int i = 0; i < message.length(); i++) {
            terminal.moveCursor(x++, y);
            terminal.putCharacter(message.charAt(i));
        }
    }

    private static boolean monsterLogic(Player player, Enemy[] enemies, GameObjects gameObjects, MP3Player mp3Player) {
        int deltaX;
        int deltaY;

        for (Enemy enemy : enemies) {
            if (Math.random() < 0.005) {
                gameObjects.addPimple(enemy.x, enemy.y);
            }
            if (enemy.x != player.x) {
                //Make enemy move stupidly randomly.
                if (Math.random() < 0.8) {
                    deltaX = player.x - enemy.x;
                } else {
                    deltaX = enemy.x - player.x;
                }

                // Make enemy move towards player.
                if (deltaX > 0
                        && !gameObjects.isWall[enemy.y][enemy.x + 1]
                        && !isOtherEnemyNearby(enemy, enemies, enemy.x + 1, enemy.y)) {
                    enemy.x = enemy.x + 1;
                } else if (!gameObjects.isWall[enemy.y][enemy.x - 1]
                        && !isOtherEnemyNearby(enemy, enemies, enemy.x - 1, enemy.y)) {
                    enemy.x = enemy.x - 1;
                }
            }

            if (enemy.y != player.y) {
                if (Math.random() < 0.8) {
                    deltaY = player.y - enemy.y;
                } else {
                    deltaY = enemy.y - player.y;
                }
                if (deltaY > 0 && !gameObjects.isWall[enemy.y + 1][enemy.x]
                        && !isOtherEnemyNearby(enemy, enemies, enemy.x, enemy.y + 1)) {
                    enemy.y = enemy.y + 1;
                } else if (!gameObjects.isWall[enemy.y - 1][enemy.x]
                        && !isOtherEnemyNearby(enemy, enemies, enemy.x, enemy.y - 1)) {
                    enemy.y = enemy.y - 1;
                }
            }

            if (gameObjects.isPimple[player.y][player.x]) {
                mp3Player.play("splash-sound.mp3");
                gameObjects.removePimple(player.x, player.y);
            }

            if (enemy.x == player.x && enemy.y == player.y)
                return true;
        }
        return false;
    }

    private static void updateScreen(Player player, Terminal terminal, Enemy[] enemies, GameObjects gameObjects) {
        for (int y = 0; y < 30; y++) {
            for (int x = 0; x < 100; x++) {
                if (!gameObjects.isWall[y][x] && !gameObjects.isPimple[y][x]) {
                    terminal.moveCursor(x, y);
                    terminal.putCharacter(' ');
                }
            }
        }

        // render player
        terminal.applyForegroundColor(0, 230, 230);
        terminal.moveCursor(player.x, player.y);
        terminal.putCharacter(player.displaychar);

        // render enemies
        for (Enemy enemy : enemies) {
            terminal.applyForegroundColor(128, 255, 0);
            terminal.moveCursor(enemy.x, enemy.y);
            terminal.putCharacter(enemy.displaychar);
        }
    }

    private static void movePlayer(Player player, GameObjects gameObjects, Key key) {

        switch (key.getKind()) {
            case ArrowDown:
                if (!gameObjects.isWall[player.y + 1][player.x]) {
                    player.y = player.y + 1;
                }
                break;
            case ArrowUp:
                if (!gameObjects.isWall[player.y - 1][player.x]) {
                    player.y = player.y - 1;
                }
                break;
            case ArrowLeft:
                if (!gameObjects.isWall[player.y][player.x - 1]) {
                    player.x = player.x - 1;
                }
                break;
            case ArrowRight:
                if (!gameObjects.isWall[player.y][player.x + 1]) {
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