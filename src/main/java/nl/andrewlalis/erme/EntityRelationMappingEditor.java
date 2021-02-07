package nl.andrewlalis.erme;

import com.formdev.flatlaf.FlatLightLaf;
import nl.andrewlalis.erme.view.EditorFrame;

public class EntityRelationMappingEditor {

	public static void main(String[] args) {
		if (!FlatLightLaf.install()) {
			System.err.println("Could not install FlatLight Look and Feel.");
		}
		final EditorFrame frame = new EditorFrame();
		frame.setVisible(true);
	}
}
