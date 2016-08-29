/*******************************************************************************
 * Copyright (c) ecobee, Inc. 2016. All rights reserved.
 *******************************************************************************/
package ca.appspace.esp8266;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsync;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class ConvertExistingDataHandler extends AWSPlatformSupport implements RequestHandler<Object, String> {

	private final static BigDecimal TG = new BigDecimal(148.1629, MathContext.DECIMAL32);

	@Override
	public String handleRequest(Object input, Context context) {
        AmazonDynamoDBAsync db = getAsyncDynamoDBClient();
        DynamoDBMapper mapper = new DynamoDBMapper(db);

        Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
        eav.put(":v1", new AttributeValue().withS("422918"));
        DynamoDBQueryExpression<AveragedReading> queryExpression = new DynamoDBQueryExpression<AveragedReading>()
        		.withKeyConditionExpression("chipId = :v1")
        		.withExpressionAttributeValues(eav);

        PaginatedQueryList<AveragedReading> results = mapper.query(AveragedReading.class, queryExpression);
        List<ConvertedReading> convertedList = results.stream().map(item -> {
        	ConvertedReading converted = new ConvertedReading();
			converted.setCreated(new Date());
			converted.setChipId(item.getChipId());
			converted.setAveragedValue(item.getAveragedValue().divide(TG, MathContext.DECIMAL32));
			return converted;
        })
        .collect(Collectors.toList());
        mapper.save(convertedList.iterator().next());
        //mapper.batchSave(convertedList);
        return "Processed "+convertedList.size()+" records";
	}

}
