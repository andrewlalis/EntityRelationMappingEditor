package nl.andrewlalis.erme.control.actions.edits;

import nl.andrewlalis.erme.control.actions.DiagramPanelAction;
import nl.andrewlalis.erme.model.MappingModel;
import nl.andrewlalis.erme.model.Relation;
import nl.andrewlalis.erme.view.DiagramPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.List;

public class RemoveRelationAction extends DiagramPanelAction {
	public RemoveRelationAction(DiagramPanel diagramPanel) {
		super("Remove Relation", diagramPanel);
		this.putValue(SHORT_DESCRIPTION, "Remove a relation from the diagram.");
		this.putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		MappingModel model = getDiagramPanel().getModel();
		List<Relation> selectedRelations = model.getSelectionModel().getSelectedRelations();
		if (selectedRelations.isEmpty()) {
			JOptionPane.showMessageDialog(
					getDiagramPanel(),
					"No relations selected. Select at least one relation to remove.",
					"No Relations Selected",
					JOptionPane.WARNING_MESSAGE
			);
			return;
		}
		int choice = JOptionPane.showConfirmDialog(getDiagramPanel(), "Are you sure you want to remove these relations?", "Confirm", JOptionPane.YES_NO_OPTION);
		if (choice == JOptionPane.YES_OPTION) {
			for (Relation r : selectedRelations) {
				model.removeRelation(r);
			}
			model.getSelectionModel().clearRelations();
		}
	}
}
