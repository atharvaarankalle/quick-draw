package nz.ac.auckland.se206.controllers;

import java.net.URISyntaxException;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import nz.ac.auckland.se206.App;

public class SongsManager {
    public static Media buttonSound1; // Wooden
    public static MediaPlayer buttonPlayer1; // Wooden

    public static void loadSFX() throws URISyntaxException {
        buttonSound1 = new Media(App.class.getResource("/sounds/woodenButton.mp3").toURI().toString());
        buttonPlayer1 = new MediaPlayer(buttonSound1);
    }
}
