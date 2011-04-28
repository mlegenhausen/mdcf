package de.uniluebeck.itm.plugin;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.RemoteException;
import android.util.Log;
import de.uniluebeck.itm.mdcf.Plugin;

public class PluginImpl extends Plugin.Stub implements SensorEventListener {

	public static final String LOG_TAG = "HelloWorldPluginImpl";
	
	private SensorManager sensorManager;
	
	public PluginImpl(Context context) {
		sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
	}
	
	@Override
	public void init() throws RemoteException {
	}

	@Override
	public void start() throws RemoteException {
		sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_FASTEST);
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			Log.e(LOG_TAG, "Start was interrupted.", e);
		}
		Log.i(LOG_TAG, "Hello World");
	}

	@Override
	public void stop() throws RemoteException {
		sensorManager.unregisterListener(this);
	}

	@Override
	public void onAccuracyChanged(Sensor paramSensor, int paramInt) {
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		Log.i(LOG_TAG, "Values: " + event.values[0] + " ," + event.values[1] + " ," + event.values[2]);
	}
}
