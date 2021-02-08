package nl.andrewlalis.erme.control.actions.edits;

import lombok.Setter;
import nl.andrewlalis.erme.model.*;

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
		if (name == null) return;
		Integer index = (Integer) JOptionPane.showInputDialog(
				c,
				"Select the index to insert this attribute at.",
				"Attribute Index",
				JOptionPane.PLAIN_MESSAGE,
				null,
				Stream.iterate(0, n -> n + 1).limit(r.getAttributes().size() + 1).toArray(),
				r.getAttributes().size()
		);
		if (index == null) return;
		AttributeType type = (AttributeType) JOptionPane.showInputDialog(
				c,
				"Select the type this attribute is.",
				"Attribute Type",
				JOptionPane.PLAIN_MESSAGE,
				null,
				AttributeType.values(),
				AttributeType.PLAIN
		);
		if (type == null) return;
		boolean shouldUseForeignKey = ((String) JOptionPane.showInputDialog(
				c,
				"Is this attribute a foreign key?",
				"Foreign Key",
				JOptionPane.PLAIN_MESSAGE,
				null,
				new String[]{"Yes", "No"},
				"No"
		)).equalsIgnoreCase("yes");
		if (shouldUseForeignKey) {
			if (this.model.getRelations().size() < 2) {
				JOptionPane.showMessageDialog(c, "There should be at least 2 relations present in the model.", "Not Enough Relations", JOptionPane.WARNING_MESSAGE);
				return;
			}
			Relation fkRelation = (Relation) JOptionPane.showInputDialog(
					c,
					"Select the relation that this foreign key references.",
					"Foreign Key Relation Reference",
					JOptionPane.PLAIN_MESSAGE,
					null,
					this.model.getRelations().toArray(new Relation[0]),
					this.model.getRelations().stream().findFirst().orElse(null)
			);
			if (fkRelation == null) return;
			List<Attribute> eligibleAttributes = fkRelation.getAttributes();
			if (eligibleAttributes.isEmpty()) {
				JOptionPane.showMessageDialog(c, "There are no referencable attributes in the selected relation.", "No Referencable Attributes", JOptionPane.WARNING_MESSAGE);
				return;
			}
			Attribute fkAttribute = (Attribute) JOptionPane.showInputDialog(
					c,
					"Select the attribute that this foreign key references.",
					"Foreign Key Attribute Reference",
					JOptionPane.PLAIN_MESSAGE,
					null,
					eligibleAttributes.toArray(new Attribute[0]),
					eligibleAttributes.get(0)
			);
			if (fkAttribute != null) {
				r.addAttribute(new ForeignKeyAttribute(r, type, name, fkAttribute), index);
			}
		} else {
			r.addAttribute(new Attribute(r, type, name), index);
		}
	}
}
