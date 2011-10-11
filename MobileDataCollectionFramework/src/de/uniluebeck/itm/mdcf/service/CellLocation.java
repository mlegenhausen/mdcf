package de.uniluebeck.itm.mdcf.service;

import android.os.Parcel;
import android.os.Parcelable;

public class CellLocation implements Parcelable {

	public static final Parcelable.Creator<CellLocation> CREATOR = new Parcelable.Creator<CellLocation>() {
		 public CellLocation createFromParcel(Parcel in) {
             return new CellLocation(in);
         }

         public CellLocation[] newArray(int size) {
             return new CellLocation[size];
         }
		
	};
	
	private int baseStationId;
	
	private int baseStationLatitude;
	
	private int baseStationLongitude;
	
	private int networkId;
	
	private int systemId;
	
	private int cid;
	
	private int lac;
	
	private int psc;
	
	public CellLocation() {
		
	}
	
	public CellLocation(Parcel src) {
		readFromParcel(src);
	}
	
    public int describeContents() {
        return 0;
    }
    
    private void readFromParcel(Parcel src) {
    	baseStationId = src.readInt();
    	baseStationLatitude = src.readInt();
    	baseStationLongitude = src.readInt();
    	networkId = src.readInt();
    	systemId = src.readInt();
    	cid = src.readInt();
    	lac = src.readInt();
    	psc = src.readInt();
    }

    public void writeToParcel(Parcel dest, int flags) {
    	dest.writeInt(baseStationId);
    	dest.writeInt(baseStationLatitude);
    	dest.writeInt(baseStationLongitude);
    	dest.writeInt(networkId);
    	dest.writeInt(systemId);
    	dest.writeInt(cid);
    	dest.writeInt(lac);
    	dest.writeInt(psc);
    }

	public int getBaseStationId() {
		return baseStationId;
	}

	public void setBaseStationId(int baseStationId) {
		this.baseStationId = baseStationId;
	}

	public int getBaseStationLatitude() {
		return baseStationLatitude;
	}

	public void setBaseStationLatitude(int baseStationLatitude) {
		this.baseStationLatitude = baseStationLatitude;
	}

	public int getBaseStationLongitude() {
		return baseStationLongitude;
	}

	public void setBaseStationLongitude(int baseStationLongitude) {
		this.baseStationLongitude = baseStationLongitude;
	}

	public int getNetworkId() {
		return networkId;
	}

	public void setNetworkId(int networkId) {
		this.networkId = networkId;
	}

	public int getSystemId() {
		return systemId;
	}

	public void setSystemId(int systemId) {
		this.systemId = systemId;
	}

	public int getCid() {
		return cid;
	}

	public void setCid(int cid) {
		this.cid = cid;
	}

	public int getLac() {
		return lac;
	}

	public void setLac(int lac) {
		this.lac = lac;
	}

	public int getPsc() {
		return psc;
	}

	public void setPsc(int psc) {
		this.psc = psc;
	}
}
