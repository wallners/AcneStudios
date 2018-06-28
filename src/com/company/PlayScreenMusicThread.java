package com.company;

public class PlayScreenMusicThread extends Thread {
    MP3Player mp3Player2 = new MP3Player();

    public void run() {
        mp3Player2.play("play-screen.mp3", true);
    }

    public void stopMusic() {
        mp3Player2.stopAll();
    }
}
