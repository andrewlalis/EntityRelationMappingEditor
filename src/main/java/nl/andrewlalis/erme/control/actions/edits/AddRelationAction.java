package nl.andrewlalis.erme.control.actions.edits;

import lombok.Setter;
import nl.andrewlalis.erme.control.actions.LocalAction;
import nl.andrewlalis.erme.model.MappingModel;
import nl.andrewlalis.erme.model.Relation;
import nl.andrewlalis.erme.view.DiagramPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class AddRelationAction extends LocalAction {
	private static AddRelationAction instance;
	public static AddRelationAction getInstance() {
		if (instance == null) {
			instance = new AddRelationAction();
		}
		return instance;
	}

	@Setter
	private MappingModel model;

	@Setter
	private DiagramPanel diagramPanel;

	public AddRelationAction() {
		super("Add Relation");
		this.putValue(SHORT_DESCRIPTION, "Add a new relation to the diagram.");
		this.putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String name = JOptionPane.showInputDialog(
				this.diagramPanel,
				"Enter the name of the relation.",
				"Add Relation",
				JOptionPane.PLAIN_MESSAGE
		);
		if (name != null) {
			final boolean isFirstRelation = this.model.getRelations().isEmpty();
			Point p;
			if (this.hasLocation()) {
				p = new Point(
						this.getLocation().x - this.diagramPanel.getPanningTranslation().x,
						this.getLocation().y - this.diagramPanel.getPanningTranslation().y
				);
			} else if (isFirstRelation) {
				p = new Point(100, 100);
			} else {
				Rectangle bounds = this.model.getRelationBounds();
				p = new Point(bounds.x + bounds.width / 2, bounds.y + bounds.height / 2);
			}
			Relation r = new Relation(this.model, p, name);
			this.model.getSelectedRelations().forEach(rl -> rl.setSelected(false));
			r.setSelected(true);
			this.model.addRelation(r);
			if (isFirstRelation) {
				this.model.normalizeRelationPositions();
				this.diagramPanel.centerModel();
				this.diagramPanel.repaint();
			}
		}
	}
}
