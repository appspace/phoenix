/*******************************************************************************
 * Copyright (c) ecobee, Inc. 2016. All rights reserved.
 *******************************************************************************/
package ca.appspace.esp8266;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName="convertedReadings")
public class ConvertedReading extends AveragedReading {

	/**
	 *
	 */
	private static final long serialVersionUID = -1405354771947615495L;
}
