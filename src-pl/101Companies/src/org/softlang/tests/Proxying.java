package org.softlang.tests;

import org.softlang.company.*;
import org.softlang.company.factory.BeanFactory;
import org.softlang.company.factory.Factory;
import org.softlang.company.factory.PojoFactory;
//#if AccessControl && TotalReducer || AccessControl && CutWhatever
import org.softlang.features.*;
//#endif
//#if AccessControl
import org.softlang.proxy.*;
//#endif

import org.junit.*;

import static org.junit.Assert.*;

public class Proxying {

	//#if AccessControl && TotalReducer
	@Test
	public void testTotal() {
		Company sampleCompany = Basics.createSampleCompany(new PojoFactory());
		AccessControl ac = new AccessControl();
		ac.disableWriteAcccess();
		sampleCompany = ac.deploy(sampleCompany);
		TotalReducer reducer = new TotalReducer();
	    assertEquals(399747, reducer.reduce(sampleCompany), 0);
	}
	//#endif
	
	//#if AccessControl && TotalReducer
	@Test
	public void testEmployeeAccessControl(){
		AccessControl ac = new AccessControl();
		ac.disableWriteAcccess();
		ac.disableReadAcccess();
		Factory f = new PojoFactory();
	    Employee ralf = f.mkEmployee();
		ralf.setName("Ralf");
		ralf.setAddress("Koblenz");
		ralf.setSalary(1234);	
		Employee e = ac.deploy(ralf);
		e.setSalary(4321);	
		assertEquals(1234, e.getSalary(),0);
	}
	//#endif

	//#if AccessControl && TotalReducer
	@Test(expected=IllegalArgumentException.class)
	public void testTotalException() {
		Company sampleCompany = Basics.createSampleCompany(new PojoFactory());
		AccessControl ac = new AccessControl();
		ac.disableReadAcccess();
		sampleCompany = ac.deploy(sampleCompany);
		TotalReducer reducer = new TotalReducer();
	    reducer.reduce(sampleCompany);
	}
	//#endif
	
	//#if AccessControl && TotalReducer && CutWhatever
	@Test
	public void testCut() {
		Company sampleCompany = Basics.createSampleCompany(new PojoFactory());
		AccessControl ac = new AccessControl();
		sampleCompany = ac.deploy(sampleCompany);
		org.softlang.features.TotalReducer total = new org.softlang.features.TotalReducer();
		org.softlang.features.SimpleCut cut = new org.softlang.features.SimpleCut();
		double before = total.reduce(sampleCompany);
		cut.postorder(sampleCompany);
		double after = total.reduce(sampleCompany);
		assertEquals(before / 2.0d, after, 0);
	}
	//#endif

	//#if AccessControl && CutWhatever
	@Test(expected=IllegalArgumentException.class)
	public void testCutException() {
		Company sampleCompany = Basics.createSampleCompany(new PojoFactory());
		AccessControl ac = new AccessControl();
		ac.disableWriteAcccess();
		sampleCompany = ac.deploy(sampleCompany);
		SimpleCut cut = new SimpleCut();
		cut.postorder(sampleCompany);
	}
	//#endif
	
	/**
	 * If all class remains empty because variability. 
	 * We have at least one test in order to compile the class.
	 */
	 @Test
	 public void test() {
	 }
}
