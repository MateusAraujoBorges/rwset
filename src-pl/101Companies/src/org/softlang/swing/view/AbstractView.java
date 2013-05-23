package org.softlang.swing.view;

import java.awt.event.ActionListener;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.softlang.swing.model.Model;

/**
 * This abstract class provides the main components for all possible views:
 * name, total and cut.
 * 
 * @author Tobias Zimmer
 */
public abstract class AbstractView extends JPanel {
	
	/** automatically generated serialVersionUID */
	private static final long serialVersionUID = -3528714296290843302L;
	
	protected Model model;
	protected JTextField name;
	protected JTextField total;
	//#if Cut
	protected JButton cut;
	//#endif
	
	/**
	 * Constructor
	 * 
	 * @param model
	 */
	public AbstractView(Model model) {
		this.model = model;
		name = new JTextField();
		//#if Total
		total = new JTextField();
		//#endif
		//#if Cut
		cut = new JButton("cut");
		//#endif
	}
	
	//#if Cut
	/**
	 * This method adds the listener for the cut button of the current view.
	 * 
	 * @param cut listener
	 */
	public void addCutListener(ActionListener listener) {
		cut.addActionListener(listener);
	}
	//#endif
	
	
	//#if Cut && Total
	public void refresh() {
		//This method refreshs the total value after a cut.
		total.setText(model.getTotal());
	}
	//#endif
	
	
	/**
	 * This method adds the listener for the name field of the current view.
	 * 
	 * @param name change listener
	 */
	public void addNameListener(KeyListener listener) {
		name.addKeyListener(listener);
	}
}
