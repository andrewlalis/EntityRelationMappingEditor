package nl.andrewlalis.erme;

import com.formdev.flatlaf.FlatLightLaf;
import nl.andrewlalis.erme.util.Hash;
import nl.andrewlalis.erme.view.EditorFrame;

import java.nio.charset.StandardCharsets;

public class EntityRelationMappingEditor {
	public static final String VERSION = "1.4.0";

	public static void main(String[] args) {
		if (!FlatLightLaf.install()) {
			System.err.println("Could not install FlatLight Look and Feel.");
		}
		final boolean includeAdminActions = shouldIncludeAdminActions(args);
		if (includeAdminActions) {
			System.out.println("Admin actions have been enabled.");
		}
		EditorFrame frame = new EditorFrame(includeAdminActions);
		frame.setVisible(true);
	}

	private static boolean shouldIncludeAdminActions(String[] args) {
		if (args.length < 1) {
			return false;
		}
		byte[] pw = args[0].getBytes(StandardCharsets.UTF_8);
		return Hash.matches(pw, "admin_hash.txt");
	}
}
