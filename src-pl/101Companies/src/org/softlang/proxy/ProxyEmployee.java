package org.softlang.proxy;

import org.softlang.company.*;
//#if Cut || Total
import org.softlang.visitor.*;
//#endif

/**
 * A proxy for employees to enforce access control policy for salaries.
 */
/* package */ class ProxyEmployee implements Employee {

	private AccessControl context;
	private Employee subject;
	
	/* package */ ProxyEmployee(AccessControl context, Employee subject) {
		this.context = context;
		this.subject = subject;
	}
	public String getName() {
		return subject.getName();
	}
	public void setName(String name) {
		subject.setName(name);
	}
	public String getAddress() {
		return subject.getAddress();
	}
	public void setAddress(String address) {
		subject.setAddress(address);
	}
	public double getSalary() {
		if (context.isReadable())
			return subject.getSalary();
		else
			throw new IllegalArgumentException("Receiver without read access for salary.");
	}
	public void setSalary(double salary) {
		if (context.isWritable())
			subject.setSalary(salary);
		else
			throw new IllegalArgumentException("Receiver without write access for salary.");
	}
	public boolean getManager() {
		return subject.getManager();
	}
	public void setManager(boolean manager) {
		subject.setManager(manager);
	}	
	
	//#if Cut || Total
	// Delegation is NOT appropriate here.
	public void accept(VoidVisitor v) {
		v.visit(this);
	}
	// Delegation is NOT appropriate here.
	public <R> R accept(ReturningVisitor<R> v) {
		return v.visit(this);
	}	
	//#endif
	
	
	
	//#if GUI
	/**
	 * This method returns the name for the tree-view.
	 */
	@Override
	public String toString(){
		String treeName = this.getName();
		if (getManager()) {
			return treeName + " (Manager)";
		}
		return treeName;
	}
	//#endif
}
