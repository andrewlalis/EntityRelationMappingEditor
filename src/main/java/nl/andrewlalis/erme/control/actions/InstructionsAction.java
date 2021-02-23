package nl.andrewlalis.erme.control.actions;

public class InstructionsAction extends HtmlDocumentViewerAction {
	private static InstructionsAction instance;

	public static InstructionsAction getInstance() {
		if (instance == null) instance = new InstructionsAction();
		return instance;
	}

	public InstructionsAction() {
		super("Instructions", "html/instructions.html");
		this.putValue(SHORT_DESCRIPTION, "Instructions for how to use this program.");
	}
}
