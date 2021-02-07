package nl.andrewlalis.erme.view.view_models;

import nl.andrewlalis.erme.model.Attribute;
import nl.andrewlalis.erme.model.Relation;

import java.awt.*;
import java.awt.font.TextAttribute;
import java.awt.geom.Rectangle2D;
import java.text.AttributedString;

public class RelationViewModel implements ViewModel {
	public static final int PADDING_X = 5;
	public static final int PADDING_Y = 5;

	private final Relation relation;

	public RelationViewModel(Relation relation) {
		this.relation = relation;
	}

	@Override
	public void draw(Graphics2D g) {
		AttributedString as = this.getAttributedString(g);
		Rectangle bounds = this.getBounds(g);
		g.setColor(Color.BLACK);
		g.drawString(as.getIterator(), bounds.x + PADDING_X, bounds.y + this.getNameBounds(g).height - PADDING_Y);
		for (Attribute a : this.relation.getAttributes()) {
			a.getViewModel().draw(g);
		}
		if (this.relation.isSelected()) {
			g.setColor(Color.BLUE);
			g.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);
		}
	}

	public Rectangle getBounds(Graphics2D g) {
		Rectangle rect = new Rectangle();
		rect.x = this.relation.getPosition().x;
		rect.y = this.relation.getPosition().y;
		int totalAttributeWidth = 0;
		int maxAttributeHeight = 0;
		for (Attribute a : this.relation.getAttributes()) {
			Rectangle attributeBounds = a.getViewModel().getBounds(g);
			totalAttributeWidth += attributeBounds.width;
			maxAttributeHeight = Math.max(maxAttributeHeight, attributeBounds.height);
		}
		Rectangle nameBounds = this.getNameBounds(g);
		rect.width = Math.max(totalAttributeWidth, nameBounds.width) + (2 * PADDING_X);
		rect.height = nameBounds.height + maxAttributeHeight + (2 * PADDING_Y);
		return rect;
	}

	public Rectangle getNameBounds(Graphics2D g) {
		AttributedString as = this.getAttributedString(g);
		Rectangle2D nameBounds = g.getFontMetrics().getStringBounds(as.getIterator(), 0, this.relation.getName().length(), g);
		return nameBounds.getBounds();
	}

	private AttributedString getAttributedString(Graphics2D g) {
		AttributedString as = new AttributedString(this.relation.getName());
		as.addAttribute(TextAttribute.FONT, g.getFont());
		as.addAttribute(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
		return as;
	}
}
