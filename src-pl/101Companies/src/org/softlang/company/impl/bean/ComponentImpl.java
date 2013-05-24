package org.softlang.company.impl.bean;

import org.softlang.company.*;
//#if Cut || Total
import org.softlang.visitor.*;
//#endif
import java.util.Observable;

/**
 * Observability and a parent axis are added on top of the Component interface.
 */
public abstract class ComponentImpl extends Observable implements Component {

	private ComponentImpl parent;
	private String name;
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
	public ComponentImpl getParent() { return parent; }
	/* package */ void setParent(ComponentImpl parent) { this.parent = parent; }
	
	//#if Cut || Total
	public abstract void accept(VoidVisitor v);
	public abstract <R> R accept(ReturningVisitor<R> v);
	//#endif
}
