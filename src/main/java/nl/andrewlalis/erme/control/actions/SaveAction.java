package nl.andrewlalis.erme.control.actions;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
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

public class SaveAction extends DiagramPanelAction {
	private static final String LAST_SAVE_LOCATION_KEY = "lastSaveLocation";

	public SaveAction(DiagramPanel diagramPanel) {
		super("Save", diagramPanel);
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
		DiagramPanel dp = getDiagramPanel();
		int choice = fileChooser.showSaveDialog(dp);
		if (choice == JFileChooser.APPROVE_OPTION) {
			File chosenFile = fileChooser.getSelectedFile();
			if (chosenFile == null || chosenFile.isDirectory()) {
				JOptionPane.showMessageDialog(dp, "The selected file cannot be written to.", "Invalid File", JOptionPane.WARNING_MESSAGE);
				return;
			}
			if (!chosenFile.exists() && !chosenFile.getName().endsWith(".json")) {
				chosenFile = new File(chosenFile.getParent(), chosenFile.getName() + ".json");
			} else if (chosenFile.exists()) {
				int result = JOptionPane.showConfirmDialog(dp, "Are you sure you want overwrite this file?", "Overwrite", JOptionPane.YES_NO_OPTION);
				if (result == JOptionPane.NO_OPTION) {
					return;
				}
			}
			try (FileOutputStream fos = new FileOutputStream(chosenFile)) {
				ObjectMapper mapper = JsonMapper.builder()
						.configure(SerializationFeature.INDENT_OUTPUT, true)
						.configure(MapperFeature.PROPAGATE_TRANSIENT_MARKER, true)
						.build();
				mapper.writeValue(fos, dp.getModel().toJson(mapper));
				prefs.put(LAST_SAVE_LOCATION_KEY, chosenFile.getAbsolutePath());
				JOptionPane.showMessageDialog(dp, "File saved successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
			} catch (IOException ex) {
				ex.printStackTrace();
				JOptionPane.showMessageDialog(dp, "An error occurred and the file could not be saved:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}
