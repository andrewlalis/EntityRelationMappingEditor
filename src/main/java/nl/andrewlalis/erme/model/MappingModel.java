package nl.andrewlalis.erme.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import nl.andrewlalis.erme.view.view_models.MappingModelViewModel;
import nl.andrewlalis.erme.view.view_models.ViewModel;

import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This model contains all the information about a single mapping diagram,
 * including each mapped table and the links between them.
 */
public class MappingModel implements Viewable {
	@Getter
	private final Set<Relation> relations;

	private transient final Set<ModelChangeListener> changeListeners;

	@Getter
	@Setter
	private transient Point lastInteractionPoint = null;
	@Getter
	@Setter
	private transient boolean lolcatEnabled = false;
	@Getter
	@Setter
	private transient boolean referenceVisualizationEnabled = false;

	public MappingModel() {
		this.relations = new HashSet<>();
		this.changeListeners = new HashSet<>();
	}

	public void addRelation(Relation r) {
		if (this.relations.add(r)) {
			this.fireChangedEvent();
		}
	}

	public void removeRelation(Relation r) {
		if (this.relations.remove(r)) {
			this.fireChangedEvent();
		}
	}

	/**
	 * Gets the list of relations which are currently selected.
	 * @return The list of relations which are selected.
	 */
	public List<Relation> getSelectedRelations() {
		return this.relations.stream().filter(Relation::isSelected).collect(Collectors.toList());
	}

	/**
	 * Finds an attribute in this model, or returns null otherwise.
	 * @param relationName The name of the relation the attribute is in.
	 * @param attributeName The name of the attribute.
	 * @return The attribute which was found, or null if none was found.
	 */
	public Attribute findAttribute(String relationName, String attributeName) {
		for (Relation r : this.getRelations()) {
			if (!r.getName().equals(relationName)) continue;
			for (Attribute a : r.getAttributes()) {
				if (a.getName().equals(attributeName)) {
					return a;
				}
			}
		}
		return null;
	}

	/**
	 * Removes all attributes from any relation in the model which reference the
	 * given attribute.
	 * @param referenced The attribute to remove references from.
	 */
	public void removeAllReferencingAttributes(Attribute referenced) {
		for (Relation r : this.getRelations()) {
			Set<Attribute> removalSet = new HashSet<>();
			for (Attribute a : r.getAttributes()) {
				if (a instanceof ForeignKeyAttribute) {
					ForeignKeyAttribute fkA = (ForeignKeyAttribute) a;
					if (fkA.getReference().equals(referenced)) {
						removalSet.add(fkA);
					}
				}
			}
			removalSet.forEach(r::removeAttribute);
		}
	}

	/**
	 * Gets the bounding rectangle around all relations of the model.
	 * @return The bounding rectangle around all relations in this model.
	 */
	public Rectangle getRelationBounds() {
		if (this.getRelations().isEmpty()) {
			return new Rectangle(0, 0, 0, 0);
		}
		int minX = Integer.MAX_VALUE;
		int minY = Integer.MAX_VALUE;
		int maxX = Integer.MIN_VALUE;
		int maxY = Integer.MIN_VALUE;
		for (Relation r : this.getRelations()) {
			minX = Math.min(minX, r.getPosition().x);
			minY = Math.min(minY, r.getPosition().y);
			maxX = Math.max(maxX, r.getPosition().x);
			maxY = Math.max(maxY, r.getPosition().y);
		}
		return new Rectangle(minX, minY, maxX - minX, maxY - minY);
	}

	/**
	 * Adds a listener to this model, which will be notified of changes to the
	 * model.
	 * @param listener The listener to add.
	 */
	public void addChangeListener(ModelChangeListener listener) {
		this.changeListeners.add(listener);
		listener.onModelChanged();
	}

	/**
	 * Fires an all-purpose event which notifies all listeners that the model
	 * has changed.
	 */
	public final void fireChangedEvent() {
		this.changeListeners.forEach(ModelChangeListener::onModelChanged);
	}

	/**
	 * Updates the positions of all relations so that the bounding box for this
	 * model starts at 0, 0.
	 */
	public final void normalizeRelationPositions() {
		int minX = Integer.MAX_VALUE;
		int minY = Integer.MAX_VALUE;
		for (Relation r : this.getRelations()) {
			minX = Math.min(minX, r.getPosition().x);
			minY = Math.min(minY, r.getPosition().y);
		}
		if (this.getRelations().isEmpty()) {
			minX = 0;
			minY = 0;
		}
		for (Relation r : this.getRelations()) {
			final Point current = r.getPosition();
			r.setPosition(new Point(current.x - minX, current.y - minY));
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || this.getClass() != o.getClass()) return false;
		MappingModel that = (MappingModel) o;
		return this.getRelations().equals(that.getRelations());
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.getRelations());
	}

	@Override
	public ViewModel getViewModel() {
		return new MappingModelViewModel(this);
	}

	public MappingModel copy() {
		MappingModel c = new MappingModel();
		this.getRelations().forEach(r -> c.addRelation(r.copy(c)));
		return c;
	}

	public ObjectNode toJson(ObjectMapper mapper) {
		ObjectNode node = mapper.createObjectNode();
		ArrayNode relationsArray = node.withArray("relations");
		for (Relation r : this.relations) {
			ObjectNode relationNode = mapper.createObjectNode()
					.put("name", r.getName());
			ObjectNode positionNode = mapper.createObjectNode()
					.put("x", r.getPosition().x)
					.put("y", r.getPosition().y);
			relationNode.set("position", positionNode);
			ArrayNode attributesArray = relationNode.withArray("attributes");
			for (Attribute a : r.getAttributes()) {
				ObjectNode attributeNode = mapper.createObjectNode()
						.put("name", a.getName())
						.put("type", a.getType().name());
				if (a instanceof ForeignKeyAttribute) {
					ForeignKeyAttribute fk = (ForeignKeyAttribute) a;
					ObjectNode referenceNode = mapper.createObjectNode()
							.put("relation", fk.getReference().getRelation().getName())
							.put("attribute", fk.getReference().getName());
					attributeNode.set("references", referenceNode);
				}
				attributesArray.add(attributeNode);
			}
			relationsArray.add(relationNode);
		}
		return node;
	}

	public static MappingModel fromJson(ObjectNode node) {
		MappingModel model = new MappingModel();
		for (JsonNode relationNodeRaw : node.withArray("relations")) {
			if (!relationNodeRaw.isObject()) throw new IllegalArgumentException();
			ObjectNode relationNode = (ObjectNode) relationNodeRaw;
			String name = relationNode.get("name").asText();
			int x = relationNode.get("position").get("x").asInt();
			int y = relationNode.get("position").get("y").asInt();
			Point position = new Point(x, y);
			Relation relation = new Relation(model, position, name);
			for (JsonNode attributeNodeRaw : relationNode.withArray("attributes")) {
				if (!attributeNodeRaw.isObject()) throw new IllegalArgumentException();
				ObjectNode attributeNode = (ObjectNode) attributeNodeRaw;
				String attributeName = attributeNode.get("name").asText();
				AttributeType type = AttributeType.valueOf(attributeNode.get("type").asText().toUpperCase());
				Attribute attribute = new Attribute(relation, type, attributeName);
				relation.addAttribute(attribute);
			}
			model.addRelation(relation);
		}
		addForeignKeys(model, node);
		return model;
	}

	private static void addForeignKeys(MappingModel model, ObjectNode node) {
		Map<Attribute, ObjectNode> references = buildReferenceMap(model, node);
		while (!references.isEmpty()) {
			boolean workDone = false;
			for (Map.Entry<Attribute, ObjectNode> entry : references.entrySet()) {
				Attribute attribute = entry.getKey();
				String referencedName = entry.getValue().get("attribute").asText();
				String referencedRelation = entry.getValue().get("relation").asText();
				Attribute referencedAttribute = model.findAttribute(referencedRelation, referencedName);
				if (referencedAttribute == null) throw new IllegalArgumentException("Foreign key referenced unknown attribute.");
				if (!references.containsKey(referencedAttribute)) {
					ForeignKeyAttribute fk = new ForeignKeyAttribute(attribute.getRelation(), attribute.getType(), attribute.getName(), referencedAttribute);
					attribute.getRelation().removeAttribute(attribute);
					attribute.getRelation().addAttribute(fk);
					references.remove(attribute);
					workDone = true;
				}
			}
			if (!workDone) {
				throw new IllegalArgumentException("Invalid foreign key structure. Possible cyclic references.");
			}
		}
	}

	/**
	 * Builds a map that contains the set of foreign key references, indexed by
	 * the primitive attribute that is referencing another.
	 * @param model The model to lookup attributes from.
	 * @param node The raw JSON data for the model.
	 * @return A map containing foreign key references, to be used to build a
	 * complete model with foreign key attributes.
	 */
	private static Map<Attribute, ObjectNode> buildReferenceMap(MappingModel model, ObjectNode node) {
		Map<Attribute, ObjectNode> references = new HashMap<>();
		for (JsonNode r : node.withArray("relations")) {
			for (JsonNode a : r.withArray("attributes")) {
				if (a.has("references") && a.get("references").isObject()) {
					ObjectNode referenceNode = (ObjectNode) a.get("references");
					String attributeName = a.get("name").asText();
					String relationName = r.get("name").asText();
					Attribute attribute = model.findAttribute(relationName, attributeName);
					if (attribute == null) throw new IllegalArgumentException("Mapping model is not complete. Missing attribute " + attributeName + " in relation " + relationName + ".");
					references.put(attribute, referenceNode);
				}
			}
		}
		return references;
	}
}
