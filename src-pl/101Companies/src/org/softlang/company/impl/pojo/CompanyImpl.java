package org.softlang.company.impl.pojo;

import java.util.LinkedList;
import java.util.List;

import org.softlang.company.Company;
import org.softlang.company.Department;
import org.softlang.company.Subunit;
//#if Cut || Total
import org.softlang.visitor.ReturningVisitor;
import org.softlang.visitor.VoidVisitor;
//#endif
public class CompanyImpl extends ContainerImpl implements Company{ 

	//#if GUI
	private List<Department> depts;
	public CompanyImpl() {
		depts = new LinkedList<Department>();
	}
	public boolean add(Department department) {
		return depts.add(department);
	}
	public List<Department> getDepts() {
		return depts;
	}
	public void setDepts(List<Department> depts) {
		this.depts = depts;
	}
	/**
	 * This method returns the name for the tree-view.
	 */
	@Override
	public String toString(){
		return this.getName();
	}
	//#endif
	
	//#if Cut || Total
	public void accept(VoidVisitor v) {
		v.visit(this);
	}
	public <R> R accept(ReturningVisitor<R> v) {
		return v.visit(this);
	}
	//#endif
	
	/**
	 * Enforce the constraint a company can only aggregate departments
	 */
	public boolean add(Subunit u) {
		if (!(u instanceof Department)){
			throw new IllegalArgumentException();
		}
		//#if GUI
		depts.add((Department) u);
		//#endif
		return super.add(u);
	}
}
