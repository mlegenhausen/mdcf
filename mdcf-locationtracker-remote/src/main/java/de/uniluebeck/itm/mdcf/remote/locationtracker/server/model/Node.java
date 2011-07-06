package de.uniluebeck.itm.mdcf.remote.locationtracker.server.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Node extends Item {
    
	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 3780531054736799768L;
    
    private final List<Node> nodes = new ArrayList<Node>();
    
    private final Map<String, Item> properties = new HashMap<String, Item>();
    
    public Node() {
    	
	}
	
	@Override
	public boolean isNode() {
		return true;
	}

	public void addNode(final Node node) {
		nodes.add(node);
	}
	
	public Iterator<Node> getNodes() {
		return nodes.iterator();
	}
	
	public boolean hasProperty(String name) {
		return properties.containsKey(name);
	}
	
	public Set<String> getProperties() {
		return properties.keySet();
	}
	
	public Property getProperty(String name) {
		Item item = properties.get(name);
		if (item.isNode()) {
			throw new RuntimeException("This item is not a property.");
		}
		return (Property) item;
	}
	
	public void setProperty(String name, boolean value) {
		setProperty(name, new Value(PropertyType.BOOLEAN, value));
	}
	
	public void setProperty(String name, long value) {
		setProperty(name, new Value(PropertyType.LONG, value));
	}
	
	public void setProperty(String name, double value) {
		setProperty(name, new Value(PropertyType.DOUBLE, value));
	}
	
	public void setProperty(String name, String value) {
		setProperty(name, new Value(PropertyType.STRING, value));
	}
	
	public void setProperty(String name, Node value) {
		properties.put(name, value);
		setTimestamp(System.currentTimeMillis());
	}
	
	public void setProperty(String name, Value value) {
		setProperty(name, new Value[] { value });
	}
	
	public void setProperty(String name, Value[] values) {
		Property property = new Property(name, values);
		properties.put(name, property);
		setTimestamp(System.currentTimeMillis());
	}

	@Override
	public void remove() {
		
	}
}
