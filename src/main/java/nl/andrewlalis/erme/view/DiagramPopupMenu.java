package nl.andrewlalis.erme.view;

import nl.andrewlalis.erme.control.actions.edits.AddAttributeAction;
import nl.andrewlalis.erme.control.actions.edits.AddRelationAction;
import nl.andrewlalis.erme.control.actions.edits.RemoveAttributeAction;
import nl.andrewlalis.erme.control.actions.edits.RemoveRelationAction;
import nl.andrewlalis.erme.model.MappingModel;
import nl.andrewlalis.erme.model.Relation;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.util.List;

public class DiagramPopupMenu extends JPopupMenu {
	public DiagramPopupMenu(MappingModel model, MouseEvent e) {
		List<Relation> selectedRelations = model.getSelectedRelations();
		AddRelationAction.getInstance().setLocation(e.getPoint());
		if (selectedRelations.size() == 0) {
			this.add(AddRelationAction.getInstance());
		}
		if (selectedRelations.size() > 0) {
			this.add(RemoveRelationAction.getInstance());
		}
		if (selectedRelations.size() == 1) {
			this.add(AddAttributeAction.getInstance());
			this.add(RemoveAttributeAction.getInstance());
		}
	}
}
