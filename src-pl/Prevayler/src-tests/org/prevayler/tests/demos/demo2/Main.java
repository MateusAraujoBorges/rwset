package org.prevayler.tests.demos.demo2;
import org.prevayler.Prevayler;
import org.prevayler.PrevaylerFactory;
import org.prevayler.tests.demos.demo2.business.Bank;
public class Main {
  public static void main(  String[] ignored) throws Exception {
    out("\nOne snapshot per day is more than enough for most applications" + "\n  because the journal recovery rate is in the order of" + "\n  6000 transactions per second. For demoing purposes, though, a"+ "\n  snapshot will be taken every 20 seconds...");
    Prevayler prevayler=PrevaylerFactory.createPrevayler(new Bank(),"demo2");
    startSnapshots(prevayler);
  }
  static void startSnapshots(  Prevayler prevayler) throws Exception {
    while (true) {
      Thread.sleep(2);//20
      prevayler.takeSnapshot();
    }
  }
  private static void out(  String message){
    System.out.println(message);
  }
}
