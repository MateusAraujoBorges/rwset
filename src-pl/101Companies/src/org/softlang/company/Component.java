package org.softlang.company;

//#if Cut || Total
import org.softlang.visitor.ReturningVisitor;
import org.softlang.visitor.VoidVisitor;
//#endif

/**
 * The root class of the object model for companies.
 * All objects in a company have a name.
 * There is also general visitor support.
 */
public interface Component{
	
	String getName();
	void setName(String name);
	//#if Cut || Total
	void accept(VoidVisitor v);
	<R> R accept(ReturningVisitor<R> v);
	//#endif
}
