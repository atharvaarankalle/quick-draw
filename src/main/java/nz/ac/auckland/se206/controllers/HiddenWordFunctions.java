package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class HiddenWordFunctions {
    private final static String API_URL = "https://api.dictionaryapi.dev/api/v2/entries/en/";
    private static List<String> definitions;
    private static Boolean hiddenModeStatus = false;

    public static List<String> searchWordDefinetion(String word) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(API_URL + word).build();
        Response response = client.newCall(request).execute();
        ResponseBody responseBody = response.body();
        String jsonString = responseBody.string();

        JSONArray jsonArray = (JSONArray) new JSONTokener(jsonString).nextValue();
        definitions = new ArrayList<String>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonEntryObj = jsonArray.getJSONObject(i);
            JSONArray jsonMeanings = jsonEntryObj.getJSONArray("meanings");

            for (int m = 0; m < jsonMeanings.length(); m++) {
                JSONObject jsonMeaningObj = jsonMeanings.getJSONObject(m);
                JSONArray jsonDefinitions = jsonMeaningObj.getJSONArray("definitions");
                for (int d = 0; d < jsonDefinitions.length(); d++) {
                    JSONObject jsonDefinitionObj = jsonDefinitions.getJSONObject(d);
                    String definition = jsonDefinitionObj.getString("definition");
                    definitions.add(definition);
                }
            }
        } 
        // for(String d : definitions){
        //     System.out.println(d);
        // }
        return definitions;
    }

    public static void toHiddenMode(){
        hiddenModeStatus = true;
    }

    public static void leaveHiddenMode(){
        hiddenModeStatus = false;
    }

    public static Boolean isHiddenMode() {
        return hiddenModeStatus;
    }
}
