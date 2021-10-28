package nl.andrewlalis.erme.view;

import nl.andrewlalis.erme.control.actions.edits.AddAttributeAction;
import nl.andrewlalis.erme.control.actions.edits.AddRelationAction;
import nl.andrewlalis.erme.control.actions.edits.RemoveRelationAction;
import nl.andrewlalis.erme.model.Relation;

import javax.swing.*;
import java.util.List;

public class DiagramPopupMenu extends JPopupMenu {
	public DiagramPopupMenu(DiagramPanel diagramPanel) {
		List<Relation> selectedRelations = diagramPanel.getModel().getSelectedRelations();
		if (selectedRelations.size() == 0) {
			this.add(new AddRelationAction(diagramPanel));
		}
		if (selectedRelations.size() > 0) {
			this.add(new RemoveRelationAction(diagramPanel));
		}
		if (selectedRelations.size() == 1) {
			Relation relation = selectedRelations.get(0);
			this.add(new AddAttributeAction(diagramPanel));
			if (!relation.getAttributes().isEmpty()) {
				this.add(new RemoveRelationAction(diagramPanel));
			}
		}
	}
}
