package nl.andrewlalis.erme.model;

import lombok.Getter;
import nl.andrewlalis.erme.view.view_models.MappingModelViewModel;
import nl.andrewlalis.erme.view.view_models.ViewModel;

import java.awt.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This model contains all the information about a single mapping diagram,
 * including each mapped table and the links between them.
 */
public class MappingModel implements Serializable, Viewable {
	@Getter
	private final Set<Relation> relations;

	private transient Set<ModelChangeListener> changeListeners;

	private final static long serialVersionUID = 6153776381873250304L;

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

	public List<Relation> getSelectedRelations() {
		return this.relations.stream().filter(Relation::isSelected).collect(Collectors.toList());
	}

	public Attribute findAttribute(String relationName, String attributeName) {
		for (Relation r : this.getRelations()) {
			if (!r.getName().equals(relationName)) continue;
			for (Attribute a : r.getAttributes()) {
				if (a.getName().equals(attributeName)) {
					return a;
				}
			}
		}
		return null;
	}

	public void removeAllReferencingAttributes(Attribute referenced) {
		for (Relation r : this.getRelations()) {
			Set<Attribute> removalSet = new HashSet<>();
			for (Attribute a : r.getAttributes()) {
				if (a instanceof ForeignKeyAttribute) {
					ForeignKeyAttribute fkA = (ForeignKeyAttribute) a;
					if (fkA.getReference().equals(referenced)) {
						removalSet.add(fkA);
					}
				}
			}
			removalSet.forEach(r::removeAttribute);
		}
	}

	public Rectangle getRelationBounds() {
		int minX = Integer.MAX_VALUE;
		int minY = Integer.MAX_VALUE;
		int maxX = Integer.MIN_VALUE;
		int maxY = Integer.MIN_VALUE;
		for (Relation r : this.getRelations()) {
			minX = Math.min(minX, r.getPosition().x);
			minY = Math.min(minY, r.getPosition().y);
			maxX = Math.max(maxX, r.getPosition().x);
			maxY = Math.max(maxY, r.getPosition().y);
		}
		return new Rectangle(minX, minY, maxX - minX, maxY - minY);
	}

	public void addChangeListener(ModelChangeListener listener) {
		if (this.changeListeners == null) {
			this.changeListeners = new HashSet<>();
		}
		this.changeListeners.add(listener);
		listener.onModelChanged();
	}

	public final void fireChangedEvent() {
		this.changeListeners.forEach(ModelChangeListener::onModelChanged);
	}

	/**
	 * Updates the positions of all relations so that the bounding box for this
	 * model starts at 0, 0.
	 */
	public final void normalizeRelationPositions() {
		int minX = Integer.MAX_VALUE;
		int minY = Integer.MAX_VALUE;
		for (Relation r : this.getRelations()) {
			minX = Math.min(minX, r.getPosition().x);
			minY = Math.min(minY, r.getPosition().y);
		}
		for (Relation r : this.getRelations()) {
			final Point current = r.getPosition();
			r.setPosition(new Point(current.x - minX, current.y - minY));
		}
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

	@Override
	public ViewModel getViewModel() {
		return new MappingModelViewModel(this);
	}

	public MappingModel copy() {
		MappingModel c = new MappingModel();
		this.getRelations().forEach(r -> c.addRelation(r.copy(c)));
		return c;
	}
}
