package nl.andrewlalis.erme.control.actions;

import nl.andrewlalis.erme.view.DiagramPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class LolcatAction extends DiagramPanelAction {
	public LolcatAction(DiagramPanel diagramPanel) {
		super("Toggle Lolcat Mode", diagramPanel);
		this.putValue(SHORT_DESCRIPTION, "Does some wacky color stuff.");
	}

	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		boolean lolcatEnabled = ((AbstractButton)actionEvent.getSource()).getModel().isSelected();
		getDiagramPanel().getModel().setLolcatEnabled(lolcatEnabled);
		getDiagramPanel().repaint();
	}
}
