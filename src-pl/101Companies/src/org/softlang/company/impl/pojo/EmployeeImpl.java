package org.softlang.company.impl.pojo;

import org.softlang.company.*;
import org.softlang.visitor.*;


public class EmployeeImpl extends ComponentImpl implements Employee {
	
	private String address;
	private double salary;
	private boolean manager = false;

	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public double getSalary() {
		return salary;
	}
	public void setSalary(double salary) {
		this.salary = salary;
	}
	public boolean getManager() {
		return manager;
	}
	public void setManager(boolean manager) {
		this.manager = manager;
	}
	
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
	/**
	 * This method returns the name for the tree-view.
	 */
	@Override
	public String toString(){
		String treeName = this.getName();
		if (manager) {
			return treeName + " (Manager)";
		}
		return treeName;
	}
	//#endif
}
