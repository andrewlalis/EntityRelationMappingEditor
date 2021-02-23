package nl.andrewlalis.erme.control.actions;

import nl.andrewlalis.erme.EntityRelationMappingEditor;
import nl.andrewlalis.erme.view.view_models.AttributeViewModel;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class LolcatAction extends AbstractAction {
	private static LolcatAction instance;
	public static LolcatAction getInstance() {
		if (instance == null) {
			instance = new LolcatAction();
		}
		return instance;
	}

	public LolcatAction() {
		super("Toggle Lolcat Mode");
	}

	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		AttributeViewModel.setLolcatMode(((AbstractButton)actionEvent.getSource()).getModel().isSelected());
		EntityRelationMappingEditor.getFrame().getContentPane().repaint();
	}
}
