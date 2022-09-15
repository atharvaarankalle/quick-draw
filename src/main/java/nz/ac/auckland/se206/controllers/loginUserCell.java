package nz.ac.auckland.se206.controllers;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

import java.nio.file.*;
/**
 * This part of code is specifically for the stats button in login page. 
 * Answer to question: JavaFX - ListView Item with an Image Button
 * On stackOverFlow. Published by Rainer Schwarze on Mar 28, 2013 at 20:02
 * https://stackoverflow.com/questions/15661500/javafx-listview-item-with-an-image-button
 */
public class loginUserCell extends ListCell<String> {
    HBox hbox = new HBox();
    Label label = new Label("Null");
    Pane pane = new Pane();
    Button button = new Button("Stats");
    String text;

    public loginUserCell() {
        super();
        hbox.getChildren().addAll(label,pane, button);
        HBox.setHgrow(pane, Priority.ALWAYS);
        // Set the action when stats button bening clicked
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // First write to the UserDatas.txt (Copied from login controller)
                // ----------
                Path path = Paths.get("DATABASE/UserDatas.txt");
                long count;
                try {
                    count = Files.lines(path).count();
                    /// Read each line
                    for (int i = 0; i < count; i++) {
                        String vertification = Files.readAllLines(path).get(i);
                        if (vertification.equals(getItem())) { // Confirmation of valid user
                            addLine(getItem());
                            break;
                        }
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                // ----------
                // Then switch the scene
                Parent newScoreboard;
                try {
                    newScoreboard = new FXMLLoader(this.getClass().getResource("/fxml/scoreboard.fxml")).load();
                    Scene currentScene = ((Button) event.getSource()).getScene();
                    currentScene.setRoot(newScoreboard);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // --------
            }
        });
    }
    //Override the updateItem method
    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setGraphic(null);
        } else {
            label.setText(item != null ? item : "<null>");
            setGraphic(hbox);
        }
    }

    /*
     * Copied from login controller
     */
    private void addLine(String line) throws IOException {
        FileWriter file_writer;

        try {
            file_writer = new FileWriter("DATABASE/UserDatas.txt", true);
            BufferedWriter buffered_Writer = new BufferedWriter(file_writer);
            buffered_Writer.write(line);
            buffered_Writer.newLine();
            buffered_Writer.flush();
            buffered_Writer.close();

        } catch (IOException e) {
            System.out.println("Add line failed!!" + e);
        }
    }
}
