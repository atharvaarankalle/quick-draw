package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import nz.ac.auckland.se206.App;

public class TestingMane implements Initializable {

    @FXML
    private BorderPane Test;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    @FXML
    private void a(ActionEvent event) throws IOException {
        Parent view = loadFxml("testing");
        Test.setCenter(view);
    }

    @FXML
    private void b(ActionEvent event) throws IOException {
        Parent view = loadFxml("canvas");
        Test.setCenter(view);
    }

    @FXML
    private void c(ActionEvent event) throws IOException {
        Parent view = loadFxml("howtoplay");
        Test.setCenter(view);
    }

    @FXML
    private void d(ActionEvent event) throws IOException {
        Parent view = loadFxml("scoreboard");
        Test.setCenter(view);
    }

    private static Parent loadFxml(final String fxml) throws IOException {
        return new FXMLLoader(App.class.getResource("/fxml/" + fxml + ".fxml")).load();
    }
}
