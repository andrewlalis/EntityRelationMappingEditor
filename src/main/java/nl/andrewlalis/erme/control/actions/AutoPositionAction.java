package nl.andrewlalis.erme.control.actions;

import nl.andrewlalis.erme.model.Relation;
import nl.andrewlalis.erme.view.DiagramPanel;
import nl.andrewlalis.erme.view.OrderableListPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A class that implements an AutoPositionAction. This automatically (vertically) positions all relations in the model
 * based either on alphabetic ordering (by name) or an order that's set by the user
 */
public class AutoPositionAction extends DiagramPanelAction {
	private final static int MARGIN = 10;
	private final static int PADDING = 10;

	public AutoPositionAction(DiagramPanel diagramPanel) {
		super("Align Relations", diagramPanel);
		this.putValue(SHORT_DESCRIPTION, "Automatically Align Relations");
	}

	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		if (getDiagramPanel().getModel().getRelations().size() == 0) {
			JOptionPane.showMessageDialog(
					getDiagramPanel(),
					"Cannot position all relations when there are no relations present",
					"Relations Required",
					JOptionPane.WARNING_MESSAGE
			);
			return;
		}
		String[] choices = new String[]{"Alphabetically", "Custom Order"};
		String choice = (String) JOptionPane.showInputDialog(
				getDiagramPanel(),
				"Select how to sort the relations",
				"Position Relations",
				JOptionPane.PLAIN_MESSAGE,
				null,
				choices,
				0);
		if (choice == null) return;
		if (choice.equals(choices[0])) {
			positionRelations(getAlphabeticRelationList());
		} else if (choice.equals(choices[1])) {
			JOptionPane.showConfirmDialog(
					getDiagramPanel(),
					OrderableListPanel.getInstance(),
					"teststring",
					JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.PLAIN_MESSAGE);
			this.positionRelations(OrderableListPanel.getInstance().getOrderList());
		}
		getDiagramPanel().repaint();
	}

	/**
	 * Sets the location on all relations in the orderList to be vertically aligned
	 * @param orderList The ordered list of relations to be aligned
	 */
	private void positionRelations(ArrayList<Relation> orderList) {
		if (orderList.isEmpty()) return;
		AtomicInteger vertSpace = new AtomicInteger(0);
		orderList.forEach(r -> {
			int height = (int) r.getViewModel().getBounds(getDiagramPanel().getGraphics2D()).getHeight();
			vertSpace.set(Math.max(vertSpace.get(), height));
		});
		vertSpace.addAndGet(PADDING);
		AtomicInteger vertPos = new AtomicInteger(MARGIN);
		orderList.forEach(r -> r.setPosition(new Point(MARGIN, vertPos.getAndAdd(vertSpace.get()))));
		getDiagramPanel().centerModel();
	}

	/**
	 * Creates an orderList by grabbing all relations and sorting them
	 */
	private ArrayList<Relation> getAlphabeticRelationList() {
		ArrayList<Relation> relationList = new ArrayList<>(getDiagramPanel().getModel().getRelations());
		Collections.sort(relationList);
		return relationList;
	}
}
