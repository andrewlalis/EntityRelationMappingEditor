package nl.andrewlalis.erme.control.actions.edits;

import nl.andrewlalis.erme.control.actions.DiagramPanelAction;
import nl.andrewlalis.erme.model.Attribute;
import nl.andrewlalis.erme.view.DiagramPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.List;

public class RemoveAttributeAction extends DiagramPanelAction {
	public RemoveAttributeAction(DiagramPanel diagramPanel) {
		super("Remove Attribute", diagramPanel);
		this.putValue(SHORT_DESCRIPTION, "Remove an attribute from a relation.");
		this.putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		List<Attribute> selectedAttributes = getDiagramPanel().getModel().getSelectionModel().getSelectedAttributes();
		if (selectedAttributes.isEmpty()) {
			JOptionPane.showMessageDialog(
					getDiagramPanel(),
					"At least one attribute must be selected to remove.",
					"Select Attributes to Remove Them",
					JOptionPane.WARNING_MESSAGE
			);
			return;
		}
		int choice = JOptionPane.showConfirmDialog(getDiagramPanel(), "Are you sure you want to remove these attributes?", "Confirm", JOptionPane.OK_CANCEL_OPTION);
		if (choice == JOptionPane.YES_OPTION) {
			for (Attribute a : selectedAttributes) {
				a.getRelation().removeAttribute(a);
			}
			getDiagramPanel().getModel().getSelectionModel().clearAttributes();
		}
	}
}
