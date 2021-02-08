package nl.andrewlalis.erme.control.actions.edits;

import lombok.Setter;
import nl.andrewlalis.erme.model.MappingModel;
import nl.andrewlalis.erme.model.Relation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class AddRelationAction extends AbstractAction {
	private static AddRelationAction instance;

	public static AddRelationAction getInstance() {
		if (instance == null) {
			instance = new AddRelationAction();
		}
		return instance;
	}

	@Setter
	private MappingModel model;

	public AddRelationAction() {
		super("Add Relation");
		this.putValue(SHORT_DESCRIPTION, "Add a new relation to the diagram.");
		this.putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Component c = (Component) e.getSource();
		String name = JOptionPane.showInputDialog(
				c,
				"Enter the name of the relation.",
				"Add Relation",
				JOptionPane.PLAIN_MESSAGE
		);
		if (name != null) {
			this.model.addRelation(new Relation(this.model, new Point(0, 0), name));
		}
	}
}
