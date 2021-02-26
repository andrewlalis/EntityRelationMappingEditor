package nl.andrewlalis.erme.view;

import lombok.Getter;
import nl.andrewlalis.erme.model.Relation;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * A custom model for a ListModel that supports ordering by publically accessable methods
 */
public class OrderableListModel extends AbstractListModel<Relation> {
	@Getter
	private final ArrayList<Relation> list = new ArrayList<>();

	public OrderableListModel() {
		super();
	}

	public void addAll(Collection<Relation> relations) {
		list.addAll(relations);
	}

	public void removeAll(Collection<Relation> relations) {
		list.removeAll(relations);
	}

	public void add(Relation relation) {
		list.add(relation);
	}

	public void remove(Relation relation) {
		list.remove(relation);
	}

	public void moveUp(int index) {
		if (index > 0) {
			Collections.swap(list, index, index - 1);
		}

		this.fireContentsChanged(this, index, index - 1);
	}

	public void moveDown(int index) {
        if (index < this.getSize() - 1) {
            Collections.swap(list, index, index + 1);
        }

        this.fireContentsChanged(this, index, index + 1);
	}

	@Override
	public int getSize() {
		return list.size();
	}

	@Override
	public Relation getElementAt(int i) {
		return list.get(i);
	}
}
