package de.uniluebeck.itm.mdcf.hardware;

import de.uniluebeck.itm.mdcf.hardware.Sensor;
import de.uniluebeck.itm.mdcf.hardware.SensorEvent;

oneway interface SensorEventListener {
	void onAccuracyChanged(in Sensor sensor, int accuracy);
	
	void onSensorChanged(in SensorEvent event);
}