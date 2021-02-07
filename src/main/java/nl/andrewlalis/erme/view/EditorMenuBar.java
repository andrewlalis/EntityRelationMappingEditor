package nl.andrewlalis.erme.view;

import nl.andrewlalis.erme.control.actions.*;

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
		JMenuItem saveItem = new JMenuItem(SaveAction.getInstance());
		menu.add(saveItem);
		JMenuItem exportAsImageItem = new JMenuItem(ExportToImageAction.getInstance());
		menu.add(exportAsImageItem);
		JMenuItem exitItem = new JMenuItem(ExitAction.getInstance());
		menu.add(exitItem);
		return menu;
	}

	private JMenu buildEditMenu() {
		JMenu menu = new JMenu("Edit");
		JMenuItem undoItem = new JMenuItem(UndoAction.getInstance());
		menu.add(undoItem);
		JMenuItem redoItem = new JMenuItem(RedoAction.getInstance());
		menu.add(redoItem);
		return menu;
	}
}
