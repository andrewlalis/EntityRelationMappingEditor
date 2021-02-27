package nl.andrewlalis.erme.control.actions;

import lombok.Setter;
import nl.andrewlalis.erme.EntityRelationMappingEditor;
import nl.andrewlalis.erme.view.DiagramPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class AboutAction extends AbstractAction {
	private static AboutAction instance;

	public static AboutAction getInstance() {
		if (instance == null) instance = new AboutAction();
		return instance;
	}

	@Setter
	private DiagramPanel diagramPanel;

	public AboutAction() {
		super("About");
		this.putValue(SHORT_DESCRIPTION, "Show some information about this program.");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JOptionPane.showMessageDialog(
				this.diagramPanel,
				"Entity-Relation Mapping Editor\n" +
						"by Andrew Lalis\n" +
						"Version " + EntityRelationMappingEditor.VERSION + "\n" +
						"To report bugs or make suggestions, please visit the GitHub\n" +
						"repository for this application and create a new issue.\n\n" +
						"Thank you for using the ERME!",
				"About ERME",
				JOptionPane.INFORMATION_MESSAGE
		);
	}
}
