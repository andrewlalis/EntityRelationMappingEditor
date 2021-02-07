package nl.andrewlalis.erme.view.view_models;

import nl.andrewlalis.erme.model.Attribute;
import nl.andrewlalis.erme.model.AttributeType;
import nl.andrewlalis.erme.model.ForeignKeyAttribute;

import java.awt.*;
import java.awt.font.TextAttribute;
import java.awt.geom.Rectangle2D;
import java.text.AttributedString;

public class AttributeViewModel implements ViewModel {
	public static final int PADDING_X = 5;
	public static final int PADDING_Y = 5;
	public static final Color BACKGROUND_COLOR = Color.LIGHT_GRAY;
	public static final Color FONT_COLOR = Color.BLACK;
	public static final float FK_FONT_SIZE = 11.0f;

	private final Attribute attribute;

	public AttributeViewModel(Attribute attribute) {
		this.attribute = attribute;
	}

	@Override
	public void draw(Graphics2D g) {
		AttributedString as = this.getAttributedString(g);
		Rectangle r = this.getBoxBounds(g, as);
		g.setColor(BACKGROUND_COLOR);
		g.fillRect(r.x, r.y, r.width, r.height);
		g.setColor(FONT_COLOR);
		g.drawRect(r.x, r.y, r.width, r.height);
		g.drawString(as.getIterator(), r.x + PADDING_X, r.y + (r.height - PADDING_Y));
		if (this.attribute instanceof ForeignKeyAttribute) {
			ForeignKeyAttribute fkAttribute = (ForeignKeyAttribute) this.attribute;
			Font originalFont = g.getFont();
			g.setFont(g.getFont().deriveFont(Font.ITALIC, FK_FONT_SIZE));
			g.drawString(fkAttribute.getFullReferenceName(), r.x + PADDING_X, r.y - PADDING_Y);
			g.setFont(originalFont);
		}
	}

	private Rectangle getBoxBounds(Graphics2D g, AttributedString as) {
		int x = this.attribute.getRelation().getPosition().x + RelationViewModel.PADDING_X;
		int y = this.attribute.getRelation().getPosition().y + this.attribute.getRelation().getViewModel().getNameBounds(g).height + RelationViewModel.ATTRIBUTE_SEPARATION;
		int i = 0;
		while (!this.attribute.getRelation().getAttributes().get(i).equals(this.attribute)) {
			x += this.attribute.getRelation().getAttributes().get(i).getViewModel().getBoxBounds(g).width;
			i++;
		}
		Rectangle2D nameRect = g.getFontMetrics().getStringBounds(as.getIterator(), 0, this.attribute.getName().length(), g);
		int width = (int) nameRect.getWidth() + (2 * PADDING_X);
		int height = (int) nameRect.getHeight() + (2 * PADDING_Y);
		if (this.attribute instanceof ForeignKeyAttribute) {
			ForeignKeyAttribute fkAttribute = (ForeignKeyAttribute) this.attribute;
			Font originalFont = g.getFont();
			g.setFont(g.getFont().deriveFont(Font.ITALIC, FK_FONT_SIZE));
			Rectangle referenceNameBounds = g.getFontMetrics().getStringBounds(fkAttribute.getFullReferenceName(), g).getBounds();
			g.setFont(originalFont);
			width = Math.max(width, referenceNameBounds.width + (2 * PADDING_X));
		}
		return new Rectangle(x, y, width, height);
	}

	public Rectangle getBoxBounds(Graphics2D g) {
		return this.getBoxBounds(g, this.getAttributedString(g));
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
