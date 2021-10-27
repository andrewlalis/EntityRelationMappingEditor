package nl.andrewlalis.erme.control.actions;

import lombok.Getter;
import lombok.Setter;
import nl.andrewlalis.erme.view.DiagramPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class VisualizeReferencesAction extends AbstractAction {
	private static VisualizeReferencesAction instance;

	public static VisualizeReferencesAction getInstance() {
		if (instance == null) {
			instance = new VisualizeReferencesAction();
		}
		return instance;
	}

	@Getter
	@Setter
	private boolean referenceVisualizationEnabled = false;

	@Setter
	private DiagramPanel diagramPanel;

	public VisualizeReferencesAction() {
		super("Toggle Reference Visualization");
		this.putValue(SHORT_DESCRIPTION, "Shows/hides visualization of the references between attributes.");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		referenceVisualizationEnabled = ((AbstractButton)e.getSource()).getModel().isSelected();
		diagramPanel.repaint();
	}
}
