package de.uniluebeck.itm.mdc.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.google.common.io.Closeables;

public final class ObjectCloner {

	public static Object deepCopy(Object oldObj) {
		ObjectOutputStream oos = null;
		ObjectInputStream ois = null;
		
		try {
			try {
				ByteArrayOutputStream bos = new ByteArrayOutputStream(); // A
				oos = new ObjectOutputStream(bos); // B
				// serialize and pass the object
				oos.writeObject(oldObj); // C
				oos.flush(); // D
				ByteArrayInputStream bin = new ByteArrayInputStream(bos.toByteArray()); // E
				ois = new ObjectInputStream(bin); // F
				// return the new object
				return ois.readObject(); // G
			} finally {
				Closeables.closeQuietly(oos);
				Closeables.closeQuietly(ois);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
