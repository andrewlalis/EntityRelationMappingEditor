package nl.andrewlalis.erme.view;

import nl.andrewlalis.erme.model.MappingModel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

/**
 * The main JFrame for the editor.
 */
public class EditorFrame extends JFrame {
	public EditorFrame() {
		super("ER-Mapping Editor");
		this.setContentPane(new DiagramPanel(new MappingModel()));
		this.setJMenuBar(new EditorMenuBar());
		try {
			InputStream is = getClass().getClassLoader().getResourceAsStream("icon.png");
			if (is == null) {
				System.err.println("Could not load application icon.");
			} else {
				this.setIconImage(ImageIO.read(is));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.setMinimumSize(new Dimension(400, 400));
		this.setPreferredSize(new Dimension(800, 800));
		this.pack();
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
	}
}
