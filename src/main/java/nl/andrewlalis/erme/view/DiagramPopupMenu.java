package nl.andrewlalis.erme.view;

import nl.andrewlalis.erme.control.actions.edits.AddAttributeAction;
import nl.andrewlalis.erme.control.actions.edits.AddRelationAction;
import nl.andrewlalis.erme.control.actions.edits.RemoveAttributeAction;
import nl.andrewlalis.erme.control.actions.edits.RemoveRelationAction;
import nl.andrewlalis.erme.model.Attribute;
import nl.andrewlalis.erme.model.Relation;

import javax.swing.*;
import java.util.List;

public class DiagramPopupMenu extends JPopupMenu {
	public DiagramPopupMenu(DiagramPanel diagramPanel) {
		List<Relation> selectedRelations = diagramPanel.getModel().getSelectionModel().getSelectedRelations();
		List<Attribute> selectedAttributes = diagramPanel.getModel().getSelectionModel().getSelectedAttributes();
		if (selectedRelations.isEmpty() && selectedAttributes.isEmpty()) {
			this.add(new AddRelationAction(diagramPanel));
		}
		if (selectedRelations.size() > 0 && selectedAttributes.isEmpty()) {
			this.add(new RemoveRelationAction(diagramPanel));
		}
		if (selectedRelations.size() == 1 && selectedAttributes.isEmpty()) {
			this.add(new AddAttributeAction(diagramPanel));
		}
		if (!selectedAttributes.isEmpty() && selectedRelations.isEmpty()) {
			this.add(new RemoveAttributeAction(diagramPanel));
		}
	}
}
