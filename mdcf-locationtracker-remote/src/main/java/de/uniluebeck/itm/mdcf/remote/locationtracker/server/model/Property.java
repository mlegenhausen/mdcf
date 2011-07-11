package de.uniluebeck.itm.mdcf.remote.locationtracker.server.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Property extends Item {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1296625465780574564L;
	
	private String identifier;
	
	private final List<Value> values = new ArrayList<Value>();
	
	public Property(String identifier, Value[] values) {
		this.identifier = identifier;
		this.values.addAll(Arrays.asList(values));
	}
	
	public Property(String identifier, Value value) {
		this(identifier, new Value[] { value });
	}
	
	@Override
	public boolean isNode() {
		return false;
	}

	@Override
	public void remove() {
		
	}
	
	public boolean isMultiple() {
		return values != null && values.size() > 1;
	}
	
	public String getIdentifier() {
		return identifier;
	}
	
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
		setTimestamp(System.currentTimeMillis());
	}
	
	public Value getValue() {
		return values.size() == 0 ? null : values.get(0);
	}
	
	public void setValue(Value value) {
		values.clear();
		values.add(value);
		setTimestamp(System.currentTimeMillis());
	}
	
	public void setValues(Value[] values) {
		this.values.clear();
		this.values.addAll(Arrays.asList(values));
	}
	
	public Value[] getValues() {
		return values.toArray(new Value[0]);
	}
}
