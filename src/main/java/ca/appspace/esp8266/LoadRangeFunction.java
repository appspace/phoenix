/*******************************************************************************
 * Copyright (c) ecobee, Inc. 2016. All rights reserved.
 *******************************************************************************/
package ca.appspace.esp8266;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import ca.appspace.esp8266.lambda.Status;

import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.util.StringUtils;

public class LoadRangeFunction extends AWSPlatformSupport implements RequestHandler<RangeRequest, RangeResponse> {

	private final static BigDecimal TG = new BigDecimal(148.1629, MathContext.DECIMAL32);
	private final static BigDecimal TG_761506 = new BigDecimal(847.2727, MathContext.DECIMAL32);

	private final static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	private final static DateFormat DATE_FORMATTER = new SimpleDateFormat(DATE_FORMAT);

	@Override
	public RangeResponse handleRequest(RangeRequest input, Context context) {
		Date start=null, end = null;
		if (input.getChipId()==null) {
			return RangeResponse.error("ChipId required");
		}
		if (StringUtils.isNullOrEmpty(input.getStart())) {
			return RangeResponse.error("Start Period required");
		} else {
			try {
				start = DATE_FORMATTER.parse(input.getStart());
			} catch (ParseException e) {
				return RangeResponse.error("Invalid Start Period. Use format "+DATE_FORMAT);
			}
		}
		if (StringUtils.isNullOrEmpty(input.getEnd())) {
			return RangeResponse.error("End Period required");
		} else {
			try {
				end = DATE_FORMATTER.parse(input.getEnd());
			} catch (ParseException e) {
				return RangeResponse.error("Invalid End Period. Use format "+DATE_FORMAT);
			}
		}
		context.getLogger().log("Loading values for chipId "+input.getChipId()+" from "+input.getStart()+" to "+input.getEnd());
		DynamoDB dynamoDB = new DynamoDB(getAsyncDynamoDBClient());
		Table readingsTable = dynamoDB.getTable("readings");

		QuerySpec querySpec = new QuerySpec()
			.withKeyConditionExpression("chipId = :v_chipId and created between :v_start and :v_end")
			.withScanIndexForward(true)
			.withValueMap(new ValueMap()
				.withString(":v_chipId", input.getChipId().trim())
				.withString(":v_start", start.toInstant().toString())
				.withString(":v_end", end.toInstant().toString())
			);
			//.withAttributesToGet("created","averagedValue");
		ItemCollection<QueryOutcome> results = readingsTable.query(querySpec);
		RangeResponse resp = new RangeResponse();
		resp.setStatus(Status.ok());
		final AtomicInteger count = new AtomicInteger(0);
		results.forEach(item -> {
			if ("761506".equals(input.getChipId())) {
				resp.addValue(item.getString("created"),
						new BigDecimal(item.getString("averagedValue"), MathContext.DECIMAL32)
						.divide(TG_761506, MathContext.DECIMAL32));
			} else {
				resp.addValue(item.getString("created"),
						new BigDecimal(item.getString("averagedValue"), MathContext.DECIMAL32)
						.divide(TG, MathContext.DECIMAL32));
			}
			count.incrementAndGet();
		});
		context.getLogger().log("Number of results loaded: "+count.get());
		return resp;
	}
}
