package nl.andrewlalis.erme.control.actions;

import lombok.Setter;
import nl.andrewlalis.erme.model.MappingModel;
import nl.andrewlalis.erme.view.DiagramPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class NewModelAction extends AbstractAction {
	private static NewModelAction instance;

	public static NewModelAction getInstance() {
		if (instance == null) {
			instance = new NewModelAction();
		}
		return instance;
	}

	@Setter
	private DiagramPanel diagramPanel;

	public NewModelAction() {
		super("New Model");
		this.putValue(SHORT_DESCRIPTION, "Create a new model.");
		this.putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		this.diagramPanel.setModel(new MappingModel());
	}
}
