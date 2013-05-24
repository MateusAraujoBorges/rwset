package org.prevayler.implementation;
import org.prevayler.Clock;
import org.prevayler.Prevayler;
import org.prevayler.Query;
import org.prevayler.SureTransactionWithQuery;
import org.prevayler.Transaction;
import org.prevayler.TransactionWithQuery;
import org.prevayler.foundation.serialization.Serializer;
import org.prevayler.implementation.publishing.TransactionPublisher;
//#if SNAPSHOT
import org.prevayler.implementation.snapshot.GenericSnapshotManager;
//#endif
import java.io.IOException;
public class PrevaylerImpl implements Prevayler {
//#if SNAPSHOT
  private PrevalentSystemGuard _guard;
//#endif
  private final Clock _clock;
//#if SNAPSHOT
  private GenericSnapshotManager _snapshotManager;
//#endif
  private final TransactionPublisher _publisher;
  private final Serializer _journalSerializer;
//#if SNAPSHOT
  /** 
 * Creates a new Prevayler
 * @param snapshotManagerThe SnapshotManager that will be used for reading and writing
 * snapshot files.
 * @param transactionPublisherThe TransactionPublisher that will be used for publishing
 * transactions executed with this PrevaylerImpl.
 * @param prevaylerMonitorThe Monitor that will be used to monitor interesting calls to
 * this PrevaylerImpl.
 * @param journalSerializer
 */
  
//#if SNAPSHOT
public
//#endif
 
//#if SNAPSHOT
PrevaylerImpl
//#endif
(
//#if SNAPSHOT
  GenericSnapshotManager snapshotManager
//#endif
,
//#if SNAPSHOT
  TransactionPublisher transactionPublisher
//#endif
,
//#if SNAPSHOT
  Serializer journalSerializer
//#endif
) throws 
//#if SNAPSHOT
IOException
//#endif
, 
//#if SNAPSHOT
ClassNotFoundException
//#endif
 
//#if SNAPSHOT
{
    this(transactionPublisher,journalSerializer);
//#if SNAPSHOT
    _snapshotManager=snapshotManager;
//#endif
    _guard=_snapshotManager.recoveredPrevalentSystem();
    _guard.subscribeTo(_publisher);
  }
//#endif
//#endif
  public PrevaylerImpl(  TransactionPublisher transactionPublisher,  Serializer journalSerializer) throws IOException, ClassNotFoundException {
    _publisher=transactionPublisher;
    _clock=_publisher.clock();
    _journalSerializer=journalSerializer;
  }
  public Object prevalentSystem(){
	  Object result = null;
//#if SNAPSHOT
	  result = _guard.prevalentSystem();
//#endif
	  return result;
  }
  public Clock clock(){
    return _clock;
  }
  public void execute(  Transaction transaction){
    publish(new TransactionCapsule(transaction,_journalSerializer));
  }
  private void publish(  Capsule capsule){
    _publisher.publish(capsule);
  }
  public Object execute(  Query sensitiveQuery) throws Exception {
	  Object result = null;
	//#if SNAPSHOT
    result = _guard.executeQuery(sensitiveQuery,clock());
  //#endif
    return result;
  }

  public Object execute(  TransactionWithQuery transactionWithQuery) throws Exception {
    TransactionWithQueryCapsule capsule=new TransactionWithQueryCapsule(transactionWithQuery,_journalSerializer);
    publish(capsule);
    return capsule.result();
  }
  public Object execute(  SureTransactionWithQuery sureTransactionWithQuery){
    try {
      return execute((TransactionWithQuery)sureTransactionWithQuery);
    }
 catch (    RuntimeException runtime) {
      throw runtime;
    }
catch (    Exception checked) {
      throw new RuntimeException("Unexpected Exception thrown.",checked);
    }
  }
  public void takeSnapshot() throws IOException {
//#if SNAPSHOT
    _guard.takeSnapshot(_snapshotManager);
//#endif
  }
  public void close() throws IOException {
    _publisher.close();
  }
}
