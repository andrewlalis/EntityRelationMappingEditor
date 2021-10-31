package nl.andrewlalis.erme.model;

import lombok.Getter;
import nl.andrewlalis.erme.view.view_models.AttributeViewModel;

import java.util.Objects;

/**
 * A single value that belongs to a relation.
 */
@Getter
public class Attribute {
	private final Relation relation;
	private AttributeType type;
	private String name;
	private Attribute reference;

	private transient AttributeViewModel viewModel;

	public Attribute(Relation relation, AttributeType type, String name) {
		this.relation = relation;
		this.type = type;
		this.name = name;
		this.reference = null;
	}

	public void setType(AttributeType type) {
		this.type = type;
		this.relation.getModel().fireChangedEvent();
	}

	public void setName(String name) {
		this.name = name;
		this.relation.getModel().fireChangedEvent();
	}

	public boolean hasReference() {
		return this.reference != null;
	}

	public void setReference(Attribute attribute) {
		this.reference = attribute;
	}
	
	public AttributeViewModel getViewModel() {
		if (this.viewModel == null) {
			this.viewModel = new AttributeViewModel(this);
		}
		return this.viewModel;
	}

	public boolean isSelected() {
		return this.relation.getModel().getSelectionModel().isSelected(this);
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
		return Objects.hash(type, relation, name);
	}

	@Override
	public String toString() {
		return this.getName();
	}

	public Attribute copy(Relation newRelation) {
		return new Attribute(newRelation, this.getType(), this.getName());
	}
}
