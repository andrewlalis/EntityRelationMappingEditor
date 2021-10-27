package nl.andrewlalis.erme.view.view_models;

import nl.andrewlalis.erme.control.actions.VisualizeReferencesAction;
import nl.andrewlalis.erme.model.Attribute;
import nl.andrewlalis.erme.model.ForeignKeyAttribute;
import nl.andrewlalis.erme.model.MappingModel;
import nl.andrewlalis.erme.model.Relation;

import java.awt.*;
import java.awt.color.ColorSpace;
import java.util.Random;

/**
 * View model for rendering an entire {@code MappingModel} object.
 */
public class MappingModelViewModel implements ViewModel {
	private final MappingModel model;

	public MappingModelViewModel(MappingModel model) {
		this.model = model;
	}

	@Override
	public void draw(Graphics2D g) {
		if (VisualizeReferencesAction.getInstance().isReferenceVisualizationEnabled()) {
			visualizeReferences(g);
		}
		for (Relation r : this.model.getRelations()) {
			r.getViewModel().draw(g);
		}
	}

	private void visualizeReferences(Graphics2D g) {
		Graphics2D g2 = (Graphics2D) g.create();
		Stroke dashedStroke = new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 0, new float[]{5}, 0);
		g2.setStroke(dashedStroke);
		for (Relation r : this.model.getRelations()) {
			for (Attribute a : r.getAttributes()) {
				if (a instanceof ForeignKeyAttribute) {
					ForeignKeyAttribute fk = (ForeignKeyAttribute) a;
					// Generate a random HSB color for the line, seeded using the referenced attribute's hash code.
					Random random = new Random(fk.getReference().hashCode());
					g2.setColor(Color.getHSBColor(random.nextFloat(), 1.0f, 0.8f));
					Rectangle sourceBounds = fk.getViewModel().getBounds(g);
					Rectangle targetBounds = fk.getReference().getViewModel().getBounds(g);
					Point sourcePoint = new Point(sourceBounds.x + sourceBounds.width / 2, sourceBounds.y + 3 * targetBounds.height / 4);
					Point targetPoint = new Point(targetBounds.x + targetBounds.width / 2, targetBounds.y + 3 * targetBounds.height / 4);
					g2.drawLine(sourcePoint.x, sourcePoint.y, targetPoint.x, targetPoint.y);
				}
			}
		}
		g2.dispose();
	}

	@Override
	public Rectangle getBounds(Graphics2D g) {
		int minX = Integer.MAX_VALUE;
		int minY = Integer.MAX_VALUE;
		int maxX = Integer.MIN_VALUE;
		int maxY = Integer.MIN_VALUE;
		for (Relation r : model.getRelations()) {
			Rectangle bounds = r.getViewModel().getBounds(g);
			minX = Math.min(minX, bounds.x);
			minY = Math.min(minY, bounds.y);
			maxX = Math.max(maxX, bounds.x + bounds.width);
			maxY = Math.max(maxY, bounds.y + bounds.height);
		}
		return new Rectangle(minX, minY, Math.abs(maxX - minX), Math.abs(maxY - minY));
	}
}
