package nl.andrewlalis.erme.control.actions;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class ExportToImageAction extends AbstractAction {
	private static ExportToImageAction instance;

	public static ExportToImageAction getInstance() {
		if (instance == null) {
			instance = new ExportToImageAction();
		}
		return instance;
	}

	public ExportToImageAction() {
		super("Export to Image");
		this.putValue(Action.SHORT_DESCRIPTION, "Export the current diagram to an image.");
		this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_DOWN_MASK));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("Export to image.");
	}
}
