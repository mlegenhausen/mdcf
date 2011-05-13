package de.uniluebeck.itm.mdcf.persistence;

import java.io.InputStream;

import android.os.Parcel;
import android.os.Parcelable;

public class Binary implements Parcelable {

	public static final Parcelable.Creator<Binary> CREATOR = new Parcelable.Creator<Binary>() {
        public Binary createFromParcel(Parcel in) {
            return new Binary(in);
        }

        public Binary[] newArray(int size) {
            return new Binary[size];
        }
    };
    
    public Binary(Parcel in) {
		readFromParcel(in);
	}
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	public void readFromParcel(Parcel in) {
		
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		
	}
	
	public void dispose() {
		
	}
	
	public long getSize() {
		return 0L;
	}
	
	public InputStream getStream() {
		return null;
	}
	
	public int read(byte[] b, long position) {
		return 0;
	}
}
