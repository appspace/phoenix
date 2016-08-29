/*******************************************************************************
 * Copyright (c) ecobee, Inc. 2016. All rights reserved.
 *******************************************************************************/
package ca.appspace.esp8266;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsync;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsyncClient;

abstract class AWSPlatformSupport {

	protected Region getRegion() {
		return Region.getRegion(Regions.fromName(System.getenv("AWS_DEFAULT_REGION")));
	}

	protected AmazonDynamoDBAsync getAsyncDynamoDBClient() {
		Region myRegion = getRegion();
		AmazonDynamoDBAsyncClient client = new AmazonDynamoDBAsyncClient();
		client.setRegion(myRegion);
		return client;
	}
}
