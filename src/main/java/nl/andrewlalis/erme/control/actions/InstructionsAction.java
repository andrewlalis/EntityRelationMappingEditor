package nl.andrewlalis.erme.control.actions;

import nl.andrewlalis.erme.view.DiagramPanel;

public class InstructionsAction extends HtmlDocumentViewerAction {
	public InstructionsAction(DiagramPanel diagramPanel) {
		super("Instructions", "html/instructions.html", diagramPanel);
		this.putValue(SHORT_DESCRIPTION, "Instructions for how to use this program.");
	}
}
