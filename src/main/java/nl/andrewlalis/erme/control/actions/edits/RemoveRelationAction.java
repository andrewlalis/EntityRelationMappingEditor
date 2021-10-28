package nl.andrewlalis.erme.control.actions.edits;

import nl.andrewlalis.erme.control.actions.DiagramPanelAction;
import nl.andrewlalis.erme.model.MappingModel;
import nl.andrewlalis.erme.model.Relation;
import nl.andrewlalis.erme.view.DiagramPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class RemoveRelationAction extends DiagramPanelAction {
	public RemoveRelationAction(DiagramPanel diagramPanel) {
		super("Remove Relation", diagramPanel);
		this.putValue(SHORT_DESCRIPTION, "Remove a relation from the diagram.");
		this.putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		MappingModel model = getDiagramPanel().getModel();
		if (model.getSelectedRelations().isEmpty()) {
			JOptionPane.showMessageDialog(
					getDiagramPanel(),
					"No relations selected. Select at least one relation to remove.",
					"No Relations Selected",
					JOptionPane.WARNING_MESSAGE
			);
			return;
		}
		for (Relation r : model.getSelectedRelations()) {
			model.removeRelation(r);
		}
	}
}
