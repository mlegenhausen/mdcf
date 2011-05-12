package de.uniluebeck.itm.mdcf.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.os.Parcel;
import android.os.Parcelable;

public class Entity implements Parcelable {

	public static final Parcelable.Creator<Entity> CREATOR = new Parcelable.Creator<Entity>() {
        public Entity createFromParcel(Parcel in) {
            return new Entity(in);
        }

        public Entity[] newArray(int size) {
            return new Entity[size];
        }
    };
    
    private Long id;
    
    private Long timestamp;
    
    private final List<Entity> entities = new ArrayList<Entity>();
    
    public Entity(Parcel in) {
		readFromParcel(in);
	}
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	public void readFromParcel(Parcel in) {
		id = in.readLong();
		timestamp = in.readLong();
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeLong(id);
		out.writeLong(timestamp);
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

	public void addEntity(final Entity entity) {
		entities.add(entity);
	}
	
	public Set<String> getProperties() {
		return null;
	}
	
	public void setProperty(String name, boolean value) {
		
	}
	
	public void setProperty(String name, long value) {
		
	}
	
	public void setProperty(String name, double value) {
		
	}
	
	public void setProperty(String name, String value) {
		
	}
	
	public void setProperty(String name, Entity value) {
		
	}
}
