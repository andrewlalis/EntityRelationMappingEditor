package nl.andrewlalis.erme.view;

import nl.andrewlalis.erme.model.Relation;

import java.util.ArrayList;
import java.util.Collections;

public class OrderableListModel {
    private ArrayList<Relation> list;

    public void moveUp(int index) {
        if (index > 0) {
            Collections.swap(list, index, index - 1);
        }
    }
}
