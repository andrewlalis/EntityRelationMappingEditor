package nl.andrewlalis.erme.control.actions;

import lombok.AccessLevel;
import lombok.Getter;
import nl.andrewlalis.erme.view.DiagramPanel;

import javax.swing.*;

public abstract class DiagramPanelAction extends AbstractAction {
	@Getter(AccessLevel.PROTECTED)
	private final DiagramPanel diagramPanel;

	public DiagramPanelAction(String name, DiagramPanel diagramPanel) {
		super(name);
		this.diagramPanel = diagramPanel;
	}
}
