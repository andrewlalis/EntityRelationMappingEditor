package nl.andrewlalis.erme.control.actions.edits;

import lombok.Setter;
import nl.andrewlalis.erme.model.MappingModel;
import nl.andrewlalis.erme.model.Relation;
import nl.andrewlalis.erme.view.DiagramPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class RemoveRelationAction extends AbstractAction {
	private static RemoveRelationAction instance;

	public static RemoveRelationAction getInstance() {
		if (instance == null) {
			instance = new RemoveRelationAction();
		}
		return instance;
	}

	@Setter
	private MappingModel model;
	@Setter
	private DiagramPanel diagramPanel;

	public RemoveRelationAction() {
		super("Remove Relation");
		this.putValue(SHORT_DESCRIPTION, "Remove a relation from the diagram.");
		this.putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (this.model.getSelectedRelations().isEmpty()) {
			JOptionPane.showMessageDialog(
					this.diagramPanel,
					"No relations selected. Select at least one relation to remove.",
					"No Relations Selected",
					JOptionPane.WARNING_MESSAGE
			);
			return;
		}
		for (Relation r : this.model.getSelectedRelations()) {
			this.model.removeRelation(r);
		}
	}
}
