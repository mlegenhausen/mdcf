package de.uniluebeck.itm.mdcf;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class PluginInfo implements Parcelable {

	public static final Parcelable.Creator<PluginInfo> CREATOR = new Parcelable.Creator<PluginInfo>() {
        public PluginInfo createFromParcel(Parcel in) {
            return new PluginInfo(in);
        }

        public PluginInfo[] newArray(int size) {
            return new PluginInfo[size];
        }
    };
    
    private String name;
    
    private String pkg;
    
    private String action;
    
    private String version;
    
    private String url;
    
    private int period;
    
    private int duration;
    
    private List<String> services = new ArrayList<String>();
    
    public PluginInfo() {
		
	}
    
    public PluginInfo(String action, String name) {
    	this.action = action;
    	this.name = name;
    }
    
    public PluginInfo(Parcel in) {
		readFromParcel(in);
	}
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int index) {
		parcel.writeString(action);
		parcel.writeString(pkg);
		parcel.writeString(name);
		parcel.writeString(version);
		parcel.writeInt(period);
		parcel.writeInt(duration);
		parcel.writeString(url);
		parcel.writeStringList(services);
	}
	
	public void readFromParcel(Parcel parcel) {
		action = parcel.readString();
		pkg = parcel.readString();
		name = parcel.readString();
		version = parcel.readString();
		period = parcel.readInt();
		duration = parcel.readInt();
		url = parcel.readString();
		parcel.readStringList(services);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getPackage() {
		return pkg;
	}
	
	public void setPackage(String pkg) {
		this.pkg = pkg;
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

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
	public List<String> getServices() {
		return services;
	}
	
	public void setServices(List<String> services) {
		this.services = services;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof PluginInfo) {
			return action.equals(((PluginInfo) o).getAction());
		}
		return super.equals(o);
	}
}
