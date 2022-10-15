package nz.ac.auckland.se206.controllers;

public class Score implements Comparable<Score> {
  private String id;
  private String word;
  private int time;

  /**
   * Constructor for the Score class. Takes in the id, word and time of the score.
   *
   * @param word The word that was guessed.
   * @param time The time it took to guess the word.
   * @param id   The id of the user.
   */
  public Score(String word, int time, String id) {
    this.word = word;
    this.time = time;
    this.id = id;
  }

  /**
   * Returns the id of the user that is currently logged in to the system.
   *
   * @return The id of the user.
   */
  public String getUsername() {
    return id;
  }

  /**
   * Returns the time taken by the user to draw the word.
   *
   * @return The time taken to draw the word.
   */
  public int getTime() {
    return time;
  }

  /**
   * Returns the word that was drawn by the currently logged in user.
   *
   * @return The word that was drawn.
   */
  public String getWord() {
    return word;
  }

  /**
   * Compares the time taken by the user to draw the word.
   *
   * @param other The other score to compare to.
   * @return The difference between the two scores.
   */
  @Override
  public int compareTo(Score arg0) {
    return this.time - arg0.getTime();
  }
}
