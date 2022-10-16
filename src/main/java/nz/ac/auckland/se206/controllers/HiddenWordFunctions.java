package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

public class HiddenWordFunctions {
  private static final String API_URL = "https://api.dictionaryapi.dev/api/v2/entries/en/";
  private static List<String> definitions;
  private static Boolean hiddenModeStatus = false;

  /**
   * This method gets the definitions of the word from the API
   *
   * @param word The word that the user is trying to guess
   * @return A list of definitions of the word
   * @throws IOException if the API cannot be reached
   */
  public static List<String> searchWordDefinetion(String word) throws IOException {

    // Set up the client and request
    OkHttpClient client = new OkHttpClient();
    Request request = new Request.Builder().url(API_URL + word).build();
    Response response = client.newCall(request).execute();
    ResponseBody responseBody = response.body();
    String jsonString = responseBody.string();

    // Parse the JSON response
    JSONArray jsonArray = (JSONArray) new JSONTokener(jsonString).nextValue();
    definitions = new ArrayList<String>();
    for (int i = 0; i < jsonArray.length(); i++) {
      JSONObject jsonEntryObj = jsonArray.getJSONObject(i);
      JSONArray jsonMeanings = jsonEntryObj.getJSONArray("meanings");

      // Get the definitions of the word
      for (int m = 0; m < jsonMeanings.length(); m++) {
        JSONObject jsonMeaningObj = jsonMeanings.getJSONObject(m);
        JSONArray jsonDefinitions = jsonMeaningObj.getJSONArray("definitions");

        // Get all the definitions of the word selected
        for (int d = 0; d < jsonDefinitions.length(); d++) {
          JSONObject jsonDefinitionObj = jsonDefinitions.getJSONObject(d);
          String definition = jsonDefinitionObj.getString("definition");
          definitions.add(definition);
        }
      }
    }
    return definitions;
  }

  /** This method enables the hidden game mode in the application */
  public static void toHiddenMode() {
    hiddenModeStatus = true;
  }

  /** This method disables the hidden game mode in the application */
  public static void leaveHiddenMode() {
    hiddenModeStatus = false;
  }

  /**
   * This method checks if the hidden game mode is enabled
   *
   * @return A boolean value indicating if the hidden game mode is enabled
   */
  public static Boolean isHiddenMode() {
    return hiddenModeStatus;
  }
}
