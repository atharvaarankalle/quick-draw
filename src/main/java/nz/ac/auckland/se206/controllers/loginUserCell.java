package nz.ac.auckland.se206.controllers;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;
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

/**
 * This part of code is specifically for the stats button in login page. Answer to question: JavaFX
 * - ListView Item with an Image Button On stackOverFlow. Published by Rainer Schwarze on Mar 28,
 * 2013 at 20:02
 * https://stackoverflow.com/questions/15661500/javafx-listview-item-with-an-image-button
 */
public class LoginUserCell extends ListCell<String> {
  private HBox hbox = new HBox();
  private Label label = new Label("Null");
  private Pane pane = new Pane();
  private Button button = new Button("Stats");

  public LoginUserCell() {
    super();
    hbox.getChildren().addAll(label, pane, button);
    HBox.setHgrow(pane, Priority.ALWAYS);
    // Set the action when stats button bening clicked
    button.setOnAction(
        new EventHandler<ActionEvent>() {
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
              newScoreboard =
                  new FXMLLoader(getClass().getResource("/fxml/scoreboard.fxml")).load();
              Scene currentScene = ((Button) event.getSource()).getScene();
              currentScene.setRoot(newScoreboard);
            } catch (IOException e) {
              e.printStackTrace();
            }
            // --------
          }
        });
  }
  // Override the updateItem method
  @Override
  protected void updateItem(String item, boolean empty) {
    super.updateItem(item, empty);

    // If the cell is empty, set the graphic to null. Otherwise, set the label text accordingly
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
    FileWriter fileWriter;

    // Write a new line containing the user that logged in to the UserDatas.txt file
    try {
      fileWriter = new FileWriter("DATABASE/UserDatas.txt", true);
      BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
      bufferedWriter.write(line);
      bufferedWriter.newLine();
      bufferedWriter.flush();
      bufferedWriter.close();

    } catch (IOException e) {
      // Catch an IOException if the write command fails
      System.out.println("Add line failed!!" + e);
    }
  }
}
