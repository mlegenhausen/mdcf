package de.uniluebeck.itm.mdc.net;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.gson.Gson;

import de.uniluebeck.itm.mdcf.persistence.Node;

public class WorkspaceTransmitionTask extends AsyncTask<Node, Integer, Void> {

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
	protected Void doInBackground(Node... nodes) {
		for (Node node : nodes) {
			String json = gson.toJson(node);
			publishProgress(50);
			try {
				SimpleJsonClient.to(url).send(json);
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			publishProgress(100);
		}
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
