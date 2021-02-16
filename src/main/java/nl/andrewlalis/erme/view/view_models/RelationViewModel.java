package nl.andrewlalis.erme.view.view_models;

import nl.andrewlalis.erme.model.Attribute;
import nl.andrewlalis.erme.model.Relation;

import java.awt.*;
import java.awt.font.TextAttribute;
import java.awt.geom.Rectangle2D;
import java.text.AttributedString;

/**
 * View model which handles rendering a single relation.
 */
public class RelationViewModel implements ViewModel {
	/**
	 * Padding that should be added to the left and right of the true bounds.
	 */
	public static final int PADDING_X = 5;

	/**
	 * Padding that should be added to the top and bottom of the true bounds.
	 */
	public static final int PADDING_Y = 5;

	/**
	 * The space between the relation's name and any attributes.
	 */
	public static final int ATTRIBUTE_SEPARATION = 10;

	public static final Color SELECTED_COLOR = new Color(204, 224, 255);
	public static final Color NAME_COLOR = Color.BLACK;

	private final Relation relation;

	public RelationViewModel(Relation relation) {
		this.relation = relation;
	}

	@Override
	public void draw(Graphics2D g) {
		AttributedString as = this.getAttributedString(g);
		Rectangle bounds = this.getBounds(g);
		if (this.relation.isSelected()) {
			g.setColor(SELECTED_COLOR);
			g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
		}
		g.setColor(NAME_COLOR);
		g.drawString(as.getIterator(), bounds.x + PADDING_X, bounds.y + this.getNameBounds(g).height - PADDING_Y);
		for (Attribute a : this.relation.getAttributes()) {
			a.getViewModel().draw(g);
		}
	}

	/**
	 * Obtains the bounding box for the relation that'll be drawn. The bounding
	 * box contains the name of the relation and any attributes in it.
	 * @param g The graphics context.
	 * @return A rectangle describing the bounding box.
	 */
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
		rect.height = nameBounds.height + maxAttributeHeight + (2 * PADDING_Y) + ATTRIBUTE_SEPARATION;
		return rect;
	}

	/**
	 * Gets the bounding box around the relation's name, according to the font,
	 * font size, weight, etc.
	 * @param g The graphics context.
	 * @return A rectangle describing the name's bounding box.
	 */
	public Rectangle getNameBounds(Graphics2D g) {
		AttributedString as = this.getAttributedString(g);
		Rectangle2D nameBounds = g.getFontMetrics().getStringBounds(as.getIterator(), 0, this.relation.getName().length(), g);
		return nameBounds.getBounds();
	}

	/**
	 * Gets an instance of AttributedString that can be used for display and
	 * reference purposes.
	 * @param g The graphics context.
	 * @return The attributed string, with all necessary attributes set.
	 */
	private AttributedString getAttributedString(Graphics2D g) {
		AttributedString as = new AttributedString(this.relation.getName());
		as.addAttribute(TextAttribute.FONT, g.getFont());
		as.addAttribute(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
		return as;
	}
}
