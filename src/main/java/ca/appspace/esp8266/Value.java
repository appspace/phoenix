/*******************************************************************************
 * Copyright (c) ecobee, Inc. 2016. All rights reserved.
 *******************************************************************************/
package ca.appspace.esp8266;

import java.io.Serializable;


public class Value implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -2979871528461812125L;

	private String x;
	private Number y;

	public Value() {}

	public Value(String dateTime, Number val) {
		this.x = dateTime;
		this.y = val;
	}

	public String getX() {
		return x;
	}
	public void setX(String x) {
		this.x = x;
	}
	public Number getY() {
		return y;
	}
	public void setY(Number y) {
		this.y = y;
	}
}