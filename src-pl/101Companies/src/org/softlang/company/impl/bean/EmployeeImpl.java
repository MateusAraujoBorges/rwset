package org.softlang.company.impl.bean;

import org.softlang.company.*;

import org.softlang.visitor.*;


public class EmployeeImpl extends ComponentImpl implements Employee {
	
	private String name;
	private String address;
	private double salary;
	private boolean manager;
	
	//#if Precedence
	private double oldSalary;
	public double getOldSalary() {
		return oldSalary;
	}
	public void setOldSalary(double oldSalary) {
		this.oldSalary = oldSalary;
	}
	//#endif

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		//#if Logging || Precedence
		setChanged();
		notifyObservers("name");
		//#endif
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
		//#if Logging || Precedence
		setChanged();
		notifyObservers("address");
	    //#endif
	}

	public double getSalary() {
		return salary;
	}

	public void setSalary(double salary) {
		if(salary!=this.getSalary()){
			//#if Precedence
			this.oldSalary = this.salary;
			//#endif
			this.salary = salary;
			//#if Logging || Precedence
	 		setChanged();
	 		notifyObservers("salary");
		    //#endif
		}	
	}	
	
	//#if Precedence
	public void setSalaryWithOldSalary(){
		this.salary = this.oldSalary;
	}
	//#endif
	
	public boolean getManager() {
		return manager;
	}

	public void setManager(boolean manager) {
		this.manager = manager;
		//#if Logging || Precedence
		setChanged();
		notifyObservers("manager");
		//#endif
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
