package nl.andrewlalis.erme.view.view_models;

import nl.andrewlalis.erme.model.MappingModel;
import nl.andrewlalis.erme.model.Relation;

import java.awt.*;

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
		for (Relation r : this.model.getRelations()) {
			r.getViewModel().draw(g);
		}
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
