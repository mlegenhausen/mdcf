package de.uniluebeck.itm.mdc.net;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.gson.Gson;

public class WorkspaceTransmitionTask extends AsyncTask<TransferRequest, Integer, Void> {

	private final Gson gson = new Gson();
	
	private final Context context;
	
	private final String url;
	
	private ProgressDialog progressDialog;
	
	public WorkspaceTransmitionTask(Context context, String url) {
		this.context = context;
		this.url = url;
	}
	
	@Override
	protected void onPreExecute() {
		progressDialog = new ProgressDialog(context);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progressDialog.setMessage("Sending Data...");
		progressDialog.setCancelable(false);
		progressDialog.show();
	}
	
	@Override
	protected Void doInBackground(TransferRequest... requests) {
		int delta = 100 / (requests.length * 2);
		int progress = 0;
		for (TransferRequest request : requests) {
			String json = gson.toJson(request);
			
			progress += delta;
			publishProgress(progress);
			
			try {
				SimpleJsonClient.to(url).send(json);
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			progress += delta;
			publishProgress(progress);
		}
		publishProgress(100);
		return null;
	}
	
	@Override
	protected void onProgressUpdate(Integer... values) {
		progressDialog.setProgress(values[0]);
	}

	@Override
	protected void onPostExecute(Void result) {
		progressDialog.hide();
		Toast.makeText(context, "Data was successfully transfered", Toast.LENGTH_LONG);
	}
}
