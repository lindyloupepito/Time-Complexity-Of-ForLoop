/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cmsc142_mp2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

/**
 *
 * @author Lindy Lou Pepito
 */
public class LoopProcessor {
  private ArrayList<String> lines;
  private ArrayList delimiters;
  
  public LoopProcessor(String fileName) {
    delimiters = new ArrayList();
    delimiters.add(';');
    delimiters.add('(');
    delimiters.add(')');
    delimiters.add('{');
    delimiters.add('}');
    lines = new ArrayList();    
    BufferedReader reader;
    try {
      reader = new BufferedReader(new FileReader(fileName));
      String s = "";
      while((s = reader.readLine()) != null) {
        lines.add(s);
      }
    } catch(Exception e) {
      System.out.println("File not found.");
    }
  }
  
  public ArrayList tokenizer() {
    ArrayList<ArrayList> allForLoops = new ArrayList();
    ArrayList<String> singleLoop = new ArrayList();
    String token = "";
    char prev = ' ';
    
    for (int i = 0; i < lines.size(); i++) {
      for (int j = 0; j < lines.get(i).length(); j++) {
        if (lines.get(i).charAt(j) == '}' ||                  //signifies the end of the loop
          (prev == ')' && lines.get(i).charAt(j) == ';')) {   //signifies that loop has no braces and ends with a semicolon only
          if (singleLoop.size() > 0)                          //checks if the loop is not empty
            allForLoops.add(singleLoop);
          singleLoop = new ArrayList();
          token = "";
        } else if (!delimiters.contains(lines.get(i).charAt(j))) {
          token += lines.get(i).charAt(j);
        } else {
          if (token.trim().length() > 0)                      //checks if the string to be added is != ""
            singleLoop.add(token.trim());
          token = "";
        }
        prev = lines.get(i).charAt(j);
      }
    }
    return allForLoops;
  }
  
  public void extractTokens() {
    ArrayList<ArrayList> allForLoops = tokenizer();
    ArrayList<Loop> extractedLoops = new ArrayList();
    for (int i = 0; i < allForLoops.size(); i++) {
      Loop aLoop = new Loop();
      Polynomial num1 = new Polynomial();
      Polynomial num2 = new Polynomial();
      ArrayList<String> initialization = new ArrayList();
      String comparator = "";
      int conditionTerms = 0;
      int numOfOperations = 0;
      char variable = ' ';                                                                //variable in the initialization
      for (int j = 1; j < allForLoops.get(i).size(); j++) {                               //starts with 1 because 'for' is not included anymore
        String aString = ((String) allForLoops.get(i).get(j)).replace(" ", "");           //for simplicity
        if (j == 1) {
          String[] array = ((String) allForLoops.get(i).get(j)).split(",");
          for (int k = 0; k < array.length; k++) {
            initialization.add(array[k].replace(" ", ""));
          }
        } else if (j == 2) {
          variable = aString.charAt(0);                                                  //gets the variable to be checked in the condition
          for (int k = 0; k < initialization.size(); k++) {
            if (initialization.get(k).charAt(initialization.get(k).indexOf("=")-1) == variable) {
              num1.getTerms().add(new Term(initialization.get(k).substring(initialization.get(k).indexOf("=")+1, initialization.get(k).length())));
              break;
            }
          }
          comparator = getComparator(aString);
          String s;
          if (comparator.equals("<") || comparator.equals(">")) {
            s = aString.substring(aString.indexOf(comparator)+1);
          } else {
            s = aString.substring(aString.indexOf(comparator)+2);
          }
          String temp = "";
          for (int k = 0; k < s.length(); k++) {
            if (s.charAt(k) == '+') {
              num2.getTerms().add(new Term(temp));
              conditionTerms += 1;
              temp = "";
            } else if (s.charAt(k) == '-') {
              num2.getTerms().add(new Term(temp));
              conditionTerms += 1;
              temp = "-";
            } else {
              temp += s.charAt(k);
            }
          }
          num2.getTerms().add(new Term(temp));
          conditionTerms += 1;
          numOfOperations += conditionTerms;
          num2.simpifyPolynomial();
        } else if (j == 3) {
          String[] arr = ((String) allForLoops.get(i).get(j)).split(",");
          String updateExpression = "";
          for (int k = 0; k < arr.length; k++) {
            arr[k] = arr[k].replace(" ", "");
            numOfOperations += getNumOfOperations(arr[k]);
            if (arr[k].charAt(0) == variable) {
              updateExpression = arr[k];
            }
          }
          
          ArrayList res = getStep(updateExpression);
          aLoop.setStep((Polynomial)res.get(0));
          String operation = (String)res.get(1);
          if (operation.equals("++") || operation.equals("+=") || operation.equals("*=")) {
            if (comparator.equals("<") || comparator.equals("<=")) {
              if (comparator.equals("<")) {
                num2.getTerms().add(new Term("-1"));
              }
              aLoop.setUpperBound(num2);
              aLoop.setLowerBound(num1);
            } else if (comparator.equals("!=")) {
              if (isDouble(num1) && isDouble(num2)) {
                if (num1.getTerms().get(0).getCoefficient() < num2.getTerms().get(0).getCoefficient()) {
                  num2.getTerms().add(new Term("-1"));
                  aLoop.setUpperBound(num2);
                  aLoop.setLowerBound(num1);
                } else {
                  aLoop.setLowerBound(new Polynomial(new Term("-1")));
                  aLoop.setUpperBound(new Polynomial(new Term("-1")));
                }
              } else {
                num2.getTerms().add(new Term("-1"));
                aLoop.setUpperBound(num2);
                aLoop.setLowerBound(num1);
              }
            } else {
              aLoop.setLowerBound(new Polynomial(new Term("-1")));
              aLoop.setUpperBound(new Polynomial(new Term("-1")));
            }
          } else if (operation.equals("--") || operation.equals("-=") || operation.equals("/=")) {
            if (comparator.equals(">") || comparator.equals(">=")) {
              if (comparator.equals(">=")) {
                num1.getTerms().add(new Term("1"));
              }
              aLoop.setUpperBound(num1);
              aLoop.setLowerBound(num2);
            } else if (comparator.equals("!=")) {
              if (isDouble(num1) && isDouble(num2)) {
                if (num1.getTerms().get(0).getCoefficient() > num2.getTerms().get(0).getCoefficient()) {
                  num1.getTerms().add(new Term("-1"));
                  aLoop.setUpperBound(num1);
                  aLoop.setLowerBound(num2);
                } else {
                  aLoop.setLowerBound(new Polynomial(new Term("-1")));
                  aLoop.setUpperBound(new Polynomial(new Term("-1")));
                }
              } else {
                num1.getTerms().add(new Term("-1"));
                aLoop.setUpperBound(num1);
                aLoop.setLowerBound(num2);
              }
            } else {
              aLoop.setLowerBound(new Polynomial(new Term("-1")));
              aLoop.setUpperBound(new Polynomial(new Term("-1")));
            }
          }
        } else {
          numOfOperations += getNumOfOperations(aString);
        }
      }
      Polynomial stp;
      if (aLoop.getStep() == null) {
        aLoop.setUpperBound(new Polynomial(new Term("0")));
      } else if (aLoop.getStep().getTerms().get(0).getVariable().contains("log")){
        aLoop.getUpperBound().simpifyPolynomial();
        if (upperBoundIsDouble(aLoop.getUpperBound())) {
          int base = Integer.parseInt(aLoop.getStep().getPolynomial().replace("log", ""));
          aLoop.setUpperBound(new Polynomial(new Term(Double.toString(logN(aLoop.getUpperBound().getTerms().get(0).getCoefficient(), base)))));
        } else {
          String s = aLoop.getStep().getTerms().get(0).getVariable() + "(" + aLoop.getUpperBound().getPolynomial() + ")";
          aLoop.setUpperBound(new Polynomial(new Term(s)));
        } 
      } else {
        stp = new Polynomial(new Term(Double.toString(Math.abs(aLoop.getStep().getTerms().get(0).getCoefficient()))));
        aLoop.setUpperBound(aLoop.getUpperBound().multiplyPolynomial(stp));
      }
      aLoop.getUpperBound().simpifyPolynomial();
      aLoop.setNumOfOperations(new Polynomial(new Term(Integer.toString(numOfOperations))));
      System.out.println("\nLowerBound: " + aLoop.getLowerBound().getPolynomial() +
              " \nUpperbound: " + aLoop.getUpperBound().getPolynomial() +
              " \nNumber of operations: " + aLoop.getNumOfOperations().getPolynomial());
      
      if (aLoop.getLowerBound().getPolynomial().equals("-1") && aLoop.getLowerBound().getPolynomial().equals("-1")) {
        aLoop.setTimeCompexity(new Polynomial(new Term(Character.toString('\u221E'))));
      } else {
        aLoop.setTimeCompexity(computeTimeComplexity(aLoop, initialization.size(), conditionTerms));
      }
      System.out.println("T(n) = " + aLoop.getTimeCompexity().getPolynomial());
      extractedLoops.add(aLoop);
    }
  }
  
  
  //log calculator that accepts any value of the base
  public double logN(double a, int base) {
    return Math.log(a) / Math.log(base);
  }
  
  
  public boolean upperBoundIsDouble(Polynomial upperbound) {
    for (int j = 0; j < upperbound.getTerms().size(); j++) {
      if (!upperbound.getTerms().get(j).getVariable().equals("")) {
        return false;
      }
    }
    return true;
  }
  
  
  public Polynomial computeTimeComplexity(Loop aLoop, int numOfInitialization, int conditionTerms) {
    Polynomial temp = (aLoop.getNumOfOperations().multiplyPolynomial(((aLoop.getUpperBound().subtractPolynomial(aLoop.getLowerBound())).addPolynomial(new Polynomial(new Term("1")))))).addPolynomial(new Polynomial(new Term(Integer.toString(conditionTerms)))).addPolynomial(new Polynomial(new Term(Integer.toString(numOfInitialization))));
    temp.simpifyPolynomial();
    return temp;
  }
  
  public boolean isDouble(Polynomial poly) {
    boolean toReturn = false;
    for (int i = 0; i < poly.getTerms().size(); i++) {
      try {
        Double.parseDouble(poly.getTerms().get(i).getTerms());
        toReturn = true;
      } catch (NumberFormatException e) {
        return false;
      }
    }
    return toReturn;
  }
  
  
  public String getComparator(String aString) {
    String comparator = "";
    if (aString.contains("<=")) {
      comparator = "<=";
    } else if (aString.contains(">=")) {
      comparator = ">=";
    } else if (aString.contains("!=")) {
      comparator = "!=";
    } else if (aString.contains("<")) {
      comparator = "<";
    } else if (aString.contains(">")) {
      comparator = ">";
    } else if (aString.contains("=")) {
      comparator = "=";
    }
    return comparator;
  }
  

  //assumes that update is always a number
  public ArrayList getStep(String aString) {
    ArrayList updateAndOperation = new ArrayList();
    Polynomial update = new Polynomial();
    String operation = "";
    if (aString.contains("++")) {
      update.getTerms().add(new Term("1"));
      operation = "++";
    } else if (aString.contains("--")) {
      update.getTerms().add(new Term("-1"));  
      operation = "--";
    } else if (aString.contains("+=")) { 
      double num = 1 / Double.parseDouble(aString.substring(aString.indexOf("=")+1, aString.length()));
      update.getTerms().add(new Term(Double.toString(num)));
      operation = "+=";
    } else if (aString.contains("-=")) {
      double num = 1 / ((Double.parseDouble(aString.substring(aString.indexOf("=")+1, aString.length()))) * 1);
      update.getTerms().add(new Term(Double.toString(num)));
      operation = "-=";
    } else if (aString.contains("*=")) {
      update.getTerms().add(new Term("log" + Integer.parseInt(aString.substring(aString.indexOf("=")+1, aString.length()))));
      operation = "*=";
    } else if (aString.contains("/=")) {
      update.getTerms().add(new Term("log" + Integer.parseInt(aString.substring(aString.indexOf("=")+1, aString.length()))));
      operation = "/=";
    }
    
    updateAndOperation.add(update);
    updateAndOperation.add(operation);
    return updateAndOperation;
  }
  
  
  public int getNumOfOperations(String aString) {
    int numOfOperations = 0;
    for (int i = 0; i < aString.length(); i++) {
      if ((aString.charAt(i) == '+' && (aString.charAt(i+1) == '=' || aString.charAt(i+1) == '+')) ||
        (aString.charAt(i) == '-' && (aString.charAt(i+1) == '=' || aString.charAt(i+1) == '-')) ||
        ((aString.charAt(i) == '*' || aString.charAt(i) == '/') && aString.charAt(i+1) == '=')) {
        numOfOperations += 1;
        i += 1; //since the next character is already checked
      } else if (aString.charAt(i) == '+' ||
          aString.charAt(i) == '-' ||
          aString.charAt(i) == '*' ||
          aString.charAt(i) == '/' ||
          aString.charAt(i) == '=') {
        numOfOperations += 1;
      }
    }
    return numOfOperations;
  }  
}
