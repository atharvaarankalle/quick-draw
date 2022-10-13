package nz.ac.auckland.se206.controllers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOError;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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

    private String previousUserID = "";

    // SoundsManager.changeBGMVolume( Double.valueOf(userStats.get(userStats.size()
    // - 1).split(" , ")[5])/ 100);
    // SoundsManager.changeSFXVolume( Double.valueOf(userStats.get(userStats.size()
    // - 1).split(" , ")[4])/ 100);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(
                () -> {
                    Stage stage = (Stage) soundSettingsRoot.getScene().getWindow();

                    gameSettings = (Settings) stage.getUserData();

                    if (!(previousUserID.equals(gameSettings.getCurrentUser()))) {
                        previousUserID = gameSettings.getCurrentUser();

                        String currentLine;
                        String lastLine = "";
                        String[] separatedUserInfo = { "" };
                        try {
                            BufferedReader bufferedReader = new BufferedReader(
                                    new FileReader("DATABASE/usersettings/" + previousUserID));

                            while ((currentLine = bufferedReader.readLine()) != null) {
                                lastLine = currentLine;
                            }

                            bufferedReader.close();

                            separatedUserInfo = lastLine.split(" , ");

                            gameSettings.setSfxVolume(Double.valueOf(separatedUserInfo[4]));
                            gameSettings.setBgmVolume(Double.valueOf(separatedUserInfo[5]));
                            gameSettings.setMuteStatus(Integer.valueOf(separatedUserInfo[6]));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    try {
                        updateMuteImage(gameSettings.getMuteStatus());
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }

                    sfxSlider.setValue(gameSettings.getSfxVolume());

                    bgmSlider.setValue(gameSettings.getBgmVolume());

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

    // private void updateVolumes() {
    // SoundsManager.changeBGMVolume(gameSettings.getBgmVolume() / 100);
    // SoundsManager.changeSFXVolume(gameSettings.getSfxVolume() / 100);
    // }

    /*
     * Code adapted from: https://stackoverflow.com/questions/26552495/javafx-set-slider-value-after-dragging-mouse-button
     */
    @FXML
    private void onSFXDragDetected() {
        sfxSlider.valueChangingProperty().addListener(new ChangeListener<Boolean>() {

            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean wasChanging, Boolean isNowChanging) {
                if(!isNowChanging){
                    SoundsManager.playSFX(sfx.TAP);
                    try {
                        addSettingsLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            
        });
    }
    /*
     * Code adapted from: https://stackoverflow.com/questions/26552495/javafx-set-slider-value-after-dragging-mouse-button
     */
    @FXML
    private void onBGMDragDetected() {
        bgmSlider.valueChangingProperty().addListener(new ChangeListener<Boolean>() {

            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean wasChanging, Boolean isNowChanging) {
                if(!isNowChanging){
                    SoundsManager.playSFX(sfx.TAP);
                    try {
                        addSettingsLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            
        });
    }

    @FXML
    private void onMute() throws URISyntaxException, IOException {
        if (gameSettings.getMuteStatus() == 0) {
            gameSettings.setMuteStatus(1);
            SoundsManager.setMuteAllBGM(true);
            SoundsManager.setMuteAllSFX(true);
            updateMuteImage(gameSettings.getMuteStatus());
            addSettingsLine();
        } else {
            gameSettings.setMuteStatus(0);
            SoundsManager.setMuteAllBGM(false);
            SoundsManager.setMuteAllSFX(false);
            updateMuteImage(gameSettings.getMuteStatus());
            addSettingsLine();
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

    private void addSettingsLine() throws IOException {
        FileWriter fileWriter = new FileWriter("DATABASE/usersettings/" + previousUserID, true);

        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

        try {
            String line = Double.toString(gameSettings.getAccuracyLevel())
                    + " , "
                    + Double.toString(gameSettings.getWordsLevel())
                    + " , "
                    + Double.toString(gameSettings.getTimeSliderPosition())
                    + " , "
                    + Double.toString(gameSettings.getConfidenceSliderPosition())
                    + " , "
                    + Double.toString(sfxSlider.getValue())
                    + " , "
                    + Double.toString(bgmSlider.getValue())
                    + " , "
                    + Integer.toString(gameSettings.getMuteStatus());
            bufferedWriter.write(line);
            bufferedWriter.newLine();
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (IOException e) {

        }
    }
}
