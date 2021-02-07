package nl.andrewlalis.erme.control.actions;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class UndoAction extends AbstractAction {
	private static UndoAction instance;

	public static UndoAction getInstance() {
		if (instance == null) {
			instance = new UndoAction();
		}
		return instance;
	}

	public UndoAction() {
		super("Undo");
		this.putValue(Action.SHORT_DESCRIPTION, "Undo the last action.");
		this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK));
		this.setEnabled(false);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("Undo");
	}
}
