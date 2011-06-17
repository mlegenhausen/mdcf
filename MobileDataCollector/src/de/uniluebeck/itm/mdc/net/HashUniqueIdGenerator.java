package de.uniluebeck.itm.mdc.net;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.google.common.base.Joiner;

public class HashUniqueIdGenerator implements UniqueIdGenerator {
	
	private static final String ALGORITHM = "SHA-1";
	
	private static final String SEPARATOR = "#";
	
	private static final String ENCODING = "iso-8859-1";
	
	private static String convertToHex(byte[] data) { 
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < data.length; i++) { 
            int halfByte = (data[i] >>> 4) & 0x0F;
            int twoHalfs = 0;
            do { 
                if ((0 <= halfByte) && (halfByte <= 9)) {
                    buf.append((char) ('0' + halfByte));
                } else { 
                    buf.append((char) ('a' + (halfByte - 10)));
                }
                halfByte = data[i] & 0x0F;
            } while(twoHalfs++ < 1);
        } 
        return buf.toString();
    } 
	
	@Override
	public String generate(String... strings) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		String text = Joiner.on(SEPARATOR).join(strings);
		MessageDigest messageDigest = MessageDigest.getInstance(ALGORITHM);
	    byte[] sha1hash = new byte[40];
	    messageDigest.update(text.getBytes(ENCODING), 0, text.length());
	    sha1hash = messageDigest.digest();
	    return convertToHex(sha1hash);
	}

}
