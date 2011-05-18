package de.uniluebeck.itm.mdcf.persistence;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;

import android.os.Parcel;
import android.os.Parcelable;

public class Value implements Parcelable {

	public static final Parcelable.Creator<Value> CREATOR = new Parcelable.Creator<Value>() {
        public Value createFromParcel(Parcel in) {
            return new Value(in);
        }

        public Value[] newArray(int size) {
            return new Value[size];
        }
    };
	
	private int type;
	
	private Object value;
	
	public Value(int type, Object value) {
		this.type = type;
		this.value = value;
	}
	
	public Value(Parcel in) {
		readFromParcel(in);
	}
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	public void readFromParcel(Parcel in) {
		type = in.readInt();
		value = in.readSerializable();
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeInt(type);
		out.writeSerializable((Serializable) value);
	}

	public boolean getBoolean() {
		return (Boolean) value;
	}
	
	public Binary getBinary() {
		return (Binary) value;
	}
	
	public Calendar getDate() {
		return (Calendar) value;
	}
	
	public BigDecimal getDecimal() {
		return (BigDecimal) value;
	}
	
	public double getDouble() {
		return (Double) value;
	}
	
	public long getLong() {
		return (Long) value;
	}
	
	public String getString() {
		return value.toString();
	}
	
	public int getType() {
		return type;
	}
	
	@Override
	public String toString() {
		return value.toString();
	}
}
