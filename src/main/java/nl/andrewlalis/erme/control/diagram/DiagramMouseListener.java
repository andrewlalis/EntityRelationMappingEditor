package nl.andrewlalis.erme.control.diagram;

import nl.andrewlalis.erme.model.MappingModel;
import nl.andrewlalis.erme.model.Relation;
import nl.andrewlalis.erme.view.DiagramPanel;

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

	@Override
	public void mousePressed(MouseEvent e) {
		DiagramPanel panel = (DiagramPanel) e.getSource();
		final Graphics2D g2d = panel.getGraphics2D();
		this.mouseDragStart = e.getPoint();
		if ((e.getModifiers() & ActionEvent.CTRL_MASK) == ActionEvent.CTRL_MASK) {
			boolean hit = false;
			for (Relation r : this.model.getRelations()) {
				if (r.getViewModel().getBounds(g2d).contains(e.getX(), e.getY())) {
					r.setSelected(!r.isSelected());
					hit = true;
				}
			}
			if (!hit) {
				this.model.getRelations().forEach(r -> r.setSelected(false));
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		super.mouseEntered(e);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		int dx = this.mouseDragStart.x - e.getX();
		int dy = this.mouseDragStart.y - e.getY();
		for (Relation r : this.model.getRelations()) {
			if (r.isSelected()) {
				r.setPosition(new Point(r.getPosition().x - dx, r.getPosition().y - dy));
			}
		}
		this.mouseDragStart = e.getPoint();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}
}
