package nl.andrewlalis.erme.control.actions;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;

public class InstructionsAction extends AbstractAction {
	private static InstructionsAction instance;

	public static InstructionsAction getInstance() {
		if (instance == null) instance = new InstructionsAction();
		return instance;
	}

	public InstructionsAction() {
		super("Instructions");
		this.putValue(SHORT_DESCRIPTION, "Instructions for how to use this program.");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor((Component) e.getSource()), "Instructions", Dialog.ModalityType.APPLICATION_MODAL);
		JTextPane textPane = new JTextPane();
		textPane.setEditable(false);
		textPane.setContentType("text/html");
		try {
			textPane.setText(this.readFile());
			textPane.addHyperlinkListener(event -> {
				if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
					if (!Desktop.isDesktopSupported()) {
						JOptionPane.showMessageDialog(dialog, "Desktop API not supported. You may still visit the link manually:\n" + event.getURL(), "Desktop API Not Supported", JOptionPane.WARNING_MESSAGE);
					} else {
						Desktop desktop = Desktop.getDesktop();
						try {
							desktop.browse(event.getURL().toURI());
						} catch (IOException | URISyntaxException ex) {
							ex.printStackTrace();
							JOptionPane.showMessageDialog(dialog, "An error occurred and the URL could not be opened:\n" + event.getURL(), "URL Could Not Open", JOptionPane.ERROR_MESSAGE);
						}
					}
				}
			});
		} catch (IOException ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(
					(Component) e.getSource(),
					"An error occured:\n" + ex.getMessage(),
					"Error",
					JOptionPane.ERROR_MESSAGE
			);
			textPane.setContentType("text/plain");
			textPane.setText("Unable to load content.");
		}
		JScrollPane scrollPane = new JScrollPane(textPane, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		dialog.setContentPane(scrollPane);
		dialog.setMaximumSize(new Dimension(600, 800));
		dialog.setPreferredSize(new Dimension(600, 800));
		dialog.pack();
		dialog.setLocationRelativeTo(null);
		dialog.setVisible(true);
	}

	private String readFile() throws IOException {
		InputStream is = getClass().getClassLoader().getResourceAsStream("html/instructions.html");
		if (is == null) {
			throw new IOException("Could not get stream for instructions.html.");
		}
		StringBuilder sb = new StringBuilder();
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
			String line = reader.readLine();
			while (line != null) {
				sb.append(line).append('\n');
				line = reader.readLine();
			}
		}
		return sb.toString();
	}
}
