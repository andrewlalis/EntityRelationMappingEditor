package nl.andrewlalis.erme.model;

import lombok.Getter;
import nl.andrewlalis.erme.view.view_models.RelationViewModel;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a single "relation" or table in the diagram.
 */
@Getter
public class Relation {
	private final MappingModel model;
	private Point position;
	private String name;
	private final List<Attribute> attributes;

	private transient boolean selected;
	private final transient RelationViewModel viewModel;

	public Relation(MappingModel model, Point position, String name) {
		this.model = model;
		this.position = position;
		this.name = name;
		this.attributes = new ArrayList<>();
		this.viewModel = new RelationViewModel(this);
	}

	public void setPosition(Point position) {
		if (!this.position.equals(position)) {
			this.position = position;
			this.model.fireChangedEvent();
		}
	}

	public void setName(String name) {
		if (!this.name.equals(name)) {
			this.name = name;
			this.model.fireChangedEvent();
		}
	}

	public void setSelected(boolean selected) {
		if (selected != this.selected) {
			this.selected = selected;
			this.model.fireChangedEvent();
		}
	}

	public void addAttribute(Attribute attribute) {
		this.attributes.add(attribute);
		this.model.fireChangedEvent();
	}

	public void removeAttribute(Attribute attribute) {
		if (this.attributes.remove(attribute)) {
			this.model.fireChangedEvent();
		}
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
