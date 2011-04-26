package de.uniluebeck.itm.mdcf.hardware;

import de.uniluebeck.itm.mdcf.hardware.SensorEventListener;

interface SensorManager {
	boolean registerListener(SensorEventListener listener, int sensors, int rate);
	
	void unregisterListener(SensorEventListener listener);
}