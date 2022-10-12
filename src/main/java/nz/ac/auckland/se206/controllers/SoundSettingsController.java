package nz.ac.auckland.se206.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import nz.ac.auckland.se206.controllers.SoundsManager.sfx;

public class SoundSettingsController implements Initializable {
    @FXML
    private Pane soundSettingsRoot;
    @FXML
    private Slider sfxSlider;
    @FXML
    private Slider bgmSlider;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(
        () -> {
        Stage stage = (Stage) soundSettingsRoot.getScene().getWindow();

        Settings gameSettings = (Settings) stage.getUserData();

        sfxSlider.setValue(gameSettings.getSfxVolume());

        bgmSlider.setValue(gameSettings.getBgmVolume());

        updateVolumes();

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
    private void onDragDetected(){
        SoundsManager.playSFX(sfx.TAP);
    }
}
