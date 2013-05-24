package org.prevayler.tests;

import java.io.File;

import org.prevayler.Prevayler;
import org.prevayler.PrevaylerFactory;
//#if MONITOR
import org.prevayler.foundation.monitor.Log4jMonitor;
import org.prevayler.foundation.monitor.LoggingMonitor;
import org.prevayler.foundation.monitor.NullMonitor;
import org.prevayler.foundation.monitor.SimpleMonitor;
//#endif
import org.prevayler.tests.demos.demo1.NumberKeeper;
import org.prevayler.tests.demos.demo1.PrimeCalculator;
import org.prevayler.tests.demos.demo2.MainTransient;
import org.prevayler.tests.implementation.AppendingSystem;
import org.prevayler.tests.implementation.Appendix;



public class TestAll {

	private static final Class String = null;

	public static void runTest(int test) throws Exception {
		// running tests with the input
		switch (test) {
		case 1:
			test1();
			break;
		case 2:
			test2();
			break;
		case 3:
			test3();
			break;
		case 4:
			test4();
			break;
		case 5:
			test5();
			break;
		default:
			try {
				throw new Exception();
			} catch (Exception e) {
				System.err.println("Test " + test + " does not exist");
				// e.printStackTrace();
				System.exit(0);
			}
		}
	}
	
	//savingNumbersLikeDemo1Main
	public static void test1() {
		String baseName = "demo1";
		Prevayler prevayler;
		NumberKeeper numberKeeper = new NumberKeeper();
		try {
			prevayler = PrevaylerFactory.createPrevayler(numberKeeper, baseName);
			//#if SNAPSHOT
			PrimeCalculator pm = new PrimeCalculator(prevayler);
			pm.start();   
			//#endif
		} catch (Exception e) {
			e.printStackTrace();
		} 
		cleningDir(baseName);
		System.out.println("test1");

	}
	
	//transientPrevayler
	public static void test2(){
		//#if MONITOR
		SimpleMonitor sm = new SimpleMonitor();
		Prevayler prevayler = PrevaylerFactory.createTransientPrevayler(new AppendingSystem());
		prevayler.execute(new Appendix("a"));
		sm.notify(String, "notify_a");
		prevayler.execute(new Appendix("b"));
		sm.notify(String, "notify_b");
		prevayler.execute(new Appendix("c"));
		sm.notify(String, "notify_c");
		String result = ((AppendingSystem)prevayler.prevalentSystem()).value();
		sm.notify(String, result);
		if(result.equals("abc")){
			System.out.println("ok..");
		} else System.out.println("fail..");
		//#endif
		System.out.println("test3");
	}
	
	//testDemos2
	private static void test3() {
		try {
			MainTransient.main(new String[] {});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("test4");
	}

	//logging test
	private static void test4() {
		//#if MONITOR
		SimpleMonitor sm = new SimpleMonitor();
		sm.notify(String, "notify1");
		sm.notify(String, "notify2", new File("log.txt"));
		
		NullMonitor nullm = new NullMonitor();
		nullm.notify(String, "notify1");
		nullm.notify(String, "notify3", new File("log.txt"));
		
		LoggingMonitor lm = new Log4jMonitor();
		//#endif
		System.out.println("test5");
	}
			
	
	private static void test5() {
		//#if MONITOR
		SimpleMonitor sm = new SimpleMonitor();
		sm.notify(String, "notify1");
		sm.notify(String, "notify2", new File("log.txt"));
		
		NullMonitor nullm = new NullMonitor();
		nullm.notify(String, "notify1");
		nullm.notify(String, "notify3", new File("log.txt"));
		
		LoggingMonitor lm = new Log4jMonitor();
		//#endif
		System.out.println("test5");
	}
	
	private static void cleningDir(String baseName) {
		String home = System.getProperty("user.dir");
		File base = new File(home+"/"+baseName);
		String [] files = base.list();
		for(String fileName : files){
			(new File(base + "/" +fileName)).delete();
		}
		base.delete();
	}
	
}

