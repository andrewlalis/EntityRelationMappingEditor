package nl.andrewlalis.erme.model;

import lombok.Getter;

public enum AttributeType {
	PLAIN("Plain"),
	ID_KEY("Identifier"),
	PARTIAL_ID_KEY("Partial Identifier");

	@Getter
	private final String name;

	AttributeType(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return this.getName();
	}
}
