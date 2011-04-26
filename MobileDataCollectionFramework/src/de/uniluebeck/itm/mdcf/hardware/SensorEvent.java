package de.uniluebeck.itm.mdcf.hardware;

import android.os.Parcel;
import android.os.Parcelable;

public class SensorEvent implements Parcelable {

	public static final Parcelable.Creator<SensorEvent> CREATOR = new Parcelable.Creator<SensorEvent>() {
        public SensorEvent createFromParcel(Parcel in) {
            return new SensorEvent(in);
        }

        public SensorEvent[] newArray(int size) {
            return new SensorEvent[size];
        }
    };
	
	public int accuracy;
	
	public Sensor sensor;
	
	public long timestamp;
	
	public final float[] values;
	
	public SensorEvent(final float[] values) {
		this.values = values;
	}
	
	public SensorEvent(final Parcel in) {
		accuracy = in.readInt();
		sensor = in.readParcelable(null);
		timestamp = in.readLong();
		int length = in.readInt();
		values = new float[length];
		in.readFloatArray(values);
	}
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeInt(accuracy);
		out.writeParcelable(sensor, flags);
		out.writeLong(timestamp);
		out.writeInt(values.length);
		out.writeFloatArray(values);
	}

}
