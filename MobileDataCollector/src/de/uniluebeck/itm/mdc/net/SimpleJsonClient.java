package de.uniluebeck.itm.mdc.net;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;

import android.util.Log;
import de.uniluebeck.itm.mdc.net.ProgressStringEntity.ProgressListener;

public class SimpleJsonClient {

	private static final String TAG = SimpleJsonClient.class.getName();
	
	private final HttpClient httpClient = new DefaultHttpClient();
	
    private final HttpPost httpPost;
    
    public static SimpleJsonClient to(String url) {
    	return new SimpleJsonClient(url);
    }
    
    private SimpleJsonClient(String url) {
    	Log.i(TAG, "Url: " + url);
    	httpPost = new HttpPost(url);
    }
    
    public HttpResponse send(String json, ProgressListener listener) throws ClientProtocolException, IOException {
    	Log.i(TAG, "JSON: " + json);
    	final StringEntity entry = new ProgressStringEntity(json, listener);  
        entry.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        httpPost.setEntity(entry);
        return httpClient.execute(httpPost);
    }
}
