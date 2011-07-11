package de.uniluebeck.itm.mdcf.remote.model;

import java.io.Serializable;

public abstract class Item implements Serializable {
	
	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 4450691537032094022L;

	private Node parent;
	
    private Long id;
    
    /**
     * Indicates the last modification of this item.
     */
    private Long timestamp;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getTimestamp() {
		return timestamp;
	}
	
	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}
	
	public Node getParent() {
		return parent;
	}
	
	public abstract void remove();

	public abstract boolean isNode();
}
