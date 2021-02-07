package nl.andrewlalis.erme.view.view_models;

import nl.andrewlalis.erme.model.MappingModel;
import nl.andrewlalis.erme.model.Relation;

import java.awt.*;

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
}
