package nl.andrewlalis.erme.control.actions.edits;

import nl.andrewlalis.erme.control.actions.DiagramPanelAction;
import nl.andrewlalis.erme.model.*;
import nl.andrewlalis.erme.view.DiagramPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.stream.Stream;

public class AddAttributeAction extends DiagramPanelAction {
	public AddAttributeAction(DiagramPanel diagramPanel) {
		super("Add Attribute", diagramPanel);
		this.putValue(SHORT_DESCRIPTION, "Add an attribute to the selected relation.");
		this.putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.CTRL_DOWN_MASK));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		DiagramPanel dp = getDiagramPanel();
		MappingModel model = dp.getModel();
		List<Relation> selectedRelations = model.getSelectedRelations();
		if (selectedRelations.size() != 1) {
			JOptionPane.showMessageDialog(
					dp,
					"A single relation must be selected to add an attribute.",
					"Single Relation Required",
					JOptionPane.WARNING_MESSAGE
			);
			return;
		}
		Relation r = selectedRelations.get(0);
		Attribute createdAttribute;
		String name = JOptionPane.showInputDialog(dp, "Enter the name of the attribute.", "Attribute Name", JOptionPane.PLAIN_MESSAGE);
		if (name == null) return;
		Integer index = (Integer) JOptionPane.showInputDialog(
				dp,
				"Select the index to insert this attribute at.",
				"Attribute Index",
				JOptionPane.PLAIN_MESSAGE,
				null,
				Stream.iterate(0, n -> n + 1).limit(r.getAttributes().size() + 1).toArray(),
				r.getAttributes().size()
		);
		if (index == null) return;
		AttributeType type = (AttributeType) JOptionPane.showInputDialog(
				dp,
				"Select the type this attribute is.",
				"Attribute Type",
				JOptionPane.PLAIN_MESSAGE,
				null,
				AttributeType.values(),
				AttributeType.PLAIN
		);
		if (type == null) return;
		boolean shouldUseForeignKey = ((String) JOptionPane.showInputDialog(
				dp,
				"Is this attribute a foreign key?",
				"Foreign Key",
				JOptionPane.PLAIN_MESSAGE,
				null,
				new String[]{"Yes", "No"},
				"No"
		)).equalsIgnoreCase("yes");
		if (shouldUseForeignKey) {
			if (model.getRelations().size() < 2) {
				JOptionPane.showMessageDialog(dp, "There should be at least 2 relations present in the model.", "Not Enough Relations", JOptionPane.WARNING_MESSAGE);
				return;
			}
			Relation fkRelation = (Relation) JOptionPane.showInputDialog(
					dp,
					"Select the relation that this foreign key references.",
					"Foreign Key Relation Reference",
					JOptionPane.PLAIN_MESSAGE,
					null,
					model.getRelations().toArray(new Relation[0]),
					model.getRelations().stream().findFirst().orElse(null)
			);
			if (fkRelation == null) return;
			List<Attribute> eligibleAttributes = fkRelation.getAttributes();
			if (eligibleAttributes.isEmpty()) {
				JOptionPane.showMessageDialog(dp, "There are no referencable attributes in the selected relation.", "No Referencable Attributes", JOptionPane.WARNING_MESSAGE);
				return;
			}
			Attribute fkAttribute = (Attribute) JOptionPane.showInputDialog(
					dp,
					"Select the attribute that this foreign key references.",
					"Foreign Key Attribute Reference",
					JOptionPane.PLAIN_MESSAGE,
					null,
					eligibleAttributes.toArray(new Attribute[0]),
					eligibleAttributes.get(0)
			);
			if (fkAttribute == null) return;
			createdAttribute = new ForeignKeyAttribute(r, type, name, fkAttribute);
		} else {
			createdAttribute = new Attribute(r, type, name);
		}
		r.addAttribute(createdAttribute, index);
	}
}
