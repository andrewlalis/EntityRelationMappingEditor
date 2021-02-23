package nl.andrewlalis.erme.control.actions;

import lombok.Getter;
import lombok.Setter;
import nl.andrewlalis.erme.EntityRelationMappingEditor;
import nl.andrewlalis.erme.view.DiagramPanel;
import nl.andrewlalis.erme.view.view_models.AttributeViewModel;

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
	}

	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		lolcatEnabled = ((AbstractButton)actionEvent.getSource()).getModel().isSelected();
		diagramPanel.repaint();
	}
}
