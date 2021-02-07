package nl.andrewlalis.erme.model;

import lombok.Getter;
import nl.andrewlalis.erme.view.view_models.RelationViewModel;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a single "relation" or table in the diagram.
 */
@Getter
public class Relation implements Serializable {
	private final MappingModel model;
	private Point position;
	private String name;
	private final List<Attribute> attributes;

	private transient boolean selected;
	private transient RelationViewModel viewModel;

	public Relation(MappingModel model, Point position, String name) {
		this.model = model;
		this.position = position;
		this.name = name;
		this.attributes = new ArrayList<>();
	}

	public void setPosition(Point position) {
		this.position = position;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public void addAttribute(Attribute attribute) {
		this.attributes.add(attribute);
		this.model.fireChangedEvent();
	}

	public void addAttribute(Attribute attribute, int index) {
		this.attributes.add(index, attribute);
		this.model.fireChangedEvent();
	}

	public void removeAttribute(Attribute attribute) {
		if (this.attributes.remove(attribute)) {
			this.model.fireChangedEvent();
		}
	}

	public RelationViewModel getViewModel() {
		if (this.viewModel == null) {
			this.viewModel = new RelationViewModel(this);
		}
		return this.viewModel;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Relation relation = (Relation) o;
		return Objects.equals(this.getName(), relation.getName());
	}

	@Override
	public int hashCode() {
		return this.getName().hashCode();
	}
}
