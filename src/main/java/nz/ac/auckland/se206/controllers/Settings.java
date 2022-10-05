package nz.ac.auckland.se206.controllers;

public class Settings {

  private double accuracyLevel = 0.0;

  public void setAccuracyLevel(double level) {
    this.accuracyLevel = level;
  }

  public double getAccuracyLevel() {
    return this.accuracyLevel;
  }
}
