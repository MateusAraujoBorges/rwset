package org.softlang.command;

import org.softlang.company.Company;
//#if Cut || Total
import org.softlang.template.Walker;
import org.softlang.company.Employee;
//#endif

/**
 * Construct a salary cut for all salaries in a company
 */
public class CutCompany extends Batch {

	public CutCompany(Company c) {
//#if Cut || Total
		new Walker() {
			public void visit(Employee e) {
				add(new CutEmployee(e));
			}
		}.postorder(c);
//#endif
	}
}
