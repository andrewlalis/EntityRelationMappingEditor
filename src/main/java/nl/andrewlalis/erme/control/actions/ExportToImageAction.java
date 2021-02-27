package nl.andrewlalis.erme.control.actions;

import lombok.Setter;
import nl.andrewlalis.erme.model.MappingModel;
import nl.andrewlalis.erme.model.Relation;
import nl.andrewlalis.erme.view.DiagramPanel;
import nl.andrewlalis.erme.view.view_models.AttributeViewModel;
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

public class ExportToImageAction extends AbstractAction {
	private static final String LAST_EXPORT_LOCATION_KEY = "lastExportLocation";

	private static ExportToImageAction instance;
	public static ExportToImageAction getInstance() {
		if (instance == null) {
			instance = new ExportToImageAction();
		}
		return instance;
	}

	@Setter
	private MappingModel model;
	@Setter
	private DiagramPanel diagramPanel;

	public ExportToImageAction() {
		super("Export to Image");
		this.putValue(Action.SHORT_DESCRIPTION, "Export the current diagram to an image.");
		this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_DOWN_MASK));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (this.model.getRelations().isEmpty()) {
			JOptionPane.showMessageDialog(
					this.diagramPanel,
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
		int choice = fileChooser.showSaveDialog(this.diagramPanel);
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
				long start = System.currentTimeMillis();
				BufferedImage render = this.renderModel();
				double durationSeconds = (System.currentTimeMillis() - start) / 1000.0;
				ImageIO.write(render, extension, chosenFile);
				prefs.put(LAST_EXPORT_LOCATION_KEY, chosenFile.getAbsolutePath());
				JOptionPane.showMessageDialog(
						this.diagramPanel,
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

	private BufferedImage renderModel() {
		// Prepare a tiny sample image that we can use to determine the bounds of the model in a graphics context.
		BufferedImage bufferedImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = bufferedImage.createGraphics();
		DiagramPanel.prepareGraphics(g2d);
		final Rectangle bounds = this.model.getViewModel().getBounds(g2d);

		// Prepare the output image.
		BufferedImage outputImage = new BufferedImage(bounds.width, bounds.height + 20, BufferedImage.TYPE_INT_RGB);
		g2d = outputImage.createGraphics();
		g2d.setColor(Color.WHITE);
		g2d.fillRect(outputImage.getMinX(), outputImage.getMinY(), outputImage.getWidth(), outputImage.getHeight());

		// Transform the graphics space to account for the model's offset from origin.
		AffineTransform originalTransform = g2d.getTransform();
		g2d.setTransform(AffineTransform.getTranslateInstance(-bounds.x, -bounds.y));
		DiagramPanel.prepareGraphics(g2d);

		// Render the model.
		boolean lolcat = LolcatAction.getInstance().isLolcatEnabled(); // save previous lolcat mode
		LolcatAction.getInstance().setLolcatEnabled(false);
		List<Relation> selectedRelations = this.model.getSelectedRelations();
		this.model.getSelectedRelations().forEach(r -> r.setSelected(false));
		new MappingModelViewModel(this.model).draw(g2d);
		this.model.getRelations().forEach(r -> r.setSelected(selectedRelations.contains(r)));
		LolcatAction.getInstance().setLolcatEnabled(lolcat); // revert previous lolcat mode

		// Revert back to the normal image space, and render a watermark.
		g2d.setTransform(originalTransform);
		g2d.setColor(Color.LIGHT_GRAY);
		g2d.setFont(g2d.getFont().deriveFont(10.0f));
		g2d.drawString("Created by EntityRelationMappingEditor", 0, outputImage.getHeight() - 3);
		return outputImage;
	}
}
