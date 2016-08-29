/*******************************************************************************
 * Copyright (c) ecobee, Inc. 2016. All rights reserved.
 *******************************************************************************/
package ca.appspace.esp8266.lambda;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Status {

	@JsonProperty
	private StatusCode code;

	@JsonProperty(required=false)
	private String message;

	public StatusCode getCode() {
		return code;
	}

	public void setCode(StatusCode code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@JsonIgnore
	public static Status ok() {
		Status status = new Status();
		status.setCode(StatusCode.OK);
		return status;
	}

	@JsonIgnore
	public static Status error(String message) {
		Status status = new Status();
		status.setCode(StatusCode.ERROR);
		status.setMessage(message);
		return status;
	}

}
