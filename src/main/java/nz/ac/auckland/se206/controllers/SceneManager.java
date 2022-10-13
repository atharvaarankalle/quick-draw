package nz.ac.auckland.se206.controllers;

import java.util.HashMap;
import javafx.scene.Parent;

public class SceneManager {

  public enum AppUi {
    HOME_PAGE,
    HOW_TO_PLAY,
    CANVAS,
    LOGIN,
    MAIN
  }

  private static HashMap<AppUi, Parent> sceneMap = new HashMap<AppUi, Parent>();

  /**
   * Loads the specified scene into the sceneMap. The sceneMap stores the scene and it's root
   *
   * @param appUi The AppUi enum value of the scene to load.
   * @param uiRoot The root node of the scene to load.
   */
  public static void addUi(AppUi appUi, Parent uiRoot) {
    sceneMap.put(appUi, uiRoot);
  }

  /**
   * Returns the root node of the specified scene. The sceneMap stores the scene and it's root
   *
   * @param appUi The AppUi enum value of the scene to return.
   * @return The root node of the specified scene.
   */
  public static Parent getUiRoot(AppUi appUi) {
    return sceneMap.get(appUi);
  }
}
