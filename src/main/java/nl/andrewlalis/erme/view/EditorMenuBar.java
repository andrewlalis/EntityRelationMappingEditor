package nl.andrewlalis.erme.view;

import nl.andrewlalis.erme.control.actions.*;
import nl.andrewlalis.erme.control.actions.edits.AddAttributeAction;
import nl.andrewlalis.erme.control.actions.edits.AddRelationAction;
import nl.andrewlalis.erme.control.actions.edits.RemoveAttributeAction;
import nl.andrewlalis.erme.control.actions.edits.RemoveRelationAction;

import javax.swing.*;

/**
 * The menu bar that's visible atop the application.
 */
public class EditorMenuBar extends JMenuBar {
	public EditorMenuBar() {
		this.add(this.buildFileMenu());
		this.add(this.buildEditMenu());
	}

	private JMenu buildFileMenu() {
		JMenu menu = new JMenu("File");
		menu.add(SaveAction.getInstance());
		menu.add(LoadAction.getInstance());
		menu.add(ExportToImageAction.getInstance());
		menu.addSeparator();
		menu.add(ExitAction.getInstance());
		return menu;
	}

	private JMenu buildEditMenu() {
		JMenu menu = new JMenu("Edit");
		menu.add(AddRelationAction.getInstance());
		menu.add(RemoveRelationAction.getInstance());
		menu.add(AddAttributeAction.getInstance());
		menu.add(RemoveAttributeAction.getInstance());
		menu.addSeparator();
		menu.add(UndoAction.getInstance());
		menu.add(RedoAction.getInstance());
		return menu;
	}
}
