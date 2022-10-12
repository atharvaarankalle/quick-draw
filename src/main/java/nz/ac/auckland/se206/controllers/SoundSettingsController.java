package nz.ac.auckland.se206.controllers;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.controllers.SoundsManager.sfx;

public class SoundSettingsController implements Initializable {
    @FXML
    private Pane soundSettingsRoot;
    @FXML
    private Slider sfxSlider;
    @FXML
    private Slider bgmSlider;
    @FXML
    private ImageView muteImageView;

    @FXML
    private Button muteButton;

    private Image muteImage;

    private Image unmuteImage;

    private Settings gameSettings;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(
                () -> {
                    Stage stage = (Stage) soundSettingsRoot.getScene().getWindow();

                    gameSettings = (Settings) stage.getUserData();

                    try {
                        updateMuteImage(gameSettings.getMuteStatus());
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }

                    sfxSlider.setValue(gameSettings.getSfxVolume());

                    bgmSlider.setValue(gameSettings.getBgmVolume());
                    if (gameSettings.getMuteStatus() == 0) {
                        updateVolumes();
                    }

                    sfxSlider.valueProperty().addListener(new InvalidationListener() {
                        @Override
                        public void invalidated(Observable observable) {
                            SoundsManager.changeSFXVolume(sfxSlider.getValue() / 100);
                            gameSettings.setSfxVolume(sfxSlider.getValue());
                        }

                    });
                    bgmSlider.valueProperty().addListener(new InvalidationListener() {

                        @Override
                        public void invalidated(Observable observable) {
                            SoundsManager.changeBGMVolume(bgmSlider.getValue() / 100);
                            gameSettings.setBgmVolume(bgmSlider.getValue());
                        }

                    });
                });
    }

    private void updateVolumes() {
        System.out.println(bgmSlider.getValue());
        SoundsManager.changeBGMVolume(bgmSlider.getValue() / 100);
        SoundsManager.changeSFXVolume(sfxSlider.getValue() / 100);
    }

    @FXML
    private void onDragDetected() {
        SoundsManager.playSFX(sfx.TAP);
    }

    @FXML
    private void onMute() throws URISyntaxException {
        if (gameSettings.getMuteStatus() == 0) {
            gameSettings.setMuteStatus(1);
            SoundsManager.changeBGMVolume(0);
            SoundsManager.changeSFXVolume(0);
            updateMuteImage(gameSettings.getMuteStatus());
        } else {
            gameSettings.setMuteStatus(0);
            updateVolumes();
            updateMuteImage(gameSettings.getMuteStatus());
        }
    }

    private void updateMuteImage(int status) throws URISyntaxException {
        muteImage = new Image(App.class.getResource("/images/mute.png").toURI().toString());
        unmuteImage = new Image(App.class.getResource("/images/unmute.png").toURI().toString());
        if (status == 1) {
            muteImageView.setImage(unmuteImage);
            muteButton.setText("UNMUTE");
        } else {
            muteImageView.setImage(muteImage);
            muteButton.setText("MUTE");
        }
    }
}
