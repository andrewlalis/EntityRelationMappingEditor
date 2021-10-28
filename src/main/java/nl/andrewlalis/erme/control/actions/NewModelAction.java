package nl.andrewlalis.erme.control.actions;

import nl.andrewlalis.erme.model.MappingModel;
import nl.andrewlalis.erme.view.DiagramPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class NewModelAction extends DiagramPanelAction {
	public NewModelAction(DiagramPanel diagramPanel) {
		super("New Model", diagramPanel);
		this.putValue(SHORT_DESCRIPTION, "Create a new model.");
		this.putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		getDiagramPanel().setModel(new MappingModel());
	}
}
