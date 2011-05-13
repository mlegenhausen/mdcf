package de.uniluebeck.itm.mdcf.persistence;

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
	
	private Value[] values;
	
	public Property(String identifier, Value[] values) {
		this.identifier = identifier;
		this.values = values;
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
		values = (Value[]) in.readArray(getClass().getClassLoader());
	}
	
	@Override
	public void writeToParcel(Parcel out, int flags) {
		super.writeToParcel(out, flags);
		out.writeString(identifier);
		out.writeArray(values);
	}
	
	@Override
	public boolean isNode() {
		return false;
	}

	@Override
	public void remove() {
		
	}
	
	public boolean isMultiple() {
		return values != null && values.length > 1;
	}
	
	public String getIdentifier() {
		return identifier;
	}
	
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	
	public Value getValue() {
		return values != null || values.length == 0 ? null : values[0];
	}
	
	public void setValue(Value value) {
		this.values = new Value[] { value };
	}
	
	public Value[] getValues() {
		return values;
	}
}
