package nl.andrewlalis.erme.model;

import lombok.Getter;
import nl.andrewlalis.erme.view.view_models.RelationViewModel;
import nl.andrewlalis.erme.view.view_models.ViewModel;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Represents a single "relation" or table in the diagram.
 */
@Getter
public class Relation implements Serializable, Viewable, Comparable<Relation> {
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
			this.model.removeAllReferencingAttributes(attribute);
			this.model.fireChangedEvent();
		}
	}

	@Override
	public ViewModel getViewModel() {
		if (this.viewModel == null) {
			this.viewModel = new RelationViewModel(this);
		}
		return this.viewModel;
	}

	public List<Attribute> getReferencableAttributes() {
		return this.attributes.stream()
				.filter(a -> a.getType() == AttributeType.ID_KEY || a.getType() == AttributeType.PARTIAL_ID_KEY)
				.collect(Collectors.toList());
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

	@Override
	public String toString() {
		return this.getName();
	}

	public Relation copy(MappingModel newModel) {
		Relation c = new Relation(newModel, new Point(this.getPosition()), this.getName());
		this.getAttributes().forEach(a -> c.addAttribute(a.copy(c)));
		return c;
	}

	@Override
	public int compareTo(Relation relation) {
		return this.name.compareTo(relation.name);
	}
}
