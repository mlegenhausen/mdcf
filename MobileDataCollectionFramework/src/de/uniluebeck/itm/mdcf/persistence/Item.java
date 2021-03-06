package de.uniluebeck.itm.mdcf.persistence;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

public abstract class Item implements Parcelable, Serializable {
	
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
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	public void readFromParcel(Parcel in) {
		id = (Long) in.readSerializable();
		timestamp = (Long) in.readSerializable();
		parent = in.readParcelable(Node.class.getClassLoader());
	}
	
	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeSerializable(id);
		out.writeSerializable(timestamp);
		out.writeParcelable(parent, flags);
	}
	
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
