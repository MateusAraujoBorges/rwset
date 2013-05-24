package org.prevayler;
//#if MONITOR
import org.prevayler.foundation.monitor.Monitor;
//#endif
//#if MONITOR
import org.prevayler.foundation.monitor.SimpleMonitor;
//#endif
//#if REPLICATION
import org.prevayler.foundation.network.OldNetwork;
//#endif
//#if REPLICATION
import org.prevayler.foundation.network.OldNetworkImpl;
//#endif
import org.prevayler.foundation.serialization.JavaSerializer;
import org.prevayler.foundation.serialization.Serializer;
import org.prevayler.foundation.serialization.SkaringaSerializer;
import org.prevayler.foundation.serialization.XStreamSerializer;
import org.prevayler.implementation.PrevaylerDirectory;
import org.prevayler.implementation.PrevaylerImpl;
import org.prevayler.implementation.clock.MachineClock;
import org.prevayler.implementation.journal.Journal;
import org.prevayler.implementation.journal.PersistentJournal;
import org.prevayler.implementation.journal.TransientJournal;
import org.prevayler.implementation.publishing.CentralPublisher;
import org.prevayler.implementation.publishing.TransactionPublisher;
//#if CENSOR
import org.prevayler.implementation.publishing.censorship.LiberalTransactionCensor;
//#endif
//#if CENSOR
import org.prevayler.implementation.publishing.censorship.StrictTransactionCensor;
//#endif
//#if CENSOR
import org.prevayler.implementation.publishing.censorship.TransactionCensor;
//#endif
//#if REPLICATION
import org.prevayler.implementation.replication.ClientPublisher;
//#endif
//#if REPLICATION
import org.prevayler.implementation.replication.ServerListener;
//#endif
//#if SNAPSHOT
import org.prevayler.implementation.snapshot.GenericSnapshotManager;
//#endif
//#if SNAPSHOT
import org.prevayler.implementation.snapshot.NullSnapshotManager;
//#endif
import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
/** 
 * Provides easy access to all Prevayler configurations and implementations
 * available in this distribution. Static methods are also provided as
 * short-cuts for the most common configurations. <br>
 * By default, the Prevayler instances created by this class will write their
 * Transactions to .journal files before executing them. The
 * FileDescriptor.sync() method is called to make sure the Java file
 * write-buffers have been written to the operating system. Many operating
 * systems, including most recent versions of Linux and Windows, allow the
 * hard-drive's write-cache to be disabled. This guarantees no executed
 * Transaction will be lost in the event of a power shortage, for example. <br>
 * Also by default, the Prevayler instances created by this class will filter
 * out all Transactions that would throw a RuntimeException or Error if executed
 * on the Prevalent System. This requires enough RAM to hold another copy of the
 * prevalent system.
 * @see Prevayler
 */
public class PrevaylerFactory {
  private Object _prevalentSystem;
  private Clock _clock;
  private boolean _transactionFiltering=true;
//#if SNAPSHOT
  private boolean _transientMode;
//#endif
  private String _prevalenceDirectory;
//#if SNAPSHOT
  private NullSnapshotManager _nullSnapshotManager;
//#endif
  private long _journalSizeThreshold;
  private long _journalAgeThreshold;
//#if REPLICATION
  private OldNetwork _network;
//#endif
//#if REPLICATION
  private int _serverPort=-1;
//#endif
//#if REPLICATION
  private String _remoteServerIpAddress;
//#endif
//#if REPLICATION
  private int _remoteServerPort;
//#endif
//#if REPLICATION
  public static final int DEFAULT_REPLICATION_PORT=8756;
//#endif
//#if MONITOR
  private Monitor _monitor;
//#endif
  private Serializer _journalSerializer;
  private String _journalSuffix;
  private Map _snapshotSerializers=new HashMap();
  private String _primarySnapshotSuffix;
  /** 
 * Creates a Prevayler that will use a directory called "PrevalenceBase"
 * under the current directory to read and write its .snapshot and .journal
 * files.
 * @param newPrevalentSystemThe newly started, "empty" prevalent system that will be used
 * as a starting point for every system startup, until the first
 * snapshot is taken.
 */
  public static Prevayler createPrevayler(  Serializable newPrevalentSystem) throws IOException, ClassNotFoundException {
    return createPrevayler(newPrevalentSystem,"PrevalenceBase");
  }
  /** 
 * Creates a Prevayler that will use the given prevalenceBase directory to
 * read and write its .snapshot and .journal files.
 * @param newPrevalentSystemThe newly started, "empty" prevalent system that will be used
 * as a starting point for every system startup, until the first
 * snapshot is taken.
 * @param prevalenceBaseThe directory where the .snapshot files and .journal files
 * will be read and written.
 */
  public static Prevayler createPrevayler(  Serializable newPrevalentSystem,  String prevalenceBase) throws IOException, ClassNotFoundException {
    PrevaylerFactory factory=new PrevaylerFactory();
    factory.configurePrevalentSystem(newPrevalentSystem);
    factory.configurePrevalenceDirectory(prevalenceBase);
    return factory.create();
  }
  /** 
 * Creates a Prevayler that will execute Transactions WITHOUT writing them
 * to disk. This is useful for running automated tests or demos MUCH faster
 * than with a regular Prevayler.
 * Attempts to take snapshots on this transient Prevayler will throw an
 * IOException.
 * @param newPrevalentSystemThe newly started, "empty" prevalent system.
 * @see #createCheckpointPrevayler(Serializable newPrevalentSystem,String snapshotDirectory)
 */
  public static Prevayler createTransientPrevayler(  Serializable newPrevalentSystem){
    PrevaylerFactory factory=new PrevaylerFactory();
    factory.configurePrevalentSystem(newPrevalentSystem);
//#if SNAPSHOT
    factory.configureNullSnapshotManager(new NullSnapshotManager(newPrevalentSystem,"Transient Prevaylers are unable to take snapshots."));
//#endif
//#if SNAPSHOT
    factory.configureTransientMode(true);
//#endif
    try {
      return factory.create();
    }
 catch (    Exception e) {
      e.printStackTrace();
      return null;
    }
  }
  /** 
 * @deprecated Use createCheckpointPrevayler() instead of this method.
 * Deprecated since Prevayler2.00.001.
 */
  public static Prevayler createTransientPrevayler(  Serializable newPrevalentSystem,  String snapshotDirectory){
    return createCheckpointPrevayler(newPrevalentSystem,snapshotDirectory);
  }
  /** 
 * Creates a Prevayler that will execute Transactions WITHOUT writing them
 * to disk. Snapshots will work as "checkpoints" for the system, therefore.
 * This is useful for stand-alone applications that have a "Save" button,
 * for example.
 * @param newPrevalentSystemThe newly started, "empty" prevalent system that will be used
 * as a starting point for every system startup, until the first
 * snapshot is taken.
 * @param snapshotDirectoryThe directory where the .snapshot files will be read and
 * written.
 */
  public static Prevayler createCheckpointPrevayler(  Serializable newPrevalentSystem,  String snapshotDirectory){
    PrevaylerFactory factory=new PrevaylerFactory();
    factory.configurePrevalentSystem(newPrevalentSystem);
    factory.configurePrevalenceDirectory(snapshotDirectory);
//#if SNAPSHOT
    factory.configureTransientMode(true);
//#endif
    try {
      return factory.create();
    }
 catch (    Exception e) {
      e.printStackTrace();
      return null;
    }
  }
  private Clock clock(){
    return _clock != null ? _clock : new MachineClock();
  }
//#if MONITOR
  /** 
 * Assigns a monitor object to receive notifications from Prevayler. This is
 * useful for logging or sending eMails to system administrators, for
 * example. If this method is not called or if null is passed as a
 * parameter, a SimpleMonitor will be used to log notification on
 * System.err.
 * @param monitorthe Monitor implementation to use.
 * @see org.prevayler.foundation.monitor.SimpleMonitor
 */
  public void configureMonitor(  Monitor monitor){
    _monitor=monitor;
  }
//#endif
//#if SNAPSHOT
  /** 
 * Determines whether the Prevayler created by this factory should be
 * transient (transientMode = true) or persistent (transientMode = false).
 * Default is persistent. A transient Prevayler will execute its
 * Transactions WITHOUT writing them to disk. This is useful for stand-alone
 * applications which have a "Save" button, for example, or for running
 * automated tests MUCH faster than with a persistent Prevayler.
 */
  public void configureTransientMode(  boolean transientMode){
    _transientMode=transientMode;
  }
//#endif
  /** 
 * Configures the Clock that will be used by the created Prevayler. The
 * Clock interface can be implemented by the application if it requires
 * Prevayler to use a special time source other than the machine clock
 * (default).
 */
  public void configureClock(  Clock clock){
    _clock=clock;
  }
  /** 
 * Configures the directory where the created Prevayler will read and write
 * its .journal and .snapshot files. The default is a directory called
 * "PrevalenceBase" under the current directory.
 * @param prevalenceDirectoryWill be ignored for the .snapshot files if a SnapshotManager
 * is configured.
 */
  public void configurePrevalenceDirectory(  String prevalenceDirectory){
    _prevalenceDirectory=prevalenceDirectory;
  }
  /** 
 * Configures the prevalent system that will be used by the Prevayler
 * created by this factory.
 * @param newPrevalentSystemIf the default Serializer is used, this prevalentSystem must
 * be Serializable. If another Serializer is used, this
 * prevalentSystem must be compatible with it.
 * @see #configureSnapshotSerializer(String,Serializer)
 */
  public void configurePrevalentSystem(  Object newPrevalentSystem){
    _prevalentSystem=newPrevalentSystem;
  }
//#if REPLICATION
  /** 
 * Reserved for future implementation.
 */
  public void configureReplicationClient(  String remoteServerIpAddress,  int remoteServerPort){
    _remoteServerIpAddress=remoteServerIpAddress;
    _remoteServerPort=remoteServerPort;
  }
//#endif
//#if REPLICATION
  /** 
 * Reserved for future implementation.
 */
  public void configureReplicationServer(  int port){
    _serverPort=port;
  }
//#endif
//#if SNAPSHOT
  private void configureNullSnapshotManager(  NullSnapshotManager snapshotManager){
    _nullSnapshotManager=snapshotManager;
  }
//#endif
  /** 
 * Determines whether the Prevayler created by this factory should filter
 * out all Transactions that would throw a RuntimeException or Error if
 * executed on the Prevalent System (default is true). This requires enough
 * RAM to hold another copy of the prevalent system.
 */
  public void configureTransactionFiltering(  boolean transactionFiltering){
    _transactionFiltering=transactionFiltering;
  }
  /** 
 * Configures the size (in bytes) of the journal file. When the current
 * journal exceeds this size, a new journal is created.
 */
  public void configureJournalFileSizeThreshold(  long sizeInBytes){
    _journalSizeThreshold=sizeInBytes;
  }
  /** 
 * Sets the age (in milliseconds) of the journal file. When the current
 * journal expires, a new journal is created.
 */
  public void configureJournalFileAgeThreshold(  long ageInMilliseconds){
    _journalAgeThreshold=ageInMilliseconds;
  }
  public void configureJournalSerializer(  JavaSerializer serializer){
    configureJournalSerializer("journal",serializer);
  }
  public void configureJournalSerializer(  XStreamSerializer serializer){
    configureJournalSerializer("xstreamjournal",serializer);
  }
  public void configureJournalSerializer(  SkaringaSerializer serializer){
    configureJournalSerializer("skaringajournal",serializer);
  }
  /** 
 * Configures the transaction journal Serializer to be used by the Prevayler
 * created by this factory. Only one Serializer is supported at a time. If
 * you want to change the Serializer of a system in production, you will
 * have to take a snapshot first because the journal files written by the
 * previous Serializer will not be read.
 */
  public void configureJournalSerializer(  String suffix,  Serializer serializer){
    PrevaylerDirectory.checkValidJournalSuffix(suffix);
    if (_journalSerializer != null) {
      throw new IllegalStateException("Read the javadoc to this method.");
    }
    _journalSerializer=serializer;
    _journalSuffix=suffix;
  }
//#if REPLICATION
  public void configureNetwork(  OldNetwork network){
    _network=network;
  }
//#endif
//#if SNAPSHOT
  public void configureSnapshotSerializer(  JavaSerializer serializer){
    configureSnapshotSerializer("snapshot",serializer);
  }
//#endif
//#if SNAPSHOT
  public void configureSnapshotSerializer(  XStreamSerializer serializer){
    configureSnapshotSerializer("xstreamsnapshot",serializer);
  }
//#endif
//#if SNAPSHOT
  public void configureSnapshotSerializer(  SkaringaSerializer serializer){
    configureSnapshotSerializer("skaringasnapshot",serializer);
  }
//#endif
//#if SNAPSHOT
  /** 
 * Configure a serialization strategy for snapshots. This may be called any
 * number of times with different suffixes to configure different strategies
 * for reading existing snapshots. The first call to this method establishes
 * the <i>primary</i> strategy, which will be used for writing snapshots as
 * well as for deep-copying the prevalent system whenever necessary.
 */
  public void configureSnapshotSerializer(  String suffix,  Serializer serializer){
    PrevaylerDirectory.checkValidSnapshotSuffix(suffix);
    _snapshotSerializers.put(suffix,serializer);
    if (_primarySnapshotSuffix == null) {
      _primarySnapshotSuffix=suffix;
    }
  }
//#endif
  /** 
 * Returns a Prevayler created according to what was defined by calls to the
 * configuration methods above.
 * @throws IOExceptionIf there is trouble creating the Prevalence Base directory or
 * reading a .journal or .snapshot file.
 * @throws ClassNotFoundExceptionIf a class of a serialized Object is not found when reading a
 * .journal or .snapshot file.
 */
  public Prevayler create() throws IOException, ClassNotFoundException {
//#if SNAPSHOT
    GenericSnapshotManager snapshotManager=snapshotManager();
//#endif
    TransactionPublisher publisher;
//#if SNAPSHOT
    if (true) 
    	publisher=publisher(snapshotManager);
    else     
//#endif
    	publisher=publisher();
//#if REPLICATION
    if (_serverPort != -1) {
//#if REPLICATION
    new ServerListener(publisher,network(),_serverPort);
    }
//#endif
//#endif
//#if SNAPSHOT
    if (true) 
    	return new PrevaylerImpl(snapshotManager,publisher,journalSerializer());
    else     
//#endif
	 return new PrevaylerImpl(publisher,journalSerializer());
  }
  private String prevalenceDirectory(){
    return _prevalenceDirectory != null ? _prevalenceDirectory : "Prevalence";
  }
  private Object prevalentSystem(){
    if (_prevalentSystem == null)     throw new IllegalStateException("The prevalent system must be configured.");
    return _prevalentSystem;
  }
  
//#if SNAPSHOT
  private TransactionPublisher publisher(
//#if SNAPSHOT
  GenericSnapshotManager snapshotManager
//#endif
) throws IOException {
//#if REPLICATION
    if (_remoteServerIpAddress != null) 
    	return new ClientPublisher(network(),_remoteServerIpAddress,_remoteServerPort);
//#endif
//#if CENSOR
    if (true) 
    	return new CentralPublisher(clock(),censor(snapshotManager),journal());
    else
//#endif
    	return new CentralPublisher(clock(),journal());
  }
//#endif
  
  private TransactionPublisher publisher() throws IOException {
//#if REPLICATION
    if (_remoteServerIpAddress != null) {
    return new ClientPublisher(network(),_remoteServerIpAddress,_remoteServerPort);
    }
//#endif
    return new CentralPublisher(clock(),journal());
  }
  
//#if CENSOR
//#if SNAPSHOT
  private TransactionCensor censor(  GenericSnapshotManager snapshotManager){
	  return _transactionFiltering ? (TransactionCensor)new StrictTransactionCensor(snapshotManager) : new LiberalTransactionCensor();
  }
//#endif
//#endif
  private Journal journal() throws IOException {
//#if SNAPSHOT
    if (_transientMode) {
      return (Journal)new TransientJournal();
    }
    else 
//#endif
    {
    	PrevaylerDirectory directory=new PrevaylerDirectory(prevalenceDirectory());
//#if SNAPSHOT
//#if MONITOR
    	if (true)
    		return new PersistentJournal(directory,_journalSizeThreshold,_journalAgeThreshold,journalSuffix(),monitor());
    	else       
//#endif
//#endif
    		return new PersistentJournal(directory,_journalSizeThreshold,_journalAgeThreshold,journalSuffix());
    }
  }
  private Serializer journalSerializer(){
    if (_journalSerializer != null)     return _journalSerializer;
    return new JavaSerializer();
  }
  private String journalSuffix(){
    return _journalSuffix != null ? _journalSuffix : "journal";
  }
//#if REPLICATION
  private OldNetwork network(){
    return _network != null ? _network : new OldNetworkImpl();
  }
//#endif
//#if SNAPSHOT
  private GenericSnapshotManager snapshotManager() throws ClassNotFoundException, IOException {
    if (_nullSnapshotManager != null)     return _nullSnapshotManager;
    PrevaylerDirectory directory=new PrevaylerDirectory(prevalenceDirectory());
    if (!_snapshotSerializers.isEmpty())     return new GenericSnapshotManager(_snapshotSerializers,_primarySnapshotSuffix,prevalentSystem(),directory,journalSerializer());
    String snapshotSuffix="snapshot";
    JavaSerializer snapshotSerializer=new JavaSerializer();
    return new GenericSnapshotManager(Collections.singletonMap(snapshotSuffix,snapshotSerializer),snapshotSuffix,prevalentSystem(),directory,journalSerializer());
  }
//#endif
//#if MONITOR
  private Monitor monitor(){
    return _monitor != null ? _monitor : new SimpleMonitor(System.err);
  }
//#endif
}
