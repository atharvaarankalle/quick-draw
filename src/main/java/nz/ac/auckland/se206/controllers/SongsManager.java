package nz.ac.auckland.se206.controllers;

import java.net.URISyntaxException;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;

public class SongsManager {
    public static Media buttonSound1; // Wooden
    public static MediaPlayer buttonPlayer1; // Wooden
    public static Media buttonSound2; // Pop
    public static MediaPlayer buttonPlayer2; // Pop
    private static Media victorySound;
    private static MediaPlayer victoryPlayer;
    private static Media failureSound;
    private static MediaPlayer failurePlayer;
    private static Media beepSound;
    private static MediaPlayer beepPlayer;

    public enum sfx {
        BUTTON1, BUTTON2, VICTORY, FAIL, BEEP
    }

    public static void loadSFX() throws URISyntaxException {
        buttonSound1 = new Media(App.class.getResource("/sounds/woodenButton.mp3").toURI().toString());
        buttonPlayer1 = new MediaPlayer(buttonSound1);
        buttonSound2 = new Media(App.class.getResource("/sounds/popButton.mp3").toURI().toString());
        buttonPlayer2 = new MediaPlayer(buttonSound2);
        victorySound = new Media(App.class.getResource("/sounds/ff14.mp3").toURI().toString());
        victoryPlayer = new MediaPlayer(victorySound);
        failureSound = new Media(App.class.getResource("/sounds/fail.mp3").toURI().toString());
        failurePlayer = new MediaPlayer(failureSound);
        beepSound = new Media(App.class.getResource("/sounds/beep.mp3").toURI().toString());
        beepPlayer = new MediaPlayer(beepSound);
    }

    public static void t (){
        if (buttonPlayer2.getStatus() == MediaPlayer.Status.PLAYING) {
            buttonPlayer2.stop();
        }
        buttonPlayer2.play();
    }

    public static void playSFX(sfx sfxType) {
        switch (sfxType) {
            case BUTTON1:
                if (buttonPlayer1.getStatus() == MediaPlayer.Status.PLAYING) {
                    buttonPlayer1.stop();
                }
                buttonPlayer1.play();
                break;
            case BUTTON2:
                if (buttonPlayer2.getStatus() == MediaPlayer.Status.PLAYING) {
                    buttonPlayer2.stop();
                }
                buttonPlayer2.play();
                break;
            case VICTORY:
                if (victoryPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                    victoryPlayer.stop();
                }
                victoryPlayer.play();
                break;
            case FAIL:
                if (failurePlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                    failurePlayer.stop();
                }
                failurePlayer.play();
                break;
            case BEEP:
                if (beepPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                    beepPlayer.stop();
                }
                beepPlayer.play();
                break;
        }

    }

    public static void playBGM(){
        victoryPlayer.play();
        victoryPlayer.setOnEndOfMedia(new Runnable(){
            @Override
            public void run(){
            victoryPlayer.seek(Duration.ZERO);
            victoryPlayer.play();
            }
        });
    }

    public static void stopBGM(){
        victoryPlayer.stop();
    }
}