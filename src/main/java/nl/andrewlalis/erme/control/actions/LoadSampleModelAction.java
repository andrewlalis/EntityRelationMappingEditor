package nl.andrewlalis.erme.control.actions;

import nl.andrewlalis.erme.model.Attribute;
import nl.andrewlalis.erme.model.AttributeType;
import nl.andrewlalis.erme.model.MappingModel;
import nl.andrewlalis.erme.model.Relation;
import nl.andrewlalis.erme.view.DiagramPanel;

import java.awt.*;
import java.awt.event.ActionEvent;

public class LoadSampleModelAction extends DiagramPanelAction {
	public LoadSampleModelAction(DiagramPanel diagramPanel) {
		super("Load Sample Model", diagramPanel);
		this.putValue(SHORT_DESCRIPTION, "Loads a sample ER-mapping model.");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		MappingModel model = new MappingModel();
		Relation r0 = new Relation(model, new Point(50, 20), "AirplaneType");
		r0.addAttribute(new Attribute(r0, AttributeType.ID_KEY, "name"));
		r0.addAttribute(new Attribute(r0, AttributeType.PLAIN, "manufacturer"));
		model.addRelation(r0);
		Relation r1 = new Relation(model, new Point(50, 100), "Airplane");
		r1.addAttribute(new Attribute(r1, AttributeType.ID_KEY, "id"));
		r1.addAttribute(new Attribute(r1, AttributeType.PLAIN, "purchasedAt"));
		Attribute fk = new Attribute(r1, AttributeType.PLAIN, "typeName");
		fk.setReference(model.findAttribute("AirplaneType", "name"));
		r1.addAttribute(fk);
		model.addRelation(r1);
		getDiagramPanel().setModel(model);
	}
}
