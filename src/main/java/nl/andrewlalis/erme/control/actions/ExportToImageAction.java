package nl.andrewlalis.erme.control.actions;

import lombok.Setter;
import nl.andrewlalis.erme.model.MappingModel;
import nl.andrewlalis.erme.model.Relation;
import nl.andrewlalis.erme.view.view_models.MappingModelViewModel;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public class ExportToImageAction extends AbstractAction {
	private static ExportToImageAction instance;

	public static ExportToImageAction getInstance() {
		if (instance == null) {
			instance = new ExportToImageAction();
		}
		return instance;
	}

	private File lastSelectedFile;

	@Setter
	private MappingModel model;

	public ExportToImageAction() {
		super("Export to Image");
		this.putValue(Action.SHORT_DESCRIPTION, "Export the current diagram to an image.");
		this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_DOWN_MASK));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (this.model.getRelations().isEmpty()) {
			JOptionPane.showMessageDialog(
					(Component) e.getSource(),
					"Model is empty. Add some relations before exporting to an image.",
					"Model Empty",
					JOptionPane.WARNING_MESSAGE
			);
			return;
		}
		JFileChooser fileChooser = new JFileChooser(this.lastSelectedFile);
		fileChooser.setFileFilter(new FileNameExtensionFilter(
				"Image files", ImageIO.getReaderFileSuffixes()
		));
		if (this.lastSelectedFile != null) {
			fileChooser.setSelectedFile(this.lastSelectedFile);
		}
		int choice = fileChooser.showSaveDialog((Component) e.getSource());
		if (choice == JFileChooser.APPROVE_OPTION) {
			File chosenFile = fileChooser.getSelectedFile();
			if (chosenFile == null || chosenFile.isDirectory()) {
				JOptionPane.showMessageDialog(fileChooser, "The selected file cannot be written to.", "Invalid File", JOptionPane.WARNING_MESSAGE);
				return;
			}
			int i = chosenFile.getName().lastIndexOf('.');
			String extension = "png";
			if (i > 0) {
				extension = chosenFile.getName().substring(i + 1);
			} else {
				chosenFile = new File(chosenFile.getParent(), chosenFile.getName() + '.' + extension);
			}
			try {
				ImageIO.write(this.renderModel(), extension, chosenFile);
			} catch (IOException ex) {
				ex.printStackTrace();
				JOptionPane.showMessageDialog(fileChooser, "An error occurred and the file could not be saved:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private BufferedImage renderModel() {
		BufferedImage bufferedImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = bufferedImage.createGraphics();
		int minX = Integer.MAX_VALUE;
		int minY = Integer.MAX_VALUE;
		int maxX = Integer.MIN_VALUE;
		int maxY = Integer.MIN_VALUE;
		for (Relation r : model.getRelations()) {
			Rectangle bounds = r.getViewModel().getBounds(g2d);
			minX = Math.min(minX, bounds.x);
			minY = Math.min(minY, bounds.y);
			maxX = Math.max(maxX, bounds.x + bounds.width);
			maxY = Math.max(maxY, bounds.y + bounds.height);
		}
		BufferedImage outputImage = new BufferedImage((maxX - minX), (maxY - minY) + 20, BufferedImage.TYPE_INT_RGB);
		g2d = outputImage.createGraphics();
		g2d.setColor(Color.WHITE);
		g2d.fillRect(outputImage.getMinX(), outputImage.getMinY(), outputImage.getWidth(), outputImage.getHeight());
		AffineTransform originalTransform = g2d.getTransform();
		g2d.setTransform(AffineTransform.getTranslateInstance(-minX, -minY));

		List<Relation> selectedRelations = this.model.getSelectedRelations();
		this.model.getSelectedRelations().forEach(r -> r.setSelected(false));
		new MappingModelViewModel(this.model).draw(g2d);
		this.model.getRelations().forEach(r -> r.setSelected(selectedRelations.contains(r)));

		g2d.setTransform(originalTransform);
		g2d.setColor(Color.LIGHT_GRAY);
		g2d.setFont(g2d.getFont().deriveFont(10.0f));
		g2d.drawString("Created by EntityRelationMappingEditor", 0, outputImage.getHeight() - 3);
		return outputImage;
	}
}
