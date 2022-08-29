package nz.ac.auckland.se206.controllers;

import java.util.HashMap;
import javafx.scene.Parent;

public class SceneManager {

  public enum AppUi {
    MAIN_MENU,
    HOW_TO_PLAY,
    CANVAS,
    LOGIN
  }

  private static HashMap<AppUi, Parent> sceneMap = new HashMap<AppUi, Parent>();

  /**
   * Loads the specified scene into the sceneMap.
   *
   * @param appUi The AppUi enum value of the scene to load.
   * @param uiRoot The root node of the scene to load.
   */
  public static void addUi(AppUi appUi, Parent uiRoot) {
    sceneMap.put(appUi, uiRoot);
  }

  /**
   * Returns the root node of the specified scene.
   *
   * @param appUi The AppUi enum value of the scene to return.
   * @return The root node of the specified scene.
   */
  public static Parent getUiRoot(AppUi appUi) {
    return sceneMap.get(appUi);
  }
}
