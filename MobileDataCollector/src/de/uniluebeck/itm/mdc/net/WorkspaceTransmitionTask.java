package de.uniluebeck.itm.mdc.net;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;

import com.google.gson.Gson;

public class WorkspaceTransmitionTask extends AsyncTask<TransferRequest, Integer, Boolean> {

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
	protected Boolean doInBackground(TransferRequest... requests) {
		int delta = 100 / (requests.length * 2);
		int progress = 0;
		for (TransferRequest request : requests) {
			String json = gson.toJson(request);
			
			progress += delta;
			publishProgress(progress);
			
			try {
				SimpleJsonClient.to(url).send(json);
			} catch (ClientProtocolException e) {
				return false;
			} catch (IOException e) {
				return false;
			}
			
			progress += delta;
			publishProgress(progress);
		}
		publishProgress(100);
		return true;
	}
	
	@Override
	protected void onProgressUpdate(Integer... values) {
		progressDialog.setProgress(values[0]);
	}

	@Override
	protected void onPostExecute(Boolean result) {
		progressDialog.hide();
		if (!result) {
			showAlertDialog("Unable to transfer workspace. Please retry...");
		}
	}
	
	private void showAlertDialog(String message) {
		new AlertDialog.Builder(context)
			.setTitle("Error")
			.setMessage(message)
			.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			})
			.show();
	}
}
