package nl.andrewlalis.erme.control.actions.edits;

import nl.andrewlalis.erme.control.actions.DiagramPanelAction;
import nl.andrewlalis.erme.model.MappingModel;
import nl.andrewlalis.erme.model.Relation;
import nl.andrewlalis.erme.view.DiagramPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class AddRelationAction extends DiagramPanelAction {
	public AddRelationAction(DiagramPanel diagramPanel) {
		super("Add Relation", diagramPanel);
		this.putValue(SHORT_DESCRIPTION, "Add a new relation to the diagram.");
		this.putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		DiagramPanel dp = getDiagramPanel();
		MappingModel model = dp.getModel();
		String name = JOptionPane.showInputDialog(
				dp,
				"Enter the name of the relation.",
				"Add Relation",
				JOptionPane.PLAIN_MESSAGE
		);
		if (name != null) {
			final boolean isFirstRelation = model.getRelations().isEmpty();
			Point p;
			if (model.getLastInteractionPoint() != null) {
				p = new Point(
						model.getLastInteractionPoint().x - dp.getPanningTranslation().x,
						model.getLastInteractionPoint().y - dp.getPanningTranslation().y
				);
			} else if (isFirstRelation) {
				p = new Point(100, 100);
			} else {
				Rectangle bounds = model.getRelationBounds();
				p = new Point(bounds.x + bounds.width / 2, bounds.y + bounds.height / 2);
			}
			Relation r = new Relation(model, p, name);
			model.addRelation(r);
			model.getSelectionModel().clearSelection();
			model.getSelectionModel().select(r);
			if (isFirstRelation) {
				model.normalizeRelationPositions();
				dp.centerModel();
				dp.repaint();
			}
		}
	}
}
