package nl.andrewlalis.erme.control.actions.edits;

import lombok.Setter;
import nl.andrewlalis.erme.model.Attribute;
import nl.andrewlalis.erme.model.AttributeType;
import nl.andrewlalis.erme.model.MappingModel;
import nl.andrewlalis.erme.model.Relation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class AddAttributeAction extends AbstractAction {
	private static AddAttributeAction instance;

	public static AddAttributeAction getInstance() {
		if (instance == null) {
			instance = new AddAttributeAction();
		}
		return instance;
	}

	@Setter
	private MappingModel model;

	public AddAttributeAction() {
		super("Add Attribute");
		this.putValue(SHORT_DESCRIPTION, "Add an attribute to the selected relation.");
		this.putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.CTRL_DOWN_MASK));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		List<Relation> selectedRelations = this.model.getSelectedRelations();
		if (selectedRelations.size() != 1) {
			JOptionPane.showMessageDialog(
					(Component) e.getSource(),
					"A single relation must be selected to add an attribute.",
					"Single Relation Required",
					JOptionPane.WARNING_MESSAGE
			);
			return;
		}
		Relation r = selectedRelations.get(0);
		Component c = (Component) e.getSource();
		String name = JOptionPane.showInputDialog(c, "Enter the name of the attribute.", "Attribute Name", JOptionPane.PLAIN_MESSAGE);
		Integer index = (Integer) JOptionPane.showInputDialog(
				c,
				"Select the index to insert this attribute at.",
				"Attribute Index",
				JOptionPane.PLAIN_MESSAGE,
				null,
				Stream.iterate(0, n -> n + 1).limit(r.getAttributes().size() + 1).toArray(),
				r.getAttributes().size()
		);
		AttributeType type = (AttributeType) JOptionPane.showInputDialog(
				c,
				"Select the type this attribute is.",
				"Attribute Type",
				JOptionPane.PLAIN_MESSAGE,
				null,
				AttributeType.values(),
				AttributeType.PLAIN
		);
		if (name != null && index != null && type != null) {
			r.addAttribute(new Attribute(r, type, name), index);
		}
	}
}
