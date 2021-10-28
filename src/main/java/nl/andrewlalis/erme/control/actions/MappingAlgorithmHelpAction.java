package nl.andrewlalis.erme.control.actions;

import nl.andrewlalis.erme.view.DiagramPanel;

import java.awt.*;

public class MappingAlgorithmHelpAction extends HtmlDocumentViewerAction {
	public MappingAlgorithmHelpAction(DiagramPanel diagramPanel) {
		super("Mapping Algorithm Help", "html/er_mapping_algorithm.html", Dialog.ModalityType.DOCUMENT_MODAL, diagramPanel);
		this.putValue(SHORT_DESCRIPTION, "Shows a quick guide on how to map from an ER model to a schema.");
	}
}
