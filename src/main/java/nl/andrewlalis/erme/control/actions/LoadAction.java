package nl.andrewlalis.erme.control.actions;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Setter;
import nl.andrewlalis.erme.model.MappingModel;
import nl.andrewlalis.erme.view.DiagramPanel;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.util.prefs.Preferences;

public class LoadAction extends AbstractAction {
	private static final String LAST_LOAD_LOCATION_KEY = "lastLoadLocation";

	private static LoadAction instance;
	public static LoadAction getInstance() {
		if (instance == null) {
			instance = new LoadAction();
		}
		return instance;
	}

	@Setter
	private DiagramPanel diagramPanel;

	public LoadAction() {
		super("Load");
		this.putValue(SHORT_DESCRIPTION, "Load a saved diagram.");
		this.putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JFileChooser fileChooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"JSON Files",
				"json"
		);
		fileChooser.setFileFilter(filter);
		Preferences prefs = Preferences.userNodeForPackage(LoadAction.class);
		String path = prefs.get(LAST_LOAD_LOCATION_KEY, null);
		if (path != null) {
			fileChooser.setSelectedFile(new File(path));
		}
		int choice = fileChooser.showOpenDialog(this.diagramPanel);
		if (choice == JFileChooser.APPROVE_OPTION) {
			File chosenFile = fileChooser.getSelectedFile();
			if (chosenFile == null || chosenFile.isDirectory() || !chosenFile.exists() || !chosenFile.canRead()) {
				JOptionPane.showMessageDialog(fileChooser, "The selected file cannot be read.", "Invalid File", JOptionPane.WARNING_MESSAGE);
				return;
			}
			try (FileInputStream fis = new FileInputStream(chosenFile)) {
				ObjectMapper mapper = JsonMapper.builder()
						.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
						.configure(MapperFeature.PROPAGATE_TRANSIENT_MARKER, true)
						.build();
				JsonNode data = mapper.readValue(fis, JsonNode.class);
				this.diagramPanel.setModel(MappingModel.fromJson((ObjectNode) data));
				prefs.put(LAST_LOAD_LOCATION_KEY, chosenFile.getAbsolutePath());
			} catch (Exception ex) {
				ex.printStackTrace();
				JOptionPane.showMessageDialog(fileChooser, "An error occurred and the file could not be read:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}
