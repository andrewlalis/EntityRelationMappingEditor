package nl.andrewlalis.erme.control.actions;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class RedoAction extends AbstractAction {
	private static RedoAction instance;

	public static RedoAction getInstance() {
		if (instance == null) {
			instance = new RedoAction();
		}
		return instance;
	}

	public RedoAction() {
		super("Redo");
		this.putValue(Action.SHORT_DESCRIPTION, "Redoes a previously undone action.");
		this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("Redo");
	}
}
