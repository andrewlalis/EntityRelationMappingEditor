package nl.andrewlalis.erme.view;

import lombok.Getter;
import nl.andrewlalis.erme.control.actions.*;
import nl.andrewlalis.erme.control.actions.edits.AddAttributeAction;
import nl.andrewlalis.erme.control.actions.edits.AddRelationAction;
import nl.andrewlalis.erme.control.actions.edits.RemoveAttributeAction;
import nl.andrewlalis.erme.control.actions.edits.RemoveRelationAction;
import nl.andrewlalis.erme.control.diagram.DiagramMouseListener;
import nl.andrewlalis.erme.model.MappingModel;
import nl.andrewlalis.erme.model.ModelChangeListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * The main panel in which the ER Mapping diagram is displayed.
 */
public class DiagramPanel extends JPanel implements ModelChangeListener {
	@Getter
	private MappingModel model;

	@Getter
	private final Point panningTranslation;

	public DiagramPanel(MappingModel model) {
		super(true);
		this.panningTranslation = new Point();
		InputMap inputMap = this.getInputMap(WHEN_IN_FOCUSED_WINDOW);
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, KeyEvent.SHIFT_DOWN_MASK), "PAN_RESET");
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, KeyEvent.CTRL_DOWN_MASK), "CENTER_MODEL");
		this.getActionMap().put("PAN_RESET", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				resetTranslation();
				repaint();
			}
		});
		this.getActionMap().put("CENTER_MODEL", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				model.normalizeRelationPositions();
				centerModel();
				repaint();
			}
		});
		this.setModel(model);
	}

	public void setModel(MappingModel newModel) {
		this.model = newModel;
		newModel.addChangeListener(this);
		for (MouseListener listener : this.getMouseListeners()) {
			this.removeMouseListener(listener);
		}
		for (MouseMotionListener listener : this.getMouseMotionListeners()) {
			this.removeMouseMotionListener(listener);
		}
		DiagramMouseListener listener = new DiagramMouseListener(newModel);
		this.addMouseListener(listener);
		this.addMouseMotionListener(listener);
		this.updateActionModels();
		this.centerModel();
		this.repaint();
	}

	public void translate(int dx, int dy) {
		this.panningTranslation.x += dx;
		this.panningTranslation.y += dy;
	}

	public void resetTranslation() {
		this.panningTranslation.x = 0;
		this.panningTranslation.y = 0;
	}

	public void centerModel() {
		if (this.getGraphics() == null) {
			return;
		}
		final Rectangle modelBounds = this.getModel().getViewModel().getBounds(this.getGraphics2D());
		final int modelCenterX = modelBounds.x + modelBounds.width / 2;
		final int modelCenterY = modelBounds.y + modelBounds.height / 2;
		final int panelCenterX = this.getWidth() / 2;
		final int panelCenterY = this.getHeight() / 2;
		this.resetTranslation();
		this.translate(panelCenterX - modelCenterX, panelCenterY - modelCenterY);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.translate(this.panningTranslation.x, this.panningTranslation.y);
		this.model.getViewModel().draw(this.getGraphics2D(g));
	}

	public Graphics2D getGraphics2D(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		prepareGraphics(g2d);
		return g2d;
	}

	public Graphics2D getGraphics2D() {
		return this.getGraphics2D(this.getGraphics());
	}

	@Override
	public void onModelChanged() {
		this.revalidate();
		this.repaint();
	}

	/**
	 * Updates all the action singletons with the latest model information.
	 */
	private void updateActionModels() {
		NewModelAction.getInstance().setDiagramPanel(this);
		SaveAction.getInstance().setModel(this.model);
		LoadAction.getInstance().setDiagramPanel(this);
		ExportToImageAction.getInstance().setModel(this.model);
		AddRelationAction.getInstance().setModel(this.model);
		AddRelationAction.getInstance().setDiagramPanel(this);
		RemoveRelationAction.getInstance().setModel(this.model);
		AddAttributeAction.getInstance().setModel(this.model);
		RemoveAttributeAction.getInstance().setModel(this.model);
		LoadSampleModelAction.getInstance().setDiagramPanel(this);
		LolcatAction.getInstance().setDiagramPanel(this);
		AutoPositionAction.getInstance().setDiagramPanel(this);
		AutoPositionAction.getInstance().setModel(this.model);
		OrderableListPanel.getInstance().setModel(this.model);
	}

	public static void prepareGraphics(Graphics2D g) {
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setFont(g.getFont().deriveFont(14.0f));
	}
}
