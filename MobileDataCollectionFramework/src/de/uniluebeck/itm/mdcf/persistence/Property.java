package de.uniluebeck.itm.mdcf.persistence;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class Property extends Item implements Parcelable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1296625465780574564L;

	public static final Parcelable.Creator<Property> CREATOR = new Parcelable.Creator<Property>() {
        public Property createFromParcel(Parcel in) {
            return new Property(in);
        }

        public Property[] newArray(int size) {
            return new Property[size];
        }
    };
	
	private String identifier;
	
	private final List<Value> values = new ArrayList<Value>();
	
	public Property(String identifier, Value[] values) {
		this.identifier = identifier;
		this.values.addAll(Arrays.asList(values));
	}
	
	public Property(String identifier, Value value) {
		this(identifier, new Value[] { value });
	}
	
	public Property(Parcel in) {
		readFromParcel(in);
	}
	
	@Override
	public int describeContents() {
		return 0;
	}

	public void readFromParcel(Parcel in) {
		super.readFromParcel(in);
		identifier = in.readString();
		in.readTypedList(values, Value.CREATOR);
	}
	
	@Override
	public void writeToParcel(Parcel out, int flags) {
		super.writeToParcel(out, flags);
		out.writeString(identifier);
		out.writeTypedList(values);
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
