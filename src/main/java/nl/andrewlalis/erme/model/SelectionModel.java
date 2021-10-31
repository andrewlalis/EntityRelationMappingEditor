package nl.andrewlalis.erme.model;

import java.util.*;

/**
 * A model that keeps track of the application's set of selected elements.
 */
public class SelectionModel {
	private final MappingModel model;
	private final Set<Relation> selectedRelations;
	private final Set<Attribute> selectedAttributes;

	public SelectionModel(MappingModel model) {
		this.model = model;
		this.selectedRelations = new HashSet<>();
		this.selectedAttributes = new HashSet<>();
	}

	public boolean isSelected(Relation relation) {
		return this.selectedRelations.contains(relation);
	}

	public boolean isSelected(Attribute attribute) {
		return this.selectedAttributes.contains(attribute);
	}

	public List<Relation> getSelectedRelations() {
		return new ArrayList<>(this.selectedRelations);
	}

	public List<Attribute> getSelectedAttributes() {
		return new ArrayList<>(this.selectedAttributes);
	}

	public void select(Relation relation) {
		this.selectedRelations.add(relation);
	}

	public void selectAll(Collection<Relation> relations) {
		this.selectedRelations.addAll(relations);
	}

	public void select(Attribute attribute) {
		this.selectedAttributes.add(attribute);
	}

	public void toggle(Relation relation) {
		if (selectedRelations.contains(relation)) {
			selectedRelations.remove(relation);
		} else {
			selectedRelations.add(relation);
		}
	}

	public void toggle(Attribute attribute) {
		if (selectedAttributes.contains(attribute)) {
			selectedAttributes.remove(attribute);
		} else {
			selectedAttributes.add(attribute);
		}
	}

	public void clearSelection() {
		this.selectedRelations.clear();
		this.selectedAttributes.clear();
	}

	public void clearRelations() {
		this.selectedRelations.clear();
	}

	public void clearAttributes() {
		this.selectedAttributes.clear();
	}
}
