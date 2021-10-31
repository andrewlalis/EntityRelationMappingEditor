package nl.andrewlalis.erme;

import com.formdev.flatlaf.FlatLightLaf;
import nl.andrewlalis.erme.view.EditorFrame;

public class EntityRelationMappingEditor {
	public static void main(String[] args) {
		if (!FlatLightLaf.setup()) {
			System.err.println("Could not install FlatLight Look and Feel.");
		}
		EditorFrame frame = new EditorFrame();
		frame.setVisible(true);
	}
}
