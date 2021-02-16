package nl.andrewlalis.erme.view.view_models;

import java.awt.*;

public interface ViewModel {
	void draw(Graphics2D g);

	Rectangle getBounds(Graphics2D g);
}
