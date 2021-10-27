package nl.andrewlalis.erme.view.view_models;

import nl.andrewlalis.erme.control.actions.LolcatAction;
import nl.andrewlalis.erme.model.Attribute;
import nl.andrewlalis.erme.model.AttributeType;
import nl.andrewlalis.erme.model.ForeignKeyAttribute;

import java.awt.*;
import java.awt.font.TextAttribute;
import java.awt.geom.Rectangle2D;
import java.text.AttributedString;

/**
 * View model for rendering a single attribute of a relation.
 */
public class AttributeViewModel implements ViewModel {
	public static final int PADDING_X = 5;
	public static final int PADDING_Y = 5;
	public static final Color BACKGROUND_COLOR = new Color(192, 192, 192, 127);
	public static final Color FONT_COLOR = Color.BLACK;
	public static final float FK_FONT_SIZE = 11.0f;
	private static final float LOLCAT_SAT = 0.75f;
	private static final float LOLCAT_BRIGHT = 1f;
	private final Attribute attribute;

	public AttributeViewModel(Attribute attribute) {
		this.attribute = attribute;
	}

	@Override
	public void draw(Graphics2D g) {
		AttributedString as = this.getAttributedString(g);
		Rectangle r = this.getBounds(g, as);
		g.setColor(this.getBackgroundColor(r.x + r.width / 2, r.y + r.height / 2, g));
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

	private Color getBackgroundColor(int x, int y, Graphics2D g) {
		if (!LolcatAction.getInstance().isLolcatEnabled()) return BACKGROUND_COLOR;
		Point offset = g.getClipBounds().getLocation();
		g.translate(offset.x, offset.y);
		Dimension viewportSize = g.getClipBounds().getSize();

		double diagonal_slope = (double) viewportSize.width / (double) viewportSize.height;
		double perp_slope = -1f / diagonal_slope;

		double perp_offset = y - perp_slope * x;

		double x_intersect = perp_offset / (diagonal_slope - perp_slope);
		double y_intersect = diagonal_slope * (perp_offset / (diagonal_slope - perp_slope));

		double total_dist = Math.sqrt(viewportSize.height * viewportSize.height + viewportSize.width * viewportSize.width);
		double dist_frac = Math.sqrt(x_intersect * x_intersect + y_intersect * y_intersect) / total_dist;

		g.translate(-offset.x, -offset.y);
		return Color.getHSBColor((float) dist_frac, LOLCAT_SAT, LOLCAT_BRIGHT);
	}

	@Override
	public Rectangle getBounds(Graphics2D g) {
		return this.getBounds(g, this.getAttributedString(g));
	}

	private Rectangle getBounds(Graphics2D g, AttributedString as) {
		final RelationViewModel relationViewModel = (RelationViewModel) this.attribute.getRelation().getViewModel();
		int x = this.attribute.getRelation().getPosition().x + RelationViewModel.PADDING_X;
		int y = this.attribute.getRelation().getPosition().y + relationViewModel.getNameBounds(g).height + RelationViewModel.ATTRIBUTE_SEPARATION;
		int i = 0;
		while (!this.attribute.getRelation().getAttributes().get(i).equals(this.attribute)) {
			x += this.attribute.getRelation().getAttributes().get(i).getViewModel().getBounds(g).width;
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
