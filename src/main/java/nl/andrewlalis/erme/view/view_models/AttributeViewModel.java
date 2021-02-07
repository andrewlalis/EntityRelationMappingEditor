package nl.andrewlalis.erme.view.view_models;

import nl.andrewlalis.erme.model.Attribute;
import nl.andrewlalis.erme.model.AttributeType;

import java.awt.*;
import java.awt.font.TextAttribute;
import java.awt.geom.Rectangle2D;
import java.text.AttributedString;

public class AttributeViewModel implements ViewModel {
	public static final int PADDING_X = 5;
	public static final int PADDING_Y = 5;
	public static final Color BACKGROUND_COLOR = Color.LIGHT_GRAY;
	public static final Color FONT_COLOR = Color.BLACK;

	private final Attribute attribute;

	public AttributeViewModel(Attribute attribute) {
		this.attribute = attribute;
	}

	@Override
	public void draw(Graphics2D g) {
		AttributedString as = this.getAttributedString(g);
		Rectangle r = this.getBounds(g, as);
		g.setColor(BACKGROUND_COLOR);
		g.fillRect(r.x, r.y, r.width, r.height);
		g.setColor(FONT_COLOR);
		g.drawRect(r.x, r.y, r.width, r.height);
		g.drawString(as.getIterator(), r.x + PADDING_X, r.y + (r.height - PADDING_Y));
	}

	private Rectangle getBounds(Graphics2D g, AttributedString as) {
		int x = this.attribute.getRelation().getPosition().x + RelationViewModel.PADDING_X;
		int y = this.attribute.getRelation().getPosition().y + this.attribute.getRelation().getViewModel().getNameBounds(g).height + PADDING_Y;
		int i = 0;
		while (!this.attribute.getRelation().getAttributes().get(i).equals(this.attribute)) {
			x += g.getFontMetrics().stringWidth(this.attribute.getRelation().getAttributes().get(i).getName()) + (2 * PADDING_X);
			i++;
		}
		Rectangle2D rect = g.getFontMetrics().getStringBounds(as.getIterator(), 0, this.attribute.getName().length(), g);
		return new Rectangle(
				x,
				y,
				(int) rect.getWidth() + (2 * PADDING_X),
				(int) rect.getHeight() + (2 * PADDING_Y)
		);
	}

	public Rectangle getBounds(Graphics2D g) {
		return this.getBounds(g, this.getAttributedString(g));
	}

	private AttributedString getAttributedString(Graphics2D g) {
		AttributedString as = new AttributedString(this.attribute.getName());
		as.addAttribute(TextAttribute.FONT, g.getFont());
		if (this.attribute.getType().equals(AttributeType.ID_KEY)) {
			as.addAttribute(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
		} else if (this.attribute.getType().equals(AttributeType.PARTIAL_ID_KEY)) {
			as.addAttribute(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_LOW_DASHED);
		}
		return as;
	}
}
