//#if REPLICATION
package org.prevayler.foundation.network;
import java.io.IOException;
/** 
 * Useful class comments should go here
 * $Revision: 1.1 $
 * $Date: 2005/03/02 06:04:17 $
 * $Author: peter_mxgroup $
 */
public interface NetworkReceiverFactory {
  ObjectReceiver newReceiver(  Service service,  ObjectSocket socket) throws IOException ;
}
//#endif
