package nl.andrewlalis.erme.control.actions;

import javax.swing.*;
import java.awt.*;
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
		int choice = JOptionPane.showConfirmDialog(
				(Component) e.getSource(),
				"Are you sure you want to quit?\nAll unsaved data will be lost.",
				"Confirm Exit",
				JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE
		);
		if (choice == JOptionPane.OK_OPTION) {
			System.exit(0);
		}
	}
}
