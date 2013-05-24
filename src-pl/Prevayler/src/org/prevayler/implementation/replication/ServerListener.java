//#if REPLICATION
package org.prevayler.implementation.replication;
//#if REPLICATION
import org.prevayler.foundation.network.OldNetwork;
//#endif
//#if REPLICATION
import org.prevayler.foundation.network.ObjectServerSocket;
//#endif
import org.prevayler.implementation.publishing.TransactionPublisher;
import java.io.IOException;
/** 
 * Reserved for future implementation.
 */
public class ServerListener extends Thread {
  private final TransactionPublisher _publisher;
  private final ObjectServerSocket _serverSocket;
  public ServerListener(  TransactionPublisher publisher,  OldNetwork network,  int port) throws IOException {
    _serverSocket=network.openObjectServerSocket(port);
    _publisher=publisher;
    setDaemon(true);
    start();
  }
  public void run(){
    try {
      while (true)       new ServerConnection(_publisher,_serverSocket.accept());
    }
 catch (    IOException iox) {
      iox.printStackTrace();
    }
  }
}
//#endif
