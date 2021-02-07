package nl.andrewlalis.erme.control.actions;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class ExitAction extends AbstractAction {
	private static ExitAction instance;

	public static ExitAction getInstance() {
		if (instance == null) {
			instance = new ExitAction();
		}
		return instance;
	}

	public ExitAction() {
		super("Exit");
		this.putValue(Action.SHORT_DESCRIPTION, "Exit the program.");
		this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.SHIFT_DOWN_MASK | InputEvent.CTRL_DOWN_MASK));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		System.exit(0);
	}
}
