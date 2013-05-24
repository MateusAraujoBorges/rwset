package org.prevayler.tests.demos.demo1;
import java.util.Date;

import org.prevayler.Transaction;
/** 
 * To change the state of the business objects, the client code must use a Transaction like this one.
 */
class NumberStorageTransaction implements Transaction {
  
  public static final long serialVersionUID=-2023934810496653301L;
  
  public int _numberToKeep;
  
  private NumberStorageTransaction(){
  }
  
  NumberStorageTransaction(  int numberToKeep){
    _numberToKeep=numberToKeep;
  }
  
  public void executeOn(  Object prevalentSystem,  Date ignored){
    ((NumberKeeper)prevalentSystem).keep(_numberToKeep);
  }
 
}
