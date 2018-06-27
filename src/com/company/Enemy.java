package com.company;

public class Enemy {

    public int x;
    public int y;
    public char displaychar = 'X';

    public Enemy(int x, int y) {
        this.x = x;
        this.y = y;
    }

    private static boolean isOtherEnemyNearby(Enemy enemy, Enemy[] enemies, int nextX, int nextY){
        for (Enemy otherEnemy : enemies) {
            if (nextX - otherEnemy.x == 0 && nextY - otherEnemy.y == 0 && enemy != otherEnemy) {
                return true;
            }
        }
        return false;
    }
}