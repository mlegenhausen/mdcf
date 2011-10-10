package de.uniluebeck.itm.mdcf.service;

import android.os.Parcel;
import android.os.Parcelable;

public class NetworkInfo implements Parcelable {

	public static final Parcelable.Creator<NetworkInfo> CREATOR = new Parcelable.Creator<NetworkInfo>() {
		public NetworkInfo createFromParcel(Parcel in) {
			return new NetworkInfo(in);
		}

		public NetworkInfo[] newArray(int size) {
			return new NetworkInfo[size];
		}
	};
	
	public NetworkInfo(Parcel in) {
		
	}
		
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
    	
    }
}
