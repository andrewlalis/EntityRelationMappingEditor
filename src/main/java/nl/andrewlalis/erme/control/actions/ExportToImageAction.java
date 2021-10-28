package nl.andrewlalis.erme.control.actions;

import nl.andrewlalis.erme.model.MappingModel;
import nl.andrewlalis.erme.model.Relation;
import nl.andrewlalis.erme.view.DiagramPanel;
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
import java.util.prefs.Preferences;

public class ExportToImageAction extends DiagramPanelAction {
	private static final String LAST_EXPORT_LOCATION_KEY = "lastExportLocation";

	public ExportToImageAction(DiagramPanel diagramPanel) {
		super("Export to Image", diagramPanel);
		this.putValue(Action.SHORT_DESCRIPTION, "Export the current diagram to an image.");
		this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_DOWN_MASK));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		DiagramPanel dp = getDiagramPanel();
		if (dp.getModel().getRelations().isEmpty()) {
			JOptionPane.showMessageDialog(
					dp,
					"Model is empty. Add some relations before exporting to an image.",
					"Model Empty",
					JOptionPane.WARNING_MESSAGE
			);
			return;
		}
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileFilter(new FileNameExtensionFilter(
				"Image files", ImageIO.getReaderFileSuffixes()
		));
		Preferences prefs = Preferences.userNodeForPackage(ExportToImageAction.class);
		String path = prefs.get(LAST_EXPORT_LOCATION_KEY, null);
		if (path != null) {
			fileChooser.setSelectedFile(new File(path));
		}
		int choice = fileChooser.showSaveDialog(dp);
		if (choice == JFileChooser.APPROVE_OPTION) {
			File chosenFile = fileChooser.getSelectedFile();
			if (chosenFile == null || chosenFile.isDirectory()) {
				JOptionPane.showMessageDialog(dp, "The selected file cannot be written to.", "Invalid File", JOptionPane.WARNING_MESSAGE);
				return;
			}
			int i = chosenFile.getName().lastIndexOf('.');
			String extension = "png";
			if (i > 0) {
				extension = chosenFile.getName().substring(i + 1);
			} else {
				chosenFile = new File(chosenFile.getParent(), chosenFile.getName() + '.' + extension);
			}
			String input = JOptionPane.showInputDialog(dp, "Choose a scale for the image.", "3.0");
			float scale;
			try {
				scale = Float.parseFloat(input);
				if (scale <= 0.0f || scale > 64.0f) throw new IllegalArgumentException();
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(dp, "Invalid scale value. Should be a positive number less than 64.", "Invalid Scale", JOptionPane.WARNING_MESSAGE);
				return;
			}
			try {
				long start = System.currentTimeMillis();
				BufferedImage render = this.renderModel(scale);
				double durationSeconds = (System.currentTimeMillis() - start) / 1000.0;
				ImageIO.write(render, extension, chosenFile);
				prefs.put(LAST_EXPORT_LOCATION_KEY, chosenFile.getAbsolutePath());
				JOptionPane.showMessageDialog(
						dp,
						"Image export completed in " + String.format("%.4f", durationSeconds) + " seconds.\n" +
								"Resolution: " + render.getWidth() + "x" + render.getHeight(),
						"Image Export Complete",
						JOptionPane.INFORMATION_MESSAGE
				);
			} catch (IOException ex) {
				ex.printStackTrace();
				JOptionPane.showMessageDialog(fileChooser, "An error occurred and the file could not be saved:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	/**
	 * Renders the mapping model to an image with the given resolution.
	 * @param scale The scale to use. Should be greater than zero.
	 * @return The image which was rendered.
	 */
	private BufferedImage renderModel(float scale) {
		MappingModel model = getDiagramPanel().getModel();
		// Prepare a tiny sample image that we can use to determine the bounds of the model in a graphics context.
		BufferedImage bufferedImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = bufferedImage.createGraphics();
		DiagramPanel.prepareGraphics(g2d);
		final Rectangle bounds = model.getViewModel().getBounds(g2d);
		bounds.width *= scale;
		bounds.height *= scale;

		// Prepare the output image.
		BufferedImage outputImage = new BufferedImage(bounds.width, bounds.height + 20, BufferedImage.TYPE_INT_RGB);
		g2d = outputImage.createGraphics();
		g2d.setColor(Color.WHITE);
		g2d.fillRect(outputImage.getMinX(), outputImage.getMinY(), outputImage.getWidth(), outputImage.getHeight());

		// Transform the graphics space to account for the model's offset from origin.
		AffineTransform originalTransform = g2d.getTransform();
		AffineTransform modelTransform = new AffineTransform();
		modelTransform.scale(scale, scale);
		modelTransform.translate(-bounds.x, -bounds.y);
		g2d.setTransform(modelTransform);
		DiagramPanel.prepareGraphics(g2d);

		// Render the model.
		boolean lolcat = model.isLolcatEnabled(); // save previous lolcat mode
		model.setLolcatEnabled(false);
		List<Relation> selectedRelations = model.getSelectedRelations();
		model.getSelectedRelations().forEach(r -> r.setSelected(false));
		new MappingModelViewModel(model).draw(g2d);
		model.getRelations().forEach(r -> r.setSelected(selectedRelations.contains(r)));
		model.setLolcatEnabled(lolcat); // revert previous lolcat mode

		// Revert to the normal image space, and render a watermark.
		g2d.setTransform(originalTransform);
		g2d.setColor(Color.decode("#e8e8e8"));
		g2d.setFont(g2d.getFont().deriveFont(10.0f));
		g2d.drawString("Created by EntityRelationMappingEditor", 0, outputImage.getHeight() - 3);
		return outputImage;
	}
}
