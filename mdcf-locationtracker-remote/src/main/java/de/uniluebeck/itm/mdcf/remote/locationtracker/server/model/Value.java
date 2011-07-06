package de.uniluebeck.itm.mdcf.remote.locationtracker.server.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;

public class Value implements Serializable {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = -5378304228650146756L;
	
	private int type;
	
	private Object value;
	
	public Value(int type, Object value) {
		this.type = type;
		this.value = value;
	}

	public boolean getBoolean() {
		return (Boolean) value;
	}
	
	public Binary getBinary() {
		return (Binary) value;
	}
	
	public Calendar getDate() {
		return (Calendar) value;
	}
	
	public BigDecimal getDecimal() {
		return (BigDecimal) value;
	}
	
	public double getDouble() {
		return (Double) value;
	}
	
	public long getLong() {
		return (Long) value;
	}
	
	public String getString() {
		return value.toString();
	}
	
	public int getType() {
		return type;
	}
	
	@Override
	public String toString() {
		return value.toString();
	}
}
