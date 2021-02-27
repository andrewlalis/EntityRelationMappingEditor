package nl.andrewlalis.erme.control.actions;

import lombok.Setter;
import nl.andrewlalis.erme.model.MappingModel;
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
public class AutoPositionAction extends AbstractAction {
	private final static int MARGIN = 10;
	private final static int PADDING = 10;
	private static AutoPositionAction instance;
	@Setter
	private DiagramPanel diagramPanel;
	@Setter
	private MappingModel model;
	public AutoPositionAction() {
		super("Align Relations");
		this.putValue(SHORT_DESCRIPTION, "Automatically Align Relations");
	}

	public static AutoPositionAction getInstance() {
		if (instance == null) {
			instance = new AutoPositionAction();
		}
		return instance;
	}

	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		if (model.getRelations().size() == 0) {
			JOptionPane.showMessageDialog(
					null,
					"Cannot position all relations when there are no relations present",
					"Relations Required",
					JOptionPane.WARNING_MESSAGE
			);
			return;
		}
		String[] choices = new String[]{"Alphabeticaly", "Custom Order"};
		String choice = (String) JOptionPane.showInputDialog(
				null,
				"Select how to sort the relations",
				"Position Relations",
				JOptionPane.PLAIN_MESSAGE,
				null,
				choices,
				0);
		if (choice == null) return;
		if (choice.equals(choices[0])) {
			positionRelation();
		} else if (choice.equals(choices[1])) {
			JOptionPane.showConfirmDialog(
					null,
					OrderableListPanel.getInstance(),
					"teststring",
					JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.PLAIN_MESSAGE);
			this.positionRelations(OrderableListPanel.getInstance().getOrderList());
		}
		diagramPanel.repaint();
	}

	/**
	 * Sets the location on all relations in the orderList to be vertically aligned
	 * @param orderList The ordered list of relations to be aligned
	 */
	private void positionRelations(ArrayList<Relation> orderList) {
		if (orderList.isEmpty()) return;
		AtomicInteger vertSpace = new AtomicInteger(0);
		orderList.forEach(r -> {
			int height = (int) r.getViewModel().getBounds(diagramPanel.getGraphics2D()).getHeight();
			vertSpace.set(Math.max(vertSpace.get(), height));
		});
		vertSpace.addAndGet(PADDING);
		AtomicInteger vertPos = new AtomicInteger(MARGIN);
		orderList.forEach(r -> {
			r.setPosition(new Point(MARGIN, vertPos.getAndAdd(vertSpace.get())));
		});

		diagramPanel.centerModel();
	}

	/**
	 * Creates an orderList by grabbing all relations and sorting them
	 */
	private void positionRelation() {
		ArrayList<Relation> relationList = new ArrayList<>(model.getRelations());
		Collections.sort(relationList);
		positionRelations(relationList);
	}
}
