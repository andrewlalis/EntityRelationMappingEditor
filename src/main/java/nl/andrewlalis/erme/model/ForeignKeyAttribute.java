package nl.andrewlalis.erme.model;

import lombok.Getter;

@Getter
public class ForeignKeyAttribute extends Attribute {
	private Attribute reference;

	public ForeignKeyAttribute(Relation relation, String name, Attribute reference) {
		super(relation, AttributeType.FOREIGN_KEY, name);
		this.reference = reference;
	}

	public ForeignKeyAttribute(Relation relation, String name, String referencedRelationName, String referencedAttributeName) {
		this(relation, name, relation.getModel().findAttribute(referencedRelationName, referencedAttributeName));
		if (this.getReference() == null) {
			throw new IllegalArgumentException("Unknown attribute name.");
		}
	}

	public void setReference(Attribute reference) {
		this.reference = reference;
		this.getRelation().getModel().fireChangedEvent();
	}
}
