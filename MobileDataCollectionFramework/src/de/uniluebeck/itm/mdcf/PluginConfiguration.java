package de.uniluebeck.itm.mdcf;

import android.os.Parcel;
import android.os.Parcelable;

public class PluginConfiguration implements Parcelable {

	public static final Parcelable.Creator<PluginConfiguration> CREATOR = new Parcelable.Creator<PluginConfiguration>() {
        public PluginConfiguration createFromParcel(Parcel in) {
            return new PluginConfiguration(in);
        }

        public PluginConfiguration[] newArray(int size) {
            return new PluginConfiguration[size];
        }
    };
    
    private String name;
    
    private String action;
    
    private String url;
    
    private int period;
    
    private int duration;
    
    private int[] sensorTypes = new int[0];
    
    public PluginConfiguration() {
		
	}
    
    public PluginConfiguration(String action, String name) {
    	this.action = action;
    	this.name = name;
    }
    
    public PluginConfiguration(Parcel in) {
		readFromParcel(in);
	}
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int index) {
		parcel.writeString(action);
		parcel.writeString(name);
		parcel.writeInt(period);
		parcel.writeInt(duration);
		parcel.writeIntArray(sensorTypes);
	}
	
	public void readFromParcel(Parcel parcel) {
		action = parcel.readString();
		name = parcel.readString();
		period = parcel.readInt();
		duration = parcel.readInt();
		parcel.readIntArray(sensorTypes);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public int[] getSensorTypes() {
		return sensorTypes;
	}

	public void setSensorTypes(int[] sensorTypes) {
		this.sensorTypes = sensorTypes;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof PluginConfiguration) {
			return action.equals(((PluginConfiguration) o).getAction());
		}
		return super.equals(o);
	}
}
