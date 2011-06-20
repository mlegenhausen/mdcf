package de.uniluebeck.itm.mdcf;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import android.os.Parcel;
import android.os.Parcelable;

@Root
public class PluginInfo implements Parcelable {

	public static final Parcelable.Creator<PluginInfo> CREATOR = new Parcelable.Creator<PluginInfo>() {
        public PluginInfo createFromParcel(Parcel in) {
            return new PluginInfo(in);
        }

        public PluginInfo[] newArray(int size) {
            return new PluginInfo[size];
        }
    };
    
    private String pkg;
    
    @Element
    private String name;
    
    @Attribute
    private String action;
    
    @Attribute
    private String version;
    
    @Element
    private String url;
    
    @Element
    private int period;
    
    @Element
    private int duration;
    
    @Element(required=false)
    private String description;
    
    @Element
    private long transferInterval;
    
    @ElementList(entry="service")
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
		parcel.writeString(description);
		parcel.writeLong(transferInterval);
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
		description = parcel.readString();
		transferInterval = parcel.readLong();
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
	
	public long getTransferInterval() {
		return transferInterval;
	}
	
	public void setTransferInterval(long transferInterval) {
		this.transferInterval = transferInterval;
	}
	
	public List<String> getServices() {
		return services;
	}
	
	public void setServices(List<String> services) {
		this.services = services;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof PluginInfo) {
			return action.equals(((PluginInfo) o).getAction());
		}
		return super.equals(o);
	}
}
