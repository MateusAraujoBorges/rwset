package org.softlang.gui;

import org.softlang.company.factory.BeanFactory;
import org.softlang.company.factory.PojoFactory;
import org.softlang.swing.controller.Controller;
import org.softlang.swing.model.Model;
import org.softlang.swing.view.MainView;

/**
 * This class initializes the MVC-structure of the project. Based on the very
 * simple data structure of the project, there is only one instance of the
 * model.
 * 
 * @author Tobias Zimmer
 */
public class Interaction {
	public static void main(String[] args) {
		Model model = new Model(new BeanFactory());
		MainView view = new MainView(model);
		Controller controller = new Controller(model, view);
		
		controller.start();
	}
}