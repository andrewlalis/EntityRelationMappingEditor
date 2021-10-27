package nl.andrewlalis.erme.control.actions;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.Setter;
import nl.andrewlalis.erme.model.MappingModel;
import nl.andrewlalis.erme.view.DiagramPanel;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.prefs.Preferences;

public class SaveAction extends AbstractAction {
	private static final String LAST_SAVE_LOCATION_KEY = "lastSaveLocation";

	private static SaveAction instance;
	public static SaveAction getInstance() {
		if (instance == null) {
			instance = new SaveAction();
		}
		return instance;
	}

	@Setter
	private MappingModel model;

	@Setter
	private DiagramPanel diagramPanel;

	public SaveAction() {
		super("Save");
		this.putValue(SHORT_DESCRIPTION, "Save the current diagram to a file.");
		this.putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JFileChooser fileChooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"JSON Files",
				"json"
		);
		fileChooser.setFileFilter(filter);
		Preferences prefs = Preferences.userNodeForPackage(SaveAction.class);
		String path = prefs.get(LAST_SAVE_LOCATION_KEY, null);
		if (path != null) {
			fileChooser.setSelectedFile(new File(path));
		}
		int choice = fileChooser.showSaveDialog(this.diagramPanel);
		if (choice == JFileChooser.APPROVE_OPTION) {
			File chosenFile = fileChooser.getSelectedFile();
			if (chosenFile == null || chosenFile.isDirectory()) {
				JOptionPane.showMessageDialog(this.diagramPanel, "The selected file cannot be written to.", "Invalid File", JOptionPane.WARNING_MESSAGE);
				return;
			}
			if (!chosenFile.exists() && !chosenFile.getName().endsWith(".json")) {
				chosenFile = new File(chosenFile.getParent(), chosenFile.getName() + ".json");
			} else if (chosenFile.exists()) {
				int result = JOptionPane.showConfirmDialog(this.diagramPanel, "Are you sure you want overwrite this file?", "Overwrite", JOptionPane.YES_NO_OPTION);
				if (result == JOptionPane.NO_OPTION) {
					return;
				}
			}
			try (FileOutputStream fos = new FileOutputStream(chosenFile)) {
				ObjectMapper mapper = JsonMapper.builder()
						.configure(SerializationFeature.INDENT_OUTPUT, true)
						.configure(MapperFeature.PROPAGATE_TRANSIENT_MARKER, true)
						.build();
				mapper.writeValue(fos, this.model.toJson(mapper));
				prefs.put(LAST_SAVE_LOCATION_KEY, chosenFile.getAbsolutePath());
				JOptionPane.showMessageDialog(fileChooser, "File saved successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
			} catch (IOException ex) {
				ex.printStackTrace();
				JOptionPane.showMessageDialog(fileChooser, "An error occurred and the file could not be saved:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}
