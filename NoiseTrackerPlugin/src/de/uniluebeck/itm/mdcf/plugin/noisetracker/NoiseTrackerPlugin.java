package de.uniluebeck.itm.mdcf.plugin.noisetracker;

import android.location.Location;
import android.location.LocationManager;
import android.media.MediaRecorder;
import android.os.RemoteException;
import de.uniluebeck.itm.mdcf.AbstractPlugin;
import de.uniluebeck.itm.mdcf.persistence.Node;

public class NoiseTrackerPlugin extends AbstractPlugin {

	private static final int POLL_INTERVAL = 100;
	
	private static final double EMA_FILTER = 0.6;
	
	private MediaRecorder recorder;
	
	private double ema = 0.0;

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
		ema = 0.0;
		
		double ema = 0.0;
		double amplitude = 0.0;
		for (int i = 0; i < 10; i++) {
			amplitude = getAmplitude();
			ema = getAmplitudeEMA();
			Thread.sleep(POLL_INTERVAL);
		}
		
		recorder.stop();
		recorder.release();

		storeLocationAndNoiseLevel(location, amplitude, ema);
	}

	private double getAmplitude() {
		if (recorder != null)
			return (recorder.getMaxAmplitude() / 2700.0);
		else
			return 0;

	}
	
    private double getAmplitudeEMA() {
        double amp = getAmplitude();
        ema = EMA_FILTER * amp + (1.0 - EMA_FILTER) * ema;
        return ema;
    }

	private void storeLocationAndNoiseLevel(Location location, double amplitude, double ema)
			throws RemoteException {
		Node workspace = getPersistenceManager().getWorkspace();
		Node node = new Node();
		node.setProperty("Latitude", location.getLatitude());
		node.setProperty("Longitude", location.getLongitude());
		node.setProperty("Amplitude", amplitude);
		node.setProperty("EMA", ema);
		workspace.addNode(node);
		getPersistenceManager().save(workspace);
	}
}
