package nl.andrewlalis.erme.control.actions;

import nl.andrewlalis.erme.view.DiagramPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class VisualizeReferencesAction extends DiagramPanelAction {
	public VisualizeReferencesAction(DiagramPanel diagramPanel) {
		super("Toggle Reference Visualization", diagramPanel);
		this.putValue(SHORT_DESCRIPTION, "Shows/hides visualization of the references between attributes.");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		getDiagramPanel().getModel().setReferenceVisualizationEnabled(((AbstractButton)e.getSource()).getModel().isSelected());
		getDiagramPanel().repaint();
	}
}
