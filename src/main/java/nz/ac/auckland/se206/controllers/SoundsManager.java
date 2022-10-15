package nz.ac.auckland.se206.controllers;

import java.net.URISyntaxException;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;

public class SoundsManager {
  public enum SoundEffects {
    BUTTON1,
    BUTTON2,
    VICTORY,
    FAIL,
    BEEP,
    TAP,
    PENCIL,
    ERASER,
    LOGIN;
  }

  public enum BackgroundMusic {
    MAINPANEL,
    INGAME,
    ZEN,
    HIDDEN
  }

  private static Media buttonSound1; // Wooden click sound
  private static MediaPlayer buttonPlayer1;
  private static Media buttonSound2; // Pop sound
  private static MediaPlayer buttonPlayer2;
  private static Media victorySound;
  private static MediaPlayer victoryPlayer;
  private static Media failureSound;
  private static MediaPlayer failurePlayer;
  private static Media beepSound;
  private static MediaPlayer beepPlayer;
  private static Media tapSound;
  private static MediaPlayer tapPlayer;
  private static Media pencilSound;
  private static MediaPlayer pencilPlayer;
  private static Media eraserSound;
  private static MediaPlayer eraserPlayer;
  private static Media loginSound;
  private static MediaPlayer loginPlayer;

  private static Media mainPanelBackgroundMusic; // Currently PVZ
  private static MediaPlayer mainPanelPlayer;
  private static Media zenBackgroundMusic; // Currently PVZ
  private static MediaPlayer zenPlayer;
  private static Media ingameBackgroundMusic;
  private static MediaPlayer ingamePlayer;
  private static Media hiddenBackgroundMusic;
  private static MediaPlayer hiddenPlayer;

  /**
   * This method loads all the SoundEffects .mp3 files required for the game
   *
   * @throws URISyntaxException If there is a error in the .mp3 files name
   */
  public static void loadSoundEffects() throws URISyntaxException {
    // Initilize each Meida field and load the media instance in to its corresponding mediaplayer
    // field
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
    pencilSound = new Media(App.class.getResource("/sounds/pencil.mp3").toURI().toString());
    pencilPlayer = new MediaPlayer(pencilSound);
    eraserSound = new Media(App.class.getResource("/sounds/eraser.mp3").toURI().toString());
    eraserPlayer = new MediaPlayer(eraserSound);
    loginSound = new Media(App.class.getResource("/sounds/successfulLogin.mp3").toURI().toString());
    loginPlayer = new MediaPlayer(loginSound);
  }

  /**
   * This method loads all the BackgroundMusic .mp3 files required for the game
   *
   * @throws URISyntaxException If there is a error in the .mp3 files name
   */
  public static void loadBackgroundMusic() throws URISyntaxException {
    mainPanelBackgroundMusic =
        new Media(App.class.getResource("/sounds/pvzMainMenu.mp3").toURI().toString());
    mainPanelPlayer = new MediaPlayer(mainPanelBackgroundMusic);
    zenBackgroundMusic = new Media(App.class.getResource("/sounds/zen.mp3").toURI().toString());
    zenPlayer = new MediaPlayer(zenBackgroundMusic);
    ingameBackgroundMusic =
        new Media(App.class.getResource("/sounds/ingame.mp3").toURI().toString());
    ingamePlayer = new MediaPlayer(ingameBackgroundMusic);
    hiddenBackgroundMusic =
        new Media(App.class.getResource("/sounds/hidden.mp3").toURI().toString());
    hiddenPlayer = new MediaPlayer(hiddenBackgroundMusic);
  }

  /**
   * Play the specific SoundEffects correspond to the SoundEffects enum input ERASER and PENCIL
   * SoundEffects will be played in loop All other SoundEffects will be played once
   *
   * @param SoundEffectsType A SoundEffects enum input, indicating the specific SoundEffects wanted
   */
  public static void playSoundEffects(SoundEffects SoundEffectsType) {
    // Play the specific SoundEffects based on the SoundEffects enum input
    switch (SoundEffectsType) {
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
        // The victory SoundEffects is always supposed to followed up by main panel BackgroundMusic
        victoryPlayer.setOnEndOfMedia(
            new Runnable() {
              @Override
              public void run() {
                playBackgroundMusic(BackgroundMusic.MAINPANEL);
              }
            });
        break;
      case FAIL:
        if (failurePlayer.getStatus() == MediaPlayer.Status.PLAYING) {
          failurePlayer.stop();
        }
        failurePlayer.play();
        // The failure SoundEffects is always supposed to followed up by main panel BackgroundMusic
        failurePlayer.setOnEndOfMedia(
            new Runnable() {
              @Override
              public void run() {
                playBackgroundMusic(BackgroundMusic.MAINPANEL);
              }
            });
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
      case PENCIL:
        pencilPlayer.play();
        // pencil SoundEffects is supposed to be played in endless loop
        pencilPlayer.setOnEndOfMedia(
            new Runnable() {
              @Override
              public void run() {
                pencilPlayer.seek(Duration.ZERO);
                pencilPlayer.play();
              }
            });
        break;
      case ERASER:
        eraserPlayer.play();
        // Eraser SoundEffects is supposed to be played in endless loop
        eraserPlayer.setOnEndOfMedia(
            new Runnable() {
              @Override
              public void run() {
                eraserPlayer.seek(Duration.ZERO);
                eraserPlayer.play();
              }
            });
        break;
      case LOGIN:
        if (loginPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
          loginPlayer.stop();
        }
        loginPlayer.play();
        break;
    }
  }

  /**
   * Play the specific SoundEffects correspond to the BackgroundMusic enum input All
   * BackgroundMusics will be played in loop
   *
   * @param BackgroundMusicType a BackgroundMusic enum input, indicating the BackgroundMusic wanted
   */
  public static void playBackgroundMusic(BackgroundMusic BackgroundMusicType) {
    // Play the specific BackgroundMusic corresponding to the BackgroundMusic enum input
    // All BackgroundMusics are expected to play in endless loop
    switch (BackgroundMusicType) {
      case MAINPANEL:
        mainPanelPlayer.play();
        mainPanelPlayer.setOnEndOfMedia(
            new Runnable() {
              @Override
              public void run() {
                mainPanelPlayer.seek(Duration.ZERO);
                mainPanelPlayer.play();
              }
            });
        break;
      case ZEN:
        zenPlayer.play();
        zenPlayer.setOnEndOfMedia(
            new Runnable() {
              @Override
              public void run() {
                zenPlayer.seek(Duration.ZERO);
                zenPlayer.play();
              }
            });
        break;
      case HIDDEN:
        hiddenPlayer.play();
        hiddenPlayer.setOnEndOfMedia(
            new Runnable() {
              @Override
              public void run() {
                hiddenPlayer.seek(Duration.ZERO);
                hiddenPlayer.play();
              }
            });
        break;
      case INGAME:
        ingamePlayer.play();
        ingamePlayer.setOnEndOfMedia(
            new Runnable() {
              @Override
              public void run() {
                ingamePlayer.seek(Duration.ZERO);
                ingamePlayer.play();
              }
            });
        break;
    }
  }

  /**
   * This method stops the win/lose SoundEffects, and resume to the looping of main panel
   * BackgroundMusic after stopping
   */
  public static void stopWinAndLoseSoundEffects() {
    victoryPlayer.stop();
    failurePlayer.stop();
  }

  /**
   * This method stops the pencil or eraser SoundEffects from playing. Error message will be printed
   * in terminal if invalid input is given
   *
   * @param pencilOrEraser a SoundEffects input, indicating weather pencil or eraser is required to
   *     be stopped
   */
  public static void stopPencilOrEraserSoundEffects(SoundEffects pencilOrEraser) {
    if (pencilOrEraser == SoundEffects.PENCIL) {
      // If passed in SoundEffect enum is PENCIL, stop the pencil SoundEffects
      pencilPlayer.stop();
    } else if (pencilOrEraser == SoundEffects.ERASER) {
      // If the passed in SoundEffect enum is ERASER, stop the eraser SoundEffects
      eraserPlayer.stop();
    } else {
      // If the passed in SoundEffect enum isn't ERASER or PENCIL, print a error message
      System.out.println(pencilOrEraser + " is not accepted by stopPencilOrEraser method");
    }
  }

  /** This method stops all curretly playing BackgroundMusic */
  public static void stopAllBackgroundMusic() {
    zenPlayer.stop();
    mainPanelPlayer.stop();
    ingamePlayer.stop();
    hiddenPlayer.stop();
  }

  /**
   * This method stops a specific BackgroundMusic
   *
   * @param BackgroundMusicType The specific BackgroundMusic to be terminated
   */
  public static void stopBackgroundMusic(BackgroundMusic BackgroundMusicType) {
    // Stop the specific BackgroundMusic player corresponding to the BackgroundMusic enum input
    switch (BackgroundMusicType) {
      case MAINPANEL:
        mainPanelPlayer.stop();
        break;
      case ZEN:
        zenPlayer.stop();
        break;
      case HIDDEN:
        hiddenPlayer.stop();
        break;
      case INGAME:
        ingamePlayer.stop();
        break;
    }
  }

  /**
   * This method takes a double as input and change the volume level of all SoundEffects to
   * corresponding value
   *
   * @param volume The disired volume level range from 0 to 100
   */
  public static void changeSoundEffectsVolume(double volume) {
    // If volume input is valid, apply changes to all SoundEffects players
    if (volume >= 0 && volume <= 100) {
      buttonPlayer1.setVolume(volume);
      buttonPlayer2.setVolume(volume);
      victoryPlayer.setVolume(volume);
      failurePlayer.setVolume(volume);
      beepPlayer.setVolume(volume);
      tapPlayer.setVolume(volume);
      pencilPlayer.setVolume(volume);
      eraserPlayer.setVolume(volume);
      loginPlayer.setVolume(volume);
    } else {
      // Otherwise print error message.
      System.out.println(volume + " is a invalid SoundEffects volume level");
    }
  }

  /**
   * This method takes a double as input and change the volume level of all BackgroundMusic to
   * corresponding value
   *
   * @param volume The disired volume level range from 0 to 100
   */
  public static void changeBackgroundMusicVolume(double volume) {
    if (volume >= 0 && volume <= 100) {
      // If volume input is valid, apply changes to all BackgroundMusic players
      mainPanelPlayer.setVolume(volume);
      zenPlayer.setVolume(volume);
      hiddenPlayer.setVolume(volume);
      ingamePlayer.setVolume(volume);
    } else {
      // Otherwise print error message.
      System.out.println(volume + " is a invalid BackgroundMusic volume level");
    }
  }

  /**
   * This method changes the mute status of all SoundEffects, according to the boolean input
   *
   * @param muteStatus boolean input, input false to unmute all
   */
  public static void setMuteAllSoundEffects(Boolean muteStatus) {
    // Set the mute status of all SoundEffects players
    buttonPlayer1.setMute(muteStatus);
    buttonPlayer2.setMute(muteStatus);
    victoryPlayer.setMute(muteStatus);
    failurePlayer.setMute(muteStatus);
    beepPlayer.setMute(muteStatus);
    tapPlayer.setMute(muteStatus);
    pencilPlayer.setMute(muteStatus);
    eraserPlayer.setMute(muteStatus);
    loginPlayer.setMute(muteStatus);
  }

  /**
   * This method changes the mute status of all BackgroundMusic, according to the boolean input
   *
   * @param muteStatus boolean input, input false to unmute all
   */
  public static void setMuteAllBackgroundMusic(Boolean muteStatus) {
    // Set mute status of all BackgroundMusic players
    mainPanelPlayer.setMute(muteStatus);
    zenPlayer.setMute(muteStatus);
    ingamePlayer.setMute(muteStatus);
    hiddenPlayer.setMute(muteStatus);
  }

  public static Boolean isZenBackgroundMusicPlaying() {
    return zenPlayer.getStatus() == MediaPlayer.Status.PLAYING;
  }

  public static Boolean ishiddenBackgroundMusicPlaying() {
    return hiddenPlayer.getStatus() == MediaPlayer.Status.PLAYING;
  }
}
