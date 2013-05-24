//#if REPLICATION
package org.prevayler.foundation.network;
import java.io.IOException;
/** 
 * Useful class comments should go here
 * $Revision: 1.2 $
 * $Date: 2005/03/02 06:04:18 $
 * $Author: peter_mxgroup $
 */
public interface StubbornNetwork {
  ObjectSocket newInstance(  String ipAddress,  int port) throws IOException ;
}
//#endif
