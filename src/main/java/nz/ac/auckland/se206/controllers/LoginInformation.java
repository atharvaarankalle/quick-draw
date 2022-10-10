package nz.ac.auckland.se206.controllers;

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
public class LoginInformation extends ListCell<String> {
  private HBox hbox = new HBox();
  private Label label = new Label("Null");
  private Pane pane = new Pane();
  // private Button button = new Button("Stats");

  public LoginInformation() {
    super();
    hbox.getChildren().addAll(label, pane);
    HBox.setHgrow(pane, Priority.ALWAYS);
  }

  // Override the updateItem method
  @Override
  protected void updateItem(String item, boolean empty) {
    super.updateItem(item, empty);

    // If the cell is empty, set the graphic to null. Otherwise, set the label text
    // accordingly
    if (empty) {
      setGraphic(null);
    } else {
      label.setText(item != null ? item : "<null>");
      setGraphic(hbox);
    }
  }
}
