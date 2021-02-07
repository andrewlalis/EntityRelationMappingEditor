package nl.andrewlalis.erme.view;

import nl.andrewlalis.erme.model.*;

import javax.swing.*;
import java.awt.*;

/**
 * The main JFrame for the editor.
 */
public class EditorFrame extends JFrame {
	public EditorFrame() {
		super("ER-Mapping Editor");
		MappingModel model = new MappingModel();
		Relation usersRelation = new Relation(model, new Point(50, 50), "Users");
		usersRelation.addAttribute(new Attribute(usersRelation, AttributeType.ID_KEY, "username"));
		usersRelation.addAttribute(new Attribute(usersRelation, AttributeType.PLAIN, "fullName"));
		usersRelation.addAttribute(new Attribute(usersRelation, AttributeType.PLAIN, "language"));
		usersRelation.addAttribute(new Attribute(usersRelation, AttributeType.PLAIN, "verified"));
		usersRelation.addAttribute(new Attribute(usersRelation, AttributeType.PARTIAL_ID_KEY, "partialKey"));
		model.addRelation(usersRelation);
		Relation tokensRelation = new Relation(model, new Point(50, 120), "Tokens");
		tokensRelation.addAttribute(new Attribute(tokensRelation, AttributeType.ID_KEY, "tokenCode"));
		tokensRelation.addAttribute(new ForeignKeyAttribute(tokensRelation,"username", "Users", "username"));
		tokensRelation.addAttribute(new Attribute(tokensRelation, AttributeType.PLAIN, "expirationDate"));
		model.addRelation(tokensRelation);

		this.setContentPane(new DiagramPanel(model));
		this.setJMenuBar(new EditorMenuBar());
		this.setMinimumSize(new Dimension(500, 500));
		this.pack();
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setLocationRelativeTo(null);
	}
}
