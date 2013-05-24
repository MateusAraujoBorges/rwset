package org.softlang.proxy;

import java.util.LinkedList;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import org.softlang.company.*;
//#if Cut || Total
import org.softlang.visitor.*;
//#endif

/**
 * A proxy for departments to enforce access control policy for salaries.
 */
class ProxyDepartment implements Department {

	private AccessControl context;
	private Department subject;
	
	//#if GUI
	private List<Department> subdepts;
	private List<Employee> employees;
	private DefaultMutableTreeNode treeNode;
	//#endif
	
	ProxyDepartment(AccessControl context, Department subject) {
		this.context = context;
		this.subject = subject;
		//#if GUI
		subdepts = new LinkedList<Department>();
		employees = new LinkedList<Employee>();
		//#endif
	}

	public String getName() {
		return subject.getName();
	}

	public void setName(String name) {
		subject.setName(name);
	}
	
	public Iterable<? extends Subunit> subunits() {
		return subject.subunits();
	}

	public boolean add(Subunit u) {
		//#if GUI
		if(u instanceof Department){
			 this.subdepts.add((Department) u);
		}else if(u instanceof Employee){
			 this.employees.add((Employee) u);
		}
		//#endif
		u = context.deploy(u);
		return subject.add(u);
	}
	
	public boolean remove(Subunit u) {
		return subject.remove(u);
	}
	public Employee getManager() {
		return subject.getManager();
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
	public void setSubdepts(List<Department> subdepts) {
		this.subdepts = subdepts;
	}
	public void setEmployees(List<Employee> employees) {
		this.employees = employees;
	}
	public List<Department> getSubdepts() {
		return subdepts;
	}
	public List<Employee> getEmployees() {
		return employees;
	}
	public void setTreeNode(DefaultMutableTreeNode treeNode) {
		this.treeNode = treeNode;
	}
	public DefaultMutableTreeNode getTreeNode() {
		return treeNode;
	}
	/**
	 * This method returns the name for the tree-view.
	 */
	@Override
	public String toString() {
		String treeName = this.getName();
		return treeName;
	}
	//#endif
}
