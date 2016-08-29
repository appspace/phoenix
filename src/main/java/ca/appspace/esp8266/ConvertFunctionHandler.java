/*******************************************************************************
 * Copyright (c) ecobee, Inc. 2016. All rights reserved.
 *******************************************************************************/
package ca.appspace.esp8266;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsync;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent.DynamodbStreamRecord;

public class ConvertFunctionHandler extends AWSPlatformSupport implements RequestHandler<DynamodbEvent, String> {

	private final static BigDecimal TG = new BigDecimal(148.1629, MathContext.DECIMAL32);

	@Override
	public String handleRequest(DynamodbEvent input, Context context) {
		if (input==null || input.getRecords().isEmpty()) {
			return "No records";
		}

        AmazonDynamoDBAsync db = getAsyncDynamoDBClient();
        DynamoDBMapper mapper = new DynamoDBMapper(db);

        Collection<ConvertedReading> toSave = new ArrayList<>();
		for (DynamodbStreamRecord record : input.getRecords()) {
			StringBuilder sb = new StringBuilder();
			sb.append("event name: ");
			sb.append(record.getEventName());
			sb.append("; values: ");
			sb.append(valuesOf(record.getDynamodb().getNewImage()));
			context.getLogger().log(sb.toString());
			if (record.getEventName()=="INSERT") {
				ConvertedReading converted = new ConvertedReading();
				converted.setCreated(new Date());
				converted.setChipId(record.getDynamodb().getNewImage().get("chipId").getS());
				BigDecimal averaged = new BigDecimal(record.getDynamodb().getNewImage().get("averagedValue").getN(), MathContext.DECIMAL32);
				converted.setAveragedValue(averaged.divide(TG, MathContext.DECIMAL32));
				//toSave.add(converted);
				mapper.save(converted);
			}
			//
		}
		//mapper.batchSave(toSave);
		return "Processed "+toSave.size()+" records";
	}


	/**
	 * Returns map flattened to key-value pairs
	 * @param input the map
	 * @return String of all keys and values in the map
	 */
	private String valuesOf(Map<String, AttributeValue> input) {
		if (input==null || input.isEmpty()) {
			return "";
		}
		return input.entrySet()
				.parallelStream()
				.map(entry -> entry.getKey() + "="+entry.getValue().getS())
				.collect(Collectors.joining("; "));
	}
}
