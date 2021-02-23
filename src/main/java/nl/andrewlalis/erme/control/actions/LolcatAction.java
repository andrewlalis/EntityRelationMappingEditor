package nl.andrewlalis.erme.control.actions;

import lombok.Getter;
import lombok.Setter;
import nl.andrewlalis.erme.view.DiagramPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class LolcatAction extends AbstractAction {
	private static LolcatAction instance;

	@Getter
	@Setter
	private boolean lolcatEnabled = false;

	public static LolcatAction getInstance() {
		if (instance == null) {
			instance = new LolcatAction();
		}
		return instance;
	}

	@Setter
	private DiagramPanel diagramPanel;

	public LolcatAction() {
		super("Toggle Lolcat Mode");
		this.putValue(SHORT_DESCRIPTION, "Does some wacky color stuff.");
	}

	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		lolcatEnabled = ((AbstractButton)actionEvent.getSource()).getModel().isSelected();
		diagramPanel.repaint();
	}
}
