package org.softlang.company.impl.bean;

import org.softlang.company.Company;
import org.softlang.visitor.ReturningVisitor;
import org.softlang.visitor.VoidVisitor;


public class CompanyImpl extends ContainerImpl implements Company {
	
	public void accept(VoidVisitor v) {
		//#if Cut || Total
		v.visit(this);
		//#endif
	}
	public <R> R accept(ReturningVisitor<R> v) {
		//#if Cut || Total
		return v.visit(this);
		//#endif
	}
	
	//#if GUI
	public CompanyImpl(){
		super();
	}
	//#endif
}
