/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cmsc142_mp2;

import java.util.ArrayList;

/**
 *
 * @author lindyloupepito
 */
public final class Polynomial {
  private ArrayList<Term> terms;
  
  public Polynomial() {
    terms = new ArrayList();
  }
  
  public Polynomial(Term term) {
    terms = new ArrayList();
    terms.add(term);
  }

  public ArrayList<Term> getTerms() {
    return terms;
  }
  
  public Polynomial addPolynomial(Polynomial anotherPoly) {
    Polynomial newPolynomial = new Polynomial();
      for (int i = 0; i < terms.size(); i++) {      //done so that the terms in this polynomial
      newPolynomial.getTerms().add(terms.get(i));   // will not be modified since java is pass by reference
    }
    
    for (int i = 0; i < anotherPoly.getTerms().size(); i++) {
      newPolynomial.getTerms().add(anotherPoly.getTerms().get(i));
    }
    
    newPolynomial.simpifyPolynomial();
    return newPolynomial;
  }
  
  public Polynomial subtractPolynomial(Polynomial anotherPoly) {
    Polynomial newPolynomial = new Polynomial();
    for (int i = 0; i < terms.size(); i++) {
      newPolynomial.getTerms().add(terms.get(i));
    }
   
    for (int i = 0; i < anotherPoly.getTerms().size(); i++) {
      anotherPoly.getTerms().get(i).setCoefficient(anotherPoly.getTerms().get(i).getCoefficient()*-1);
      newPolynomial.getTerms().add(anotherPoly.getTerms().get(i));
    }
    
    newPolynomial.simpifyPolynomial();
    return newPolynomial;
  }
  
  public Polynomial multiplyPolynomial(Polynomial anotherPoly) {    
    simpifyPolynomial();
    anotherPoly.simpifyPolynomial();
    Polynomial product = new Polynomial();
    for (int i = 0; i < anotherPoly.getTerms().size(); i++) {
      Term aPoly = anotherPoly.getTerms().get(i);
      for (int j = 0; j < terms.size(); j++) {
        Term temp = new Term();         //for simplicity
        if (aPoly.getVariable().equals("") && terms.get(j).getVariable().equals("")){
          temp.setCoefficient(aPoly.getCoefficient() * terms.get(j).getCoefficient());
          temp.setVariable("");
          temp.setDegree(1);
        } else if (aPoly.getVariable().equals(terms.get(j).getVariable())) {
          temp.setCoefficient(aPoly.getCoefficient() * terms.get(j).getCoefficient());
          temp.setVariable(aPoly.getVariable());
          temp.setDegree(aPoly.getDegree() + terms.get(j).getDegree());
        } else {
          temp.setCoefficient(aPoly.getCoefficient() * terms.get(j).getCoefficient());
          temp.setVariable(aPoly.getVariable() + terms.get(j).getVariable());
          if (aPoly.getVariable().equals("")) {
            temp.setDegree(terms.get(j).getDegree());
          } else if (terms.get(j).getVariable().equals("")) {
            temp.setDegree(aPoly.getDegree());
          } else if (aPoly.getDegree() < terms.get(j).getDegree()) {
            temp.setDegree(terms.get(j).getDegree());
          } else {
            temp.setDegree(aPoly.getDegree());
          }          
        }
        product.getTerms().add(temp);
      }
    }
    return product;
  }
  
  public String getPolynomial() {
    String poly = "";
    if (terms.isEmpty()) {
      poly = "0";
    } else {
      for (int i = 0; i < terms.size(); i++) {
        if (i == 0) {
          poly += terms.get(i).getTerms();
        } else if (terms.get(i).getCoefficient() >= 0) {
          poly += "+" + terms.get(i).getTerms();
        } else {
          poly += terms.get(i).getTerms();
        }
      }
    }
    return poly;
  }
  
  public void simpifyPolynomial() {
    Polynomial simplified = new Polynomial();
    while (!terms.isEmpty()) {
      if (terms.get(0).getCoefficient() == 0) {
        terms.remove(0);
      } else {
        int j;
        for (j = 0; j < simplified.getTerms().size(); j++) {
          if (simplified.getTerms().get(j).getVariable().equals(terms.get(0).getVariable()) &&
              simplified.getTerms().get(j).getDegree() == (terms.get(0).getDegree())) {
            double sum = simplified.getTerms().get(j).getCoefficient() + terms.get(0).getCoefficient();
            if (sum != 0) {
              simplified.getTerms().get(j).setCoefficient(sum);
            } else {
              simplified.getTerms().remove(j);
              j--;  
            }
            terms.remove(0);
            break;
          }
        }
        if (j == simplified.getTerms().size()) {
          simplified.getTerms().add(terms.get(0));
          terms.remove(0);
        }
      }
    }
    terms = simplified.getTerms();
  }
}
