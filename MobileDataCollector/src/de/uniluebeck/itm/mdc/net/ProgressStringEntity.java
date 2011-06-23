package de.uniluebeck.itm.mdc.net;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;

public class ProgressStringEntity extends StringEntity {

	public static interface ProgressListener {
		
		void onProgress(int progress, int size);
	}
	
	public class ProgressOutputStream extends FilterOutputStream {
		
		private final ProgressListener listener;
        
		private int progress = 0;

        public ProgressOutputStream(final OutputStream out, final ProgressListener listener) {
            super(out);
            this.listener = listener;
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            out.write(b, off, len);
            this.progress += len;
            this.listener.onProgress(this.progress, size);
        }

        @Override
        public void write(int b) throws IOException {
            out.write(b);
            this.progress++;
            this.listener.onProgress(this.progress, size);
        }
	}
	
	private final ProgressListener listener;
	
	private final int size;
	
	public ProgressStringEntity(String data, ProgressListener listener) throws UnsupportedEncodingException {
		super(data);
		this.listener = listener;
		this.size = data.length();
	}
	
	@Override
	public void writeTo(OutputStream outstream) throws IOException {
		super.writeTo(new ProgressOutputStream(outstream, this.listener));
	}
}
