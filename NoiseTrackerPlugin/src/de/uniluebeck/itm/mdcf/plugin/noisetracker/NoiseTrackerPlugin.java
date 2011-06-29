package de.uniluebeck.itm.mdcf.plugin.noisetracker;

import android.location.Location;
import android.location.LocationManager;
import android.media.MediaRecorder;
import android.os.RemoteException;
import de.uniluebeck.itm.mdcf.AbstractPlugin;
import de.uniluebeck.itm.mdcf.persistence.Node;

public class NoiseTrackerPlugin extends AbstractPlugin {

	private static final int POLL_INTERVAL = 100;
	
	private MediaRecorder recorder;

	@Override
	protected void onRun() throws Exception {
		Location location = getLocationManager().getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

		recorder = new MediaRecorder();
		recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		recorder.setOutputFile("/dev/null");
		recorder.prepare();
		recorder.start();
		
		double amplitude = 0.0;
		for (int i = 0; i < 10; i++) {
			amplitude = getAmplitude();
			Thread.sleep(POLL_INTERVAL);
		}
		
		storeLocationAndNoiseLevel(location, amplitude);
		
		recorder.stop();
		recorder.release();
	}

	private double getAmplitude() {
		return recorder.getMaxAmplitude() / 2700.0;
	}

	private void storeLocationAndNoiseLevel(Location location, double amplitude) throws RemoteException {
		Node workspace = getPersistenceManager().getWorkspace();
		Node node = new Node();
		node.setProperty("Latitude", location.getLatitude());
		node.setProperty("Longitude", location.getLongitude());
		node.setProperty("Amplitude", amplitude);
		workspace.addNode(node);
		getPersistenceManager().save(workspace);
	}
}
