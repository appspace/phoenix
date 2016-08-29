/*******************************************************************************
 * Copyright (c) ecobee, Inc. 2016. All rights reserved.
 *******************************************************************************/
package ca.appspace.esp8266;

import java.io.Serializable;

public class RangeRequest implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -6763720426960533180L;

	private String start;
	private String end;
	private String chipId;

	public String getStart() {
		return start;
	}
	public void setStart(String start) {
		this.start = start;
	}
	public String getEnd() {
		return end;
	}
	public void setEnd(String end) {
		this.end = end;
	}
	public String getChipId() {
		return chipId;
	}
	public void setChipId(String chipId) {
		this.chipId = chipId;
	}

}
