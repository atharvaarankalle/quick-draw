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

  /**
   * The constructor for the CategorySelector class which initialises the difficultyListMap
   *
   * @throws CsvException if the CSV file is not formatted correctly
   * @throws IOException if the CSV file cannot be read
   * @throws URISyntaxException if a string cannot be converted to a URI reference
   */
  public CategorySelector() throws CsvException, IOException, URISyntaxException {
    difficultyListMap = new HashMap<>();

    // Put a list of categories for each difficulty into the map
    for (Difficulty difficulty : Difficulty.values()) {
      difficultyListMap.put(difficulty, new ArrayList<>());
    }

    // Read the CSV file and add the categories to the map
    for (String[] line : getLines()) {
      difficultyListMap.get(Difficulty.valueOf(line[1])).add(line[0]);
    }
  }

  /**
   * Returns a random category for the given difficulty
   *
   * @param difficulty the difficulty of the category
   * @return a random category for the given difficulty
   */
  public String getRandomCategory(Difficulty difficulty) {
    return difficultyListMap
        .get(difficulty)
        .get(new Random().nextInt(difficultyListMap.get(difficulty).size()));
  }

  /**
   * Returns a list of lines from the CSV file
   *
   * @return a list of lines from the CSV file
   * @throws IOException if the CSV file cannot be read
   * @throws URISyntaxException if a string cannot be converted to a URI reference
   */
  protected List<String[]> getLines() throws CsvException, IOException, URISyntaxException {
    File fileName =
        new File(CategorySelector.class.getResource("/category_difficulty.csv").toURI());

    // Read the CSV file and return the lines
    try (FileReader fileReader = new FileReader(fileName, StandardCharsets.UTF_8);
        CSVReader csvReader = new CSVReader(fileReader)) {
      return csvReader.readAll();
    }
  }

  /**
   * Gets the difficulty list map that contains the categories for each difficulty
   *
   * @return the difficulty list map
   */
  public Map<Difficulty, List<String>> getDifficultyMap() {
    return difficultyListMap;
  }
}
