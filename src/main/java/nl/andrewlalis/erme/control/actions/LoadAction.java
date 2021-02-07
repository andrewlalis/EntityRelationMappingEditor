package nl.andrewlalis.erme.control.actions;

import lombok.Setter;
import nl.andrewlalis.erme.model.MappingModel;
import nl.andrewlalis.erme.view.DiagramPanel;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class LoadAction extends AbstractAction {
	private static LoadAction instance;

	public static LoadAction getInstance() {
		if (instance == null) {
			instance = new LoadAction();
		}
		return instance;
	}

	private File lastSelectedFile;

	@Setter
	private DiagramPanel diagramPanel;

	public LoadAction() {
		super("Load");
		this.putValue(SHORT_DESCRIPTION, "Load a saved diagram.");
		this.putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JFileChooser fileChooser = new JFileChooser(this.lastSelectedFile);
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"ERME Serialized Files",
				"erme"
		);
		fileChooser.setFileFilter(filter);
		if (this.lastSelectedFile != null) {
			fileChooser.setSelectedFile(this.lastSelectedFile);
		}
		int choice = fileChooser.showOpenDialog((Component) e.getSource());
		if (choice == JFileChooser.APPROVE_OPTION) {
			File chosenFile = fileChooser.getSelectedFile();
			if (chosenFile == null || chosenFile.isDirectory() || !chosenFile.exists() || !chosenFile.canRead()) {
				JOptionPane.showMessageDialog(fileChooser, "The selected file cannot be read.", "Invalid File", JOptionPane.WARNING_MESSAGE);
				return;
			}
			try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(chosenFile))) {
				MappingModel loadedModel = (MappingModel) ois.readObject();
				this.lastSelectedFile = chosenFile;
				this.diagramPanel.setModel(loadedModel);
			} catch (IOException | ClassNotFoundException | ClassCastException ex) {
				ex.printStackTrace();
				JOptionPane.showMessageDialog(fileChooser, "An error occurred and the file could not be read:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}
