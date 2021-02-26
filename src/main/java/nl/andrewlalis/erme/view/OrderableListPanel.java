package nl.andrewlalis.erme.view;

import lombok.Setter;
import nl.andrewlalis.erme.model.MappingModel;
import nl.andrewlalis.erme.model.ModelChangeListener;
import nl.andrewlalis.erme.model.Relation;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * A panel to be used in a popup that has a OrderableListModel implemented. Implements ModelChangeListener to be able
 * to update the list of relations when new ones are added or ones are removed.
 */
public class OrderableListPanel extends JPanel implements ModelChangeListener {
	private static OrderableListPanel instance;
	private final JList<Relation> list;
	private final OrderableListModel listModel;
	@Setter
	private MappingModel model;

	private OrderableListPanel() {
		list = new JList<>();
		listModel = new OrderableListModel();
		list.setModel(listModel);

		JButton up = new JButton(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				if (list.isSelectionEmpty()) return;
				listModel.moveUp(list.getSelectedIndex());
				list.setSelectedIndex(list.getSelectedIndex() - 1);
			}
		});
		up.setText("\u2227");
		JButton down = new JButton(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				if (list.isSelectionEmpty()) return;
				listModel.moveDown(list.getSelectedIndex());
				list.setSelectedIndex(list.getSelectedIndex() + 1);
			}
		});
		down.setText("\u2228");

		this.add(list);
		this.add(up);
		this.add(down);
	}

	public static OrderableListPanel getInstance() {
		if (instance == null) {
			instance = new OrderableListPanel();
		}
		return instance;
	}

	/**
	 * Updates removed and new relations in the listModel. Does it in a special way to preserve existing ordering in the
	 * list, so user has to do minimal re-sorting.
	 */
    @Override
    public void onModelChanged() {
    	if (this.model == null) return;
		Set<Relation> newRelations = new HashSet<>(model.getRelations());
		newRelations.removeAll(listModel.getList());

        Set<Relation> removedRelations = new HashSet<>(listModel.getList());
        removedRelations.removeAll(model.getRelations());

        listModel.removeAll(removedRelations);
        listModel.addAll(newRelations);
    }

    public ArrayList<Relation> getOrderList() {
    	return new ArrayList<>(listModel.getList());
	}
}
