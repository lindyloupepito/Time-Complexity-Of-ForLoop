/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cmsc142_mp2;

/**
 *
 * @author lindyloupepito
 */
public class Loop {
  private Polynomial lowerBound;
  private Polynomial upperBound;
  private Polynomial step;
  private Polynomial numOfOperations;
  private Polynomial timeCompexity;
  
  public Loop() {
    lowerBound = new Polynomial();
    upperBound = new Polynomial();
    numOfOperations = new Polynomial();
    timeCompexity = new Polynomial();
  }

  public Polynomial getLowerBound() {
    return lowerBound;
  }

  public Polynomial getNumOfOperations() {
    return numOfOperations;
  }

  public Polynomial getStep() {
    return step;
  }

  public Polynomial getTimeCompexity() {
    return timeCompexity;
  }

  public Polynomial getUpperBound() {
    return upperBound;
  }

  public void setLowerBound(Polynomial lowerBound) {
    this.lowerBound = lowerBound;
  }

  public void setNumOfOperations(Polynomial numOfOperations) {
    this.numOfOperations = numOfOperations;
  }

  public void setStep(Polynomial step) {
    this.step = step;
  }

  public void setTimeCompexity(Polynomial timeCompexity) {
    this.timeCompexity = timeCompexity;
  }

  public void setUpperBound(Polynomial upperbound) {
    this.upperBound = upperbound;
  }
}

