package nl.andrewlalis.erme.control.actions;

import lombok.Getter;
import lombok.Setter;
import nl.andrewlalis.erme.model.MappingModel;
import nl.andrewlalis.erme.model.Relation;
import nl.andrewlalis.erme.view.DiagramPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;

public class AutoPositionAction extends AbstractAction {
	private static AutoPositionAction instance;
	private final static int MARGIN = 10;
	private final static int PADDING = 10;

	public static AutoPositionAction getInstance() {
		if (instance == null) {
			instance = new AutoPositionAction();
		}
		return instance;
	}

	@Setter
	private DiagramPanel diagramPanel;
	@Setter
	private MappingModel model;

	public AutoPositionAction() {
		super("Position Relations");
		this.putValue(SHORT_DESCRIPTION, "Automatically Position Relations");
	}

	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		this.positionRelations();
		diagramPanel.repaint();
	}

	private void positionRelations() {
		diagramPanel.resetTranslation();
		ArrayList<Relation> relationList = new ArrayList<>(model.getRelations());
		Collections.sort(relationList);
		if (relationList.isEmpty()) return;

		int vertSpace = (int) relationList.get(0).getViewModel().getBounds(diagramPanel.getGraphics2D()).getHeight() + PADDING;
		AtomicInteger vertPos = new AtomicInteger(MARGIN);
		relationList.forEach(r -> {
			r.setPosition(new Point(MARGIN, vertPos.getAndAdd(vertSpace)));
		});
	}
}
