package de.uniluebeck.itm.mdc.net;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

public interface UniqueIdGenerator {

	String generate(String... strings) throws NoSuchAlgorithmException, UnsupportedEncodingException;
}
