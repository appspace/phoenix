/*******************************************************************************
 * Copyright (c) ecobee, Inc. 2016. All rights reserved.
 *******************************************************************************/
package ca.appspace.esp8266;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGenerateStrategy;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedTimestamp;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName="readings")
public class AveragedReading implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 8549797706222296876L;


	@DynamoDBHashKey(attributeName="chipId")
	private String chipId;

	@DynamoDBRangeKey(attributeName="created")
	private Date created;

	@DynamoDBAutoGeneratedTimestamp(strategy=DynamoDBAutoGenerateStrategy.ALWAYS)
	private Date updated;

	@DynamoDBAttribute(attributeName="averagedValue")
	private BigDecimal averagedValue;

	public AveragedReading() {
		 created = new Date();
	}

	public String getChipId() {
		return chipId;
	}

	public void setChipId(String chipId) {
		this.chipId = chipId;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	public BigDecimal getAveragedValue() {
		return averagedValue;
	}

	public void setAveragedValue(BigDecimal averagedValue) {
		this.averagedValue = averagedValue;
	}

}