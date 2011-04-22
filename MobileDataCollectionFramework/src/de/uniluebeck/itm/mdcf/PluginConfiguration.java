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
	}
	
	public void readFromParcel(Parcel parcel) {
		action = parcel.readString();
		name = parcel.readString();
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
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof PluginConfiguration) {
			return action.equals(((PluginConfiguration) o).getAction());
		}
		return super.equals(o);
	}
}
