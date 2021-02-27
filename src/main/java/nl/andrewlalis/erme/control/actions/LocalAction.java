package nl.andrewlalis.erme.control.actions;

import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;

public abstract class LocalAction extends AbstractAction {
	@Setter
	@Getter
	private Point location;

	public LocalAction(String name, Point location) {
		super(name);
		this.location = location;
	}

	public LocalAction(String name) {
		this(name, null);
	}

	public boolean hasLocation() {
		return this.location != null;
	}
}
