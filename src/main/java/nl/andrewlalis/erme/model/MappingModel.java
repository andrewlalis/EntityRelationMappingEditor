package nl.andrewlalis.erme.model;

import lombok.Getter;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * This model contains all the information about a single mapping diagram,
 * including each mapped table and the links between them.
 */
public class MappingModel implements Serializable {
	@Getter
	private final Set<Relation> relations;

	private transient final Set<ModelChangeListener> changeListeners;

	public MappingModel() {
		this.relations = new HashSet<>();
		this.changeListeners = new HashSet<>();
	}

	public void addRelation(Relation r) {
		if (this.relations.add(r)) {
			this.fireChangedEvent();
		}
	}

	public void removeRelation(Relation r) {
		if (this.relations.remove(r)) {
			this.fireChangedEvent();
		}
	}

	public void addChangeListener(ModelChangeListener listener) {
		this.changeListeners.add(listener);
		listener.onModelChanged();
	}

	public final void fireChangedEvent() {
		this.changeListeners.forEach(ModelChangeListener::onModelChanged);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || this.getClass() != o.getClass()) return false;
		MappingModel that = (MappingModel) o;
		return this.getRelations().equals(that.getRelations());
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.getRelations());
	}
}
