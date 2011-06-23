package de.uniluebeck.itm.mdc.net;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;

import com.google.gson.Gson;

import de.uniluebeck.itm.mdc.net.ProgressStringEntity.ProgressListener;

public class WorkspaceTransmitionTask extends AsyncTask<TransferRequest, Integer, Throwable> implements ProgressListener {
	
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
		progressDialog.setMessage("Sending Workspace...");
		progressDialog.setCancelable(false);
		progressDialog.show();
	}
	
	@Override
	protected Throwable doInBackground(TransferRequest... requests) {
		for (TransferRequest request : requests) {
			String json = gson.toJson(request);
			
			try {
				SimpleJsonClient.to(url).send(json, this);
			} catch (ClientProtocolException e) {
				return e;
			} catch (IOException e) {
				return e;
			}
		}
		return null;
	}
	
	@Override
	protected void onProgressUpdate(Integer... values) {
		progressDialog.setMax(values[0]);
		progressDialog.setProgress(values[1]);
	}

	@Override
	protected void onPostExecute(Throwable e) {
		progressDialog.dismiss();
		if (e != null) {
			showAlertDialog(e.getMessage() + ". Please retry...");
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
	
	@Override
	public void onProgress(int progress, int size) {
		publishProgress(size, progress);
	}
}
