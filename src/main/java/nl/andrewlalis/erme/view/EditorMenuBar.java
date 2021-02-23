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
	private final boolean includeAdminActions;

	public EditorMenuBar(boolean includeAdminActions) {
		this.includeAdminActions = includeAdminActions;
		this.add(this.buildFileMenu());
		this.add(this.buildEditMenu());
		this.add(this.buildHelpMenu());
	}

	private JMenu buildFileMenu() {
		JMenu menu = new JMenu("File");
		menu.add(NewModelAction.getInstance());
		menu.add(SaveAction.getInstance());
		menu.add(LoadAction.getInstance());
		menu.addSeparator();
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
		menu.add(new JCheckBoxMenuItem(LolcatAction.getInstance()));
		menu.addSeparator();
		menu.add(UndoAction.getInstance());
		menu.add(RedoAction.getInstance());
		return menu;
	}

	private JMenu buildHelpMenu() {
		JMenu menu = new JMenu("Help");
		menu.add(InstructionsAction.getInstance());
		menu.add(MappingAlgorithmHelpAction.getInstance());
		menu.add(LoadSampleModelAction.getInstance());
		menu.add(AboutAction.getInstance());
		return menu;
	}
}
