package nl.andrewlalis.erme.view;

import nl.andrewlalis.erme.EntityRelationMappingEditor;
import nl.andrewlalis.erme.control.actions.*;
import nl.andrewlalis.erme.control.actions.edits.AddAttributeAction;
import nl.andrewlalis.erme.control.actions.edits.AddRelationAction;
import nl.andrewlalis.erme.control.actions.edits.RemoveAttributeAction;
import nl.andrewlalis.erme.control.actions.edits.RemoveRelationAction;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URI;

/**
 * The menu bar that's visible atop the application.
 */
public class EditorMenuBar extends JMenuBar {
	public EditorMenuBar() {
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
		menu.addSeparator();
		menu.add(UndoAction.getInstance());
		menu.add(RedoAction.getInstance());
		return menu;
	}

	private JMenu buildHelpMenu() {
		JMenu menu = new JMenu("Help");
		JMenuItem instructionsItem = new JMenuItem("GitHub (Instructions)");
		instructionsItem.addActionListener(e -> {
			try {
				Desktop.getDesktop().browse(URI.create("https://github.com/andrewlalis/EntityRelationMappingEditor"));
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		});
		menu.add(instructionsItem);
		JMenuItem aboutItem = new JMenuItem("About");
		aboutItem.addActionListener(e -> JOptionPane.showMessageDialog(
				(Component) e.getSource(),
				"Entity-Relation Mapping Editor\n" +
						"by Andrew Lalis\n" +
						"Version " + EntityRelationMappingEditor.VERSION + "\n" +
						"To report bugs or make suggestions, please visit the GitHub\n" +
						"repository for this application and create a new issue.\n\n" +
						"Thank you for using the ERME!",
				"About ERME",
				JOptionPane.INFORMATION_MESSAGE
		));
		menu.add(aboutItem);
		return menu;
	}
}
