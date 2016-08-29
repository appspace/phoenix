/*******************************************************************************
 * Copyright (c) ecobee, Inc. 2016. All rights reserved.
 *******************************************************************************/
package ca.appspace.esp8266;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import ca.appspace.esp8266.lambda.Status;
import ca.appspace.esp8266.lambda.StatusCode;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class RangeResponse implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 3040490047625314933L;

	private Collection<Value> values;

	private Status status;

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Collection<Value> getValues() {
		return values;
	}

	public void setValues(Collection<Value> values) {
		this.values = values;
	}

	@JsonIgnore
	public RangeResponse addValue(String dateTime, Number val) {
		if (values==null) {
			values = new ArrayList<>();
		}
		values.add(new Value(dateTime, val));
		return this;
	}

	@JsonIgnore
	public static RangeResponse error(String message) {
		RangeResponse resp = new RangeResponse();
		resp.setStatus(new Status());
		resp.getStatus().setCode(StatusCode.ERROR);
		resp.getStatus().setMessage(message);
		return resp;
	}
}
