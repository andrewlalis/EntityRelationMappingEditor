package nl.andrewlalis.erme.control.actions;

import lombok.Setter;
import nl.andrewlalis.erme.model.MappingModel;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
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

	public SaveAction() {
		super("Save");
		this.putValue(SHORT_DESCRIPTION, "Save the current diagram to a file.");
		this.putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JFileChooser fileChooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"ERME Serialized Files",
				"erme"
		);
		fileChooser.setFileFilter(filter);
		Preferences prefs = Preferences.userNodeForPackage(SaveAction.class);
		String path = prefs.get(LAST_SAVE_LOCATION_KEY, null);
		if (path != null) {
			fileChooser.setSelectedFile(new File(path));
		}
		int choice = fileChooser.showSaveDialog((Component) e.getSource());
		if (choice == JFileChooser.APPROVE_OPTION) {
			File chosenFile = fileChooser.getSelectedFile();
			if (chosenFile == null || chosenFile.isDirectory()) {
				JOptionPane.showMessageDialog(fileChooser, "The selected file cannot be written to.", "Invalid File", JOptionPane.WARNING_MESSAGE);
				return;
			}
			if (!chosenFile.exists() && !chosenFile.getName().endsWith(".erme")) {
				chosenFile = new File(chosenFile.getParent(), chosenFile.getName() + ".erme");
			}
			// TODO: Check for confirm before overwriting.
			try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(chosenFile))) {
				oos.writeObject(this.model);
				prefs.put(LAST_SAVE_LOCATION_KEY, chosenFile.getAbsolutePath());
				JOptionPane.showMessageDialog(fileChooser, "File saved successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
			} catch (IOException ex) {
				ex.printStackTrace();
				JOptionPane.showMessageDialog(fileChooser, "An error occurred and the file could not be saved:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}
