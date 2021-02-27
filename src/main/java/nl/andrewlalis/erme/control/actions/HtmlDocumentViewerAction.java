package nl.andrewlalis.erme.control.actions;

import lombok.Setter;
import nl.andrewlalis.erme.view.DiagramPanel;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;

public abstract class HtmlDocumentViewerAction extends AbstractAction {
	private final String resourceFileName;
	private final Dialog.ModalityType modalityType;

	public HtmlDocumentViewerAction(String name, String resourceFileName) {
		this(name, resourceFileName, Dialog.ModalityType.APPLICATION_MODAL);
	}

	public HtmlDocumentViewerAction(String name, String resourceFileName, Dialog.ModalityType modalityType) {
		super(name);
		this.resourceFileName = resourceFileName;
		this.modalityType = modalityType;
	}

	@Setter
	private DiagramPanel diagramPanel;

	@Override
	public void actionPerformed(ActionEvent e) {
		JDialog dialog = new JDialog(
				SwingUtilities.getWindowAncestor(this.diagramPanel),
				(String) this.getValue(NAME),
				this.modalityType
		);
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
					this.diagramPanel,
					"An error occured:\n" + ex.getMessage(),
					"Error",
					JOptionPane.ERROR_MESSAGE
			);
			textPane.setContentType("text/plain");
			textPane.setText("Unable to load content.");
		}
		textPane.setCaretPosition(0);
		JScrollPane scrollPane = new JScrollPane(textPane, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		dialog.setContentPane(scrollPane);
		dialog.setMaximumSize(new Dimension(600, 800));
		dialog.setPreferredSize(new Dimension(600, 800));
		dialog.pack();
		dialog.setLocationRelativeTo(this.diagramPanel);
		dialog.setVisible(true);
	}

	private String readFile() throws IOException {
		InputStream is = getClass().getClassLoader().getResourceAsStream(this.resourceFileName);
		if (is == null) {
			throw new IOException("Could not get stream for " + this.resourceFileName);
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
