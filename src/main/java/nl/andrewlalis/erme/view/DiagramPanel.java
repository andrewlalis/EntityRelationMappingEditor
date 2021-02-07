package nl.andrewlalis.erme.view;

import lombok.Getter;
import nl.andrewlalis.erme.control.actions.SaveAction;
import nl.andrewlalis.erme.control.diagram.DiagramMouseListener;
import nl.andrewlalis.erme.model.MappingModel;
import nl.andrewlalis.erme.model.ModelChangeListener;
import nl.andrewlalis.erme.view.view_models.MappingModelViewModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * The main panel in which the ER Mapping diagram is displayed.
 */
public class DiagramPanel extends JPanel implements ModelChangeListener {
	@Getter
	private MappingModel model;

	public DiagramPanel(MappingModel model) {
		super(true);
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
		SaveAction.getInstance().setModel(newModel);
		this.repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		new MappingModelViewModel(this.model).draw(this.getGraphics2D(g));
	}

	public Graphics2D getGraphics2D(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setFont(g.getFont().deriveFont(14.0f));
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
}
