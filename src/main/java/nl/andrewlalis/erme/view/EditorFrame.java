package nl.andrewlalis.erme.view;

import nl.andrewlalis.erme.model.MappingModel;

import javax.swing.*;
import java.awt.*;

/**
 * The main JFrame for the editor.
 */
public class EditorFrame extends JFrame {
	public EditorFrame(boolean includeAdminActions) {
		super("ER-Mapping Editor");
		this.setContentPane(new DiagramPanel(new MappingModel()));
		this.setJMenuBar(new EditorMenuBar(includeAdminActions));
		this.setMinimumSize(new Dimension(400, 400));
		this.setPreferredSize(new Dimension(800, 800));
		this.pack();
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
	}
}
