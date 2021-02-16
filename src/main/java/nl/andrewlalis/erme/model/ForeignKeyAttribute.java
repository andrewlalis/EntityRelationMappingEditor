package nl.andrewlalis.erme.model;

import lombok.Getter;

@Getter
public class ForeignKeyAttribute extends Attribute {
	private Attribute reference;

	public ForeignKeyAttribute(Relation relation, AttributeType type, String name, Attribute reference) {
		super(relation, type, name);
		this.reference = reference;
	}

	public ForeignKeyAttribute(Relation relation, AttributeType type, String name, String referencedRelationName, String referencedAttributeName) {
		this(relation, type, name, relation.getModel().findAttribute(referencedRelationName, referencedAttributeName));
		if (this.getReference() == null) {
			throw new IllegalArgumentException("Unknown attribute name.");
		}
	}

	public void setReference(Attribute reference) {
		this.reference = reference;
		this.getRelation().getModel().fireChangedEvent();
	}

	public String getFullReferenceName() {
		return this.getReference().getRelation().getName() + "." + this.getReference().getName();
	}

	@Override
	public String toString() {
		return super.toString() + "->" + this.getFullReferenceName();
	}

	@Override
	public ForeignKeyAttribute copy(Relation newRelation) {
		return new ForeignKeyAttribute(newRelation, this.getType(), this.getName(), this.getReference());
	}
}
