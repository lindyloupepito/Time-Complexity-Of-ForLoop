/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cmsc142_mp2;

/**
 *
 * @author lindyloupepito
 */
public final class Term {
  private double coefficient = 0;
  private String variable = "";
  private int degree = 1;
  
  public Term() {
    
  }
  
  public Term(String x) {
    if (x.charAt(0) == '-') {                                                 //checks if the parameter is negative
      if (isDouble(x.replace("-", ""))) {                                     //checks if x has '-' which tells us that it is negative
        this.coefficient = Double.parseDouble(x.replace("-", "")) * -1;
      } else {                                                                //parameter x is a variable and that it has a leading '-' sign
        this.coefficient = -1;                                                //which tells us that the coefficient is -1
        this.variable = x;
      }
    } else {
      if (isDouble(x)) {
        this.coefficient = Double.parseDouble(x);
      } else {
        this.coefficient = 1;
        this.variable = x;
      }
    }
  }
  
  public Term(String coefficient, String variable) {
    this.coefficient = Double.parseDouble(coefficient);
    this.variable = variable;
  }
  
  public Term(String coefficient, String variable, String degree) {
    this.coefficient = Double.parseDouble(coefficient);
    this.variable = variable;
    this.degree = Integer.parseInt(degree);
  }
  
  public Double parseCoefficient(String c) {
    Double coef;
    if (c.contains("-")) {
      coef = Double.parseDouble(c.replaceAll("-", "")) * -1;
    } else {
      coef = Double.parseDouble(c);
    }
    return coef;
  }
  
  public boolean isDouble(String aString) {
    try {
      Double.parseDouble(aString);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }
  
  // make 1.00 to 1 or 0.02000 to 0.02
  public String formatDouble(double d) {
    if(d == (long) d) {
      return Long.toString((long)d);
    } else {
      return Double.toString(d);
    }
  }
  
  public String getTerms() {
    String toReturn;
    if (variable.equals("")) {
      toReturn = formatDouble(coefficient);
    } else if (coefficient == 1 && degree == 1) {
      toReturn = variable;
    } else if (coefficient == -1 && degree == 1) {
      toReturn = "-" + variable;
    } else if (coefficient == 1) {
      toReturn = variable + "^" + degree;
    } else if (degree == 1) {
      toReturn = formatDouble(coefficient) + variable;
    } else {
      toReturn = formatDouble(coefficient) + variable + "^" + degree;
    }
    return toReturn;
  }

  public double getCoefficient() {
    return coefficient;
  }

  public int getDegree() {
    return degree;
  }

  public String getVariable() {
    return variable;
  }

  public void setCoefficient(double coefficient) {
    this.coefficient = coefficient;
  }

  public void setDegree(int degree) {
    this.degree = degree;
  }

  public void setVariable(String variable) {
    this.variable = variable;
  }
}
