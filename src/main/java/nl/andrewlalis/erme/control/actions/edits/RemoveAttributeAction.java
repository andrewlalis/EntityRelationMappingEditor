package nl.andrewlalis.erme.control.actions.edits;

import lombok.Setter;
import nl.andrewlalis.erme.model.Attribute;
import nl.andrewlalis.erme.model.MappingModel;
import nl.andrewlalis.erme.model.Relation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.stream.Stream;

public class RemoveAttributeAction extends AbstractAction {
	private static RemoveAttributeAction instance;

	public static RemoveAttributeAction getInstance() {
		if (instance == null) {
			instance = new RemoveAttributeAction();
		}
		return instance;
	}

	@Setter
	private MappingModel model;

	public RemoveAttributeAction() {
		super("Remove Attribute");
		this.putValue(SHORT_DESCRIPTION, "Remove an attribute from a relation.");
		this.putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		List<Relation> selectedRelations = this.model.getSelectedRelations();
		if (selectedRelations.size() != 1 || selectedRelations.get(0).getAttributes().isEmpty()) {
			JOptionPane.showMessageDialog(
					(Component) e.getSource(),
					"A single relation with at least one attribute must be selected to remove an attribute.",
					"Single Relation With Attribute Required",
					JOptionPane.WARNING_MESSAGE
			);
			return;
		}
		Relation r = selectedRelations.get(0);
		Attribute attribute = (Attribute) JOptionPane.showInputDialog(
				(Component) e.getSource(),
				"Select the index to insert this attribute at.",
				"Attribute Index",
				JOptionPane.PLAIN_MESSAGE,
				null,
				r.getAttributes().toArray(new Attribute[0]),
				r.getAttributes().get(0)
		);
		if (attribute != null) {
			r.removeAttribute(attribute);
		}
	}
}
