//#if CENSOR
package org.prevayler.implementation.publishing.censorship;
import org.prevayler.foundation.serialization.Serializer;
import org.prevayler.implementation.PrevalentSystemGuard;
import org.prevayler.implementation.TransactionTimestamp;
//#if SNAPSHOT
import org.prevayler.implementation.snapshot.GenericSnapshotManager;
//#endif
//#if CENSOR
public class StrictTransactionCensor implements 
//#if CENSOR
TransactionCensor
//#endif
 {
  private PrevalentSystemGuard _king = null;
  private PrevalentSystemGuard _royalFoodTaster;
  private Serializer _snapshotSerializer = null;
  
//#if SNAPSHOT
  public StrictTransactionCensor(  GenericSnapshotManager snapshotManager){
    _king=snapshotManager.recoveredPrevalentSystem();
    _snapshotSerializer=snapshotManager.primarySerializer();
  }
//#endif
  public void approve(  TransactionTimestamp transactionTimestamp) throws RuntimeException, Error {
    try {
      TransactionTimestamp timestampCopy=transactionTimestamp.cleanCopy();
      PrevalentSystemGuard royalFoodTaster=royalFoodTaster(transactionTimestamp.systemVersion() - 1);
      royalFoodTaster.receive(timestampCopy);
    }
 catch (    RuntimeException rx) {
      letTheFoodTasterDie();
      throw rx;
    }
catch (    Error error) {
      letTheFoodTasterDie();
      throw error;
    }
  }
  private void letTheFoodTasterDie(){
    _royalFoodTaster=null;
  }
  private PrevalentSystemGuard royalFoodTaster(  long systemVersion){
    if (_royalFoodTaster == null)     produceNewFoodTaster(systemVersion);
    return _royalFoodTaster;
  }
  private void produceNewFoodTaster(  long systemVersion){
    try {
    //#if SNAPSHOT
      _royalFoodTaster=_king.deepCopy(systemVersion,_snapshotSerializer);
    //#endif
    }
 catch (    Exception ex) {
      ex.printStackTrace();
      throw new RuntimeException("Unable to produce a copy of the prevalent system for trying out transactions before applying them to the real system.");
    }
  }
}
//#endif
//#endif
