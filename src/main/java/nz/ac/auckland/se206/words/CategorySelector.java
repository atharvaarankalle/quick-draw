package nz.ac.auckland.se206.words;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class CategorySelector {

  public enum Difficulty {
    E,
    M,
    H
  }

  private Map<Difficulty, List<String>> difficultyListMap;

  public CategorySelector() throws CsvException, IOException, URISyntaxException {
    difficultyListMap = new HashMap<>();

    for (Difficulty difficulty : Difficulty.values()) {
      difficultyListMap.put(difficulty, new ArrayList<>());
    }

    for (String[] line : getLines()) {
      difficultyListMap.get(Difficulty.valueOf(line[1])).add(line[0]);
    }
  }

  public String getRandomCategory(Difficulty difficulty) {
    return difficultyListMap
        .get(difficulty)
        .get(new Random().nextInt(difficultyListMap.get(difficulty).size()));
  }

  protected List<String[]> getLines() throws CsvException, IOException, URISyntaxException {
    File fileName =
        new File(CategorySelector.class.getResource("/category_difficulty.csv").toURI());

    try (FileReader fileReader = new FileReader(fileName, StandardCharsets.UTF_8);
        CSVReader csvReader = new CSVReader(fileReader)) {
      return csvReader.readAll();
    }
  }
}
