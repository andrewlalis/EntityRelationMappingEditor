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
	public EditorMenuBar(DiagramPanel diagramPanel) {
		this.add(this.buildFileMenu(diagramPanel));
		this.add(this.buildEditMenu(diagramPanel));
		this.add(this.buildViewMenu(diagramPanel));
		this.add(this.buildHelpMenu(diagramPanel));
	}

	private JMenu buildFileMenu(DiagramPanel diagramPanel) {
		JMenu menu = new JMenu("File");
		menu.add(new NewModelAction(diagramPanel));
		menu.add(new SaveAction(diagramPanel));
		menu.add(new LoadAction(diagramPanel));
		menu.addSeparator();
		menu.add(new ExportToImageAction(diagramPanel));
		menu.addSeparator();
		menu.add(new ExitAction(diagramPanel));
		return menu;
	}

	private JMenu buildEditMenu(DiagramPanel diagramPanel) {
		JMenu menu = new JMenu("Edit");
		menu.add(new AddRelationAction(diagramPanel));
		menu.add(new RemoveRelationAction(diagramPanel));
		menu.add(new AddAttributeAction(diagramPanel));
		menu.add(new RemoveAttributeAction(diagramPanel));
		menu.add(new AutoPositionAction(diagramPanel));
		return menu;
	}

	private JMenu buildViewMenu(DiagramPanel diagramPanel) {
		JMenu menu = new JMenu("View");
		menu.add(new JCheckBoxMenuItem(new LolcatAction(diagramPanel)));
		menu.add(new JCheckBoxMenuItem(new VisualizeReferencesAction(diagramPanel)));
		return menu;
	}

	private JMenu buildHelpMenu(DiagramPanel diagramPanel) {
		JMenu menu = new JMenu("Help");
		menu.add(new InstructionsAction(diagramPanel));
		menu.add(new MappingAlgorithmHelpAction(diagramPanel));
		menu.add(new LoadSampleModelAction(diagramPanel));
		return menu;
	}
}
