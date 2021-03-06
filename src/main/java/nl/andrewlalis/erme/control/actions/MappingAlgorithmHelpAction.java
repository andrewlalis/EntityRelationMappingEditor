package nl.andrewlalis.erme.control.actions;

import java.awt.*;

public class MappingAlgorithmHelpAction extends HtmlDocumentViewerAction {
	private static MappingAlgorithmHelpAction instance;

	public static MappingAlgorithmHelpAction getInstance() {
		if (instance == null) {
			instance = new MappingAlgorithmHelpAction();
		}
		return instance;
	}

	public MappingAlgorithmHelpAction() {
		super("Mapping Algorithm Help", "html/er_mapping_algorithm.html", Dialog.ModalityType.DOCUMENT_MODAL);
		this.putValue(SHORT_DESCRIPTION, "Shows a quick guide on how to map from an ER model to a schema.");
	}
}
