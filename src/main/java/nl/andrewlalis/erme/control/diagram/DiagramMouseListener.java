package nl.andrewlalis.erme.control.diagram;

import nl.andrewlalis.erme.model.MappingModel;
import nl.andrewlalis.erme.model.Relation;
import nl.andrewlalis.erme.view.DiagramPanel;
import nl.andrewlalis.erme.view.DiagramPopupMenu;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DiagramMouseListener extends MouseAdapter {
	private final MappingModel model;
	private Point mouseDragStart;

	public DiagramMouseListener(MappingModel model) {
		this.model = model;
	}

	/**
	 * Handles mouse presses. This is what should happen:
	 * - If the click occurs outside of the bounds of any relation, deselect all
	 * relations, if CTRL is not held down.
	 * - If the click occurs within at least one relation, select the first one,
	 * and deselect all others if CTRL is not held down.
	 * - If the user did a right-click, try to open a popup menu with some
	 * possible actions.
	 * @param e The mouse event.
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		DiagramPanel panel = (DiagramPanel) e.getSource();
		final Graphics2D g2d = panel.getGraphics2D();
		this.mouseDragStart = e.getPoint();

		boolean isCtrlDown = (e.getModifiers() & ActionEvent.CTRL_MASK) == ActionEvent.CTRL_MASK;

		if (!isCtrlDown) {
			this.model.getRelations().forEach(r -> r.setSelected(false));
		}
		for (Relation r : this.model.getRelations()) {
			if (r.getViewModel().getBounds(g2d).contains(e.getX(), e.getY())) {
				r.setSelected(!r.isSelected());
				break;
			}
		}

		if (e.getButton() == MouseEvent.BUTTON3) {
			DiagramPopupMenu popupMenu = new DiagramPopupMenu(this.model);
			popupMenu.show(panel, e.getX(), e.getY());
		}

		this.model.fireChangedEvent();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		super.mouseEntered(e);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		int dx = this.mouseDragStart.x - e.getX();
		int dy = this.mouseDragStart.y - e.getY();
		boolean changed = false;
		for (Relation r : this.model.getRelations()) {
			if (r.isSelected()) {
				r.setPosition(new Point(r.getPosition().x - dx, r.getPosition().y - dy));
				changed = true;
			}
		}
		if (changed) {
			this.model.fireChangedEvent();
		}
		this.mouseDragStart = e.getPoint();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}
}
