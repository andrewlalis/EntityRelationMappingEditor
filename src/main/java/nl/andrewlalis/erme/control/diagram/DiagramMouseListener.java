package nl.andrewlalis.erme.control.diagram;

import nl.andrewlalis.erme.model.Attribute;
import nl.andrewlalis.erme.model.MappingModel;
import nl.andrewlalis.erme.model.Relation;
import nl.andrewlalis.erme.view.DiagramPanel;
import nl.andrewlalis.erme.view.DiagramPopupMenu;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DiagramMouseListener extends MouseAdapter {
	private final DiagramPanel diagramPanel;
	private Point mouseDragStart;

	public DiagramMouseListener(DiagramPanel diagramPanel) {
		this.diagramPanel = diagramPanel;
	}

	/**
	 * Handles mouse presses. This is what should happen:
	 * - If the click occurs outside of the bounds of any relation, deselect all
	 * relations, if CTRL is not held down.
	 * - If the click occurs within at least one relation, select the first one,
	 * and deselect all others if CTRL is not held down.
	 * - If the user did a right-click, try to open a popup menu with some
	 * possible actions, based on what entity we're over.
	 * @param e The mouse event.
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		final DiagramPanel panel = (DiagramPanel) e.getSource();
		final Graphics2D g = panel.getGraphics2D();
		this.mouseDragStart = e.getPoint();
		final int modelX = e.getX() - panel.getPanningTranslation().x;
		final int modelY = e.getY() - panel.getPanningTranslation().y;

		final boolean isCtrlDown = (e.getModifiers() & ActionEvent.CTRL_MASK) == ActionEvent.CTRL_MASK;
		final boolean isShiftDown = (e.getModifiers() & ActionEvent.SHIFT_MASK) == ActionEvent.SHIFT_MASK;
		MappingModel model = this.diagramPanel.getModel();

		if (!isShiftDown && !isCtrlDown) {// A simple left-click anywhere should reset selection.
			model.getSelectionModel().clearSelection();
		}

		if (!isShiftDown) {// If the user clicked or CTRL+clicked, try and select the relation they clicked on.
			for (Relation r : model.getRelations()) {
				if (r.getViewModel().getBounds(g).contains(modelX, modelY)) {
					boolean anyAttributeSelected = false;
					for (Attribute a : r.getAttributes()) {
						if (a.getViewModel().getBounds(g).contains(modelX, modelY)) {
							model.getSelectionModel().toggle(a);
						}
						anyAttributeSelected = anyAttributeSelected || a.isSelected();
					}
					if (!anyAttributeSelected) {
						model.getSelectionModel().toggle(r);
						break;
					}
				}
			}
		}

		// If the user right-clicked, show a popup menu.
		if (e.getButton() == MouseEvent.BUTTON3) {
			model.setLastInteractionPoint(e.getPoint());
			DiagramPopupMenu popupMenu = new DiagramPopupMenu(this.diagramPanel);
			popupMenu.show(panel, e.getX(), e.getY());
		}

		model.fireChangedEvent();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		super.mouseEntered(e);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		final int dx = this.mouseDragStart.x - e.getX();
		final int dy = this.mouseDragStart.y - e.getY();
		final boolean isShiftDown = (e.getModifiers() & ActionEvent.SHIFT_MASK) == ActionEvent.SHIFT_MASK;
		boolean changed = false;
		MappingModel model = this.diagramPanel.getModel();

		if (isShiftDown) {
			this.diagramPanel.translate(-dx, -dy);
			this.diagramPanel.repaint();
		} else {
			for (Relation r : model.getRelations()) {
				if (model.getSelectionModel().isSelected(r)) {
					r.setPosition(new Point(r.getPosition().x - dx, r.getPosition().y - dy));
					changed = true;
				}
			}
		}

		if (changed) {
			model.fireChangedEvent();
		}
		this.mouseDragStart = e.getPoint();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}
}
