package nz.ac.auckland.se206.controllers;

import java.net.URISyntaxException;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;

public class SoundsManager {
    public static Media buttonSound1; // Wooden click sound
    public static MediaPlayer buttonPlayer1;
    public static Media buttonSound2; // Pop sound
    public static MediaPlayer buttonPlayer2;
    private static Media victorySound;
    private static MediaPlayer victoryPlayer;
    private static Media failureSound;
    private static MediaPlayer failurePlayer;
    private static Media beepSound;
    private static MediaPlayer beepPlayer;
    private static Media tapSound;
    private static MediaPlayer tapPlayer;

    private static Media mainPanelBGM; // Currently PVZ
    private static MediaPlayer mainPanelPlayer;
    private static Media zenBGM; // Currently PVZ
    private static MediaPlayer zenPlayer;
    private static Media pencilBGM;
    private static MediaPlayer pencilPlayer;
    private static Media eraserBGM;
    private static MediaPlayer eraserPlayer;

    public enum sfx {
        BUTTON1, BUTTON2, VICTORY, FAIL, BEEP, TAP
    }

    public enum bgm {
        MAINPANEL, INGAME, ZEN, HIDDEN, PENCIL, ERASER
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
        tapSound = new Media(App.class.getResource("/sounds/tap.mp3").toURI().toString());
        tapPlayer = new MediaPlayer(tapSound);
    }

    public static void loadBGM() throws URISyntaxException {
        mainPanelBGM = new Media(App.class.getResource("/sounds/pvzMainMenu.mp3").toURI().toString());
        mainPanelPlayer = new MediaPlayer(mainPanelBGM);
        zenBGM = new Media(App.class.getResource("/sounds/zen.mp3").toURI().toString());
        zenPlayer = new MediaPlayer(zenBGM);
        pencilBGM = new Media(App.class.getResource("/sounds/pencil.mp3").toURI().toString());
        pencilPlayer = new MediaPlayer(pencilBGM);
        eraserBGM = new Media(App.class.getResource("/sounds/eraser.mp3").toURI().toString());
        eraserPlayer = new MediaPlayer(eraserBGM);
    }

    /*
     * 
     */
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
            case TAP:
                if (tapPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                    tapPlayer.stop();
                }
                tapPlayer.play();
                break;
        }

    }

    public static void playBGM(bgm bgmType) {
        switch (bgmType) {
            case MAINPANEL:
                mainPanelPlayer.play();
                mainPanelPlayer.setOnEndOfMedia(new Runnable() {
                    @Override
                    public void run() {
                        mainPanelPlayer.seek(Duration.ZERO);
                        mainPanelPlayer.play();
                    }
                });
                break;
            case ZEN:
                zenPlayer.play();
                zenPlayer.setOnEndOfMedia(new Runnable() {
                    @Override
                    public void run() {
                        zenPlayer.seek(Duration.ZERO);
                        zenPlayer.play();
                    }
                });
                break;
            case HIDDEN:
                break;
            case INGAME:
                break;
            case PENCIL:
                pencilPlayer.play();
                pencilPlayer.setOnEndOfMedia(new Runnable() {
                    @Override
                    public void run() {
                        pencilPlayer.seek(Duration.ZERO);
                        pencilPlayer.play();
                    }
                });
                break;
            case ERASER:
                eraserPlayer.play();
                eraserPlayer.setOnEndOfMedia(new Runnable() {
                    @Override
                    public void run() {
                        eraserPlayer.seek(Duration.ZERO);
                        eraserPlayer.play();
                    }
                });
                break;
        }
    }

    public static void stopAllBGM() {
        zenPlayer.stop();
        mainPanelPlayer.stop();
    }

    public static void stopBGM(bgm bgmType) {
        switch (bgmType) {
            case MAINPANEL:
                mainPanelPlayer.play();
                break;
            case ZEN:
                zenPlayer.stop();
                break;
            case HIDDEN:
                break;
            case INGAME:
                break;
            case PENCIL:
                pencilPlayer.stop();
                break;
            case ERASER:
                eraserPlayer.stop();
                break;
        }
    }

    public static void changeSFXVolume(int volume) {

    }

    public static void changeBGMVolume(int volume) {

    }
}