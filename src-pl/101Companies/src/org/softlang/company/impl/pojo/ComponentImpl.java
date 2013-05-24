package org.softlang.company.impl.pojo;

import org.softlang.company.*;
//#if Cut || Total
import org.softlang.visitor.*;
//#endif
public abstract class ComponentImpl implements Component {
	private String name;
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	//#if Cut || Total
	public abstract void accept(VoidVisitor v);
	public abstract <R> R accept(ReturningVisitor<R> v);
	//#endif
}
