package org.prevayler.tests.demos.demo1;
import org.prevayler.Prevayler;
/** 
 * This is client code to the prevalent system. It does not need to be persisted.
 */
public class PrimeCalculator {
  
  public final Prevayler _prevayler;
  
  public final NumberKeeper _numberKeeper;
  
  public PrimeCalculator(  Prevayler prevayler){
    _prevayler=prevayler;
    _numberKeeper=(NumberKeeper)prevayler.prevalentSystem();
  }
  
  public void start() throws Exception {
    int largestPrime=0;
    int primesFound=0;
    int primeCandidate=_numberKeeper.lastNumber() == 0 ? 2 : _numberKeeper.lastNumber() + 1;
    while (primeCandidate <= 500) {//Integer.MAX_VALUE
      if (isPrime(primeCandidate)) {
    	NumberStorageTransaction nst = new NumberStorageTransaction(primeCandidate); 
        _prevayler.execute(nst);
//        reportSliceNST(nst);
        largestPrime=primeCandidate;
        primesFound=_numberKeeper.numbers().size();
        System.out.println("Primes found: " + primesFound + ". Largest: "+ largestPrime);
      }
      primeCandidate++;
    }
  }
  
  private boolean isPrime(  int candidate){
    if (candidate < 2) {
      return false;
    }
    if (candidate == 2) {
      return true;
    }
    if (candidate % 2 == 0) {
      return false;
    }
    int factor=3;
    double square=Math.ceil(Math.sqrt(candidate));
    while (factor <= square) {
      if (candidate % factor == 0)       return false;
      factor+=2;
    }
    return true;
  }
  
  /**
   * Reporting slice of NumberStorageTransaction. 
   */
//  public static void reportSliceNST(NumberStorageTransaction nst) {
//  	Slicer.reportObject(nst.serialVersionUID);
//  	Slicer.reportObject(nst._numberToKeep);
//    }
}
