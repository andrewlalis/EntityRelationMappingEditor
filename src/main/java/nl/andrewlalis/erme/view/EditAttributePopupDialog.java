package nl.andrewlalis.erme.view;

import nl.andrewlalis.erme.model.Attribute;
import nl.andrewlalis.erme.model.AttributeType;
import nl.andrewlalis.erme.model.Relation;

import javax.swing.*;
import java.awt.*;

/**
 * A popup that's shown when creating or editing an attribute of a relation.
 */
public class EditAttributePopupDialog extends JDialog {
	private final Relation relation;
	private final Attribute attribute;

	private final JTextField nameField;
	private final JComboBox<AttributeType> attributeTypeComboBox;
	private final JSpinner orderSpinner;

	public EditAttributePopupDialog(Frame owner, Relation relation, Attribute attribute) {
		super(owner, "Edit Attribute", true);
		this.relation = relation;
		this.attribute = attribute;
		JPanel mainPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();
		gc.ipadx = 2;
		gc.ipady = 2;
		gc.gridx = 0;
		gc.gridy = 0;
		gc.anchor = GridBagConstraints.LINE_START;
		mainPanel.add(new JLabel("Name"), gc);
		gc.gridx = 1;
		gc.anchor = GridBagConstraints.LINE_END;
		this.nameField = new JTextField(20);
		mainPanel.add(nameField, gc);
		gc.gridx = 0;
		gc.gridy++;
		gc.anchor = GridBagConstraints.LINE_START;
		mainPanel.add(new JLabel("Type"), gc);
		gc.gridx = 1;
		gc.anchor = GridBagConstraints.LINE_END;
		this.attributeTypeComboBox = new JComboBox<>(AttributeType.values());
		mainPanel.add(attributeTypeComboBox, gc);
		gc.gridx = 0;
		gc.gridy++;
		gc.anchor = GridBagConstraints.LINE_START;
		mainPanel.add(new JLabel("Order"), gc);
		gc.gridx = 1;
		gc.anchor = GridBagConstraints.LINE_END;
		this.orderSpinner = new JSpinner(new SpinnerNumberModel(1, 1, relation.getAttributes().size() + 1, 1));
		mainPanel.add(orderSpinner, gc);

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JButton okayButton = new JButton("Okay");
		okayButton.addActionListener(e -> this.submit());
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(e -> this.dispose());
		buttonPanel.add(okayButton);
		buttonPanel.add(cancelButton);
		gc.gridx = 0;
		gc.gridy++;
		gc.gridwidth = 2;
		mainPanel.add(buttonPanel, gc);

		if (this.attribute != null) {
			this.nameField.setText(this.attribute.getName());
			this.attributeTypeComboBox.setSelectedItem(this.attribute.getType());
			this.orderSpinner.setValue(this.relation.getAttributes().indexOf(this.attribute));
		} else {
			this.orderSpinner.setValue(this.relation.getAttributes().size() + 1);
		}

		this.setContentPane(mainPanel);
		this.pack();
		this.setLocationRelativeTo(owner);
	}

	private void submit() {
		String name = this.nameField.getText().trim();
		AttributeType type = (AttributeType) this.attributeTypeComboBox.getSelectedItem();
		int order = (int) this.orderSpinner.getValue();
		if (name.isEmpty()) {
			JOptionPane.showMessageDialog(this, "The attribute must have a name.", "Missing Name", JOptionPane.WARNING_MESSAGE);
			return;
		}
		if (this.attribute != null) {
			this.attribute.setName(name);
			this.attribute.setType(type);
		} else {
			Attribute a = new Attribute(this.relation, type, name);
			this.relation.addAttribute(a, order - 1);
		}
		this.dispose();
	}
}
