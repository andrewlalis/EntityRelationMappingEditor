package nl.andrewlalis.erme.model;

import lombok.Getter;

import java.util.Objects;

/**
 * A single value that belongs to a relation.
 */
@Getter
public class Attribute {
	private final Relation relation;
	private AttributeType type;
	private String name;

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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Attribute attribute = (Attribute) o;
		return type == attribute.type &&
				name.equals(attribute.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(type, name);
	}
}
