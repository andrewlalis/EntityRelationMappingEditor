package nl.andrewlalis.erme.model;

import lombok.Getter;
import nl.andrewlalis.erme.view.view_models.AttributeViewModel;

import java.io.Serializable;
import java.util.Objects;

/**
 * A single value that belongs to a relation.
 */
@Getter
public class Attribute implements Serializable {
	private final Relation relation;
	private AttributeType type;
	private String name;

	private AttributeViewModel viewModel;

	public Attribute(Relation relation, AttributeType type, String name) {
		this.relation = relation;
		this.type = type;
		this.name = name;
	}

	public void setType(AttributeType type) {
		this.type = type;
		this.relation.getModel().fireChangedEvent();
	}

	public void setName(String name) {
		this.name = name;
		this.relation.getModel().fireChangedEvent();
	}

	public AttributeViewModel getViewModel() {
		if (this.viewModel == null) {
			this.viewModel = new AttributeViewModel(this);
		}
		return this.viewModel;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Attribute attribute = (Attribute) o;
		return type == attribute.type &&
				relation.equals(attribute.getRelation()) &&
				name.equals(attribute.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(type, name);
	}

	@Override
	public String toString() {
		return this.getName();
	}
}
