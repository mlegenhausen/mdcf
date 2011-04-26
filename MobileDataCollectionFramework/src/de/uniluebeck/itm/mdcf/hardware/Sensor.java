package de.uniluebeck.itm.mdcf.hardware;

import android.os.Parcel;
import android.os.Parcelable;

public class Sensor implements Parcelable {

	public static final Parcelable.Creator<Sensor> CREATOR = new Parcelable.Creator<Sensor>() {
        public Sensor createFromParcel(Parcel in) {
            return new Sensor(in);
        }

        public Sensor[] newArray(int size) {
            return new Sensor[size];
        }
    };
    
    private float maximumRange;
    
    private int minDelay;
    
    private String name;
    
    private float power;
    
    private float resolution;
    
    private int type;
    
    private String vendor;
    
    private int version;
    
    public Sensor(float maximumRange, int minDelay, String name, float power, float resolution, int type, String vendor, int version) {
    	this.maximumRange = maximumRange;
    	this.minDelay = minDelay;
    	this.name = name;
    	this.power = power;
    	this.resolution = resolution;
    	this.type = type;
    	this.vendor = vendor;
    	this.version = version;
    }
    
    public Sensor(Parcel in) {
		maximumRange = in.readFloat();
		minDelay = in.readInt();
		name = in.readString();
		power = in.readFloat();
		resolution = in.readFloat();
		type = in.readInt();
		vendor = in.readString();
		version = in.readInt();
	}
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeFloat(maximumRange);
		out.writeInt(minDelay);
		out.writeString(name);
		out.writeFloat(power);
		out.writeFloat(resolution);
		out.writeInt(type);
		out.writeString(vendor);
		out.writeInt(version);
	}

	public float getMaximumRange() {
		return maximumRange;
	}

	public int getMinDelay() {
		return minDelay;
	}

	public String getName() {
		return name;
	}

	public float getPower() {
		return power;
	}

	public float getResolution() {
		return resolution;
	}

	public int getType() {
		return type;
	}

	public String getVendor() {
		return vendor;
	}

	public int getVersion() {
		return version;
	}
}
