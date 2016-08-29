package ca.appspace.esp8266;

import java.math.BigDecimal;
import java.math.MathContext;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsync;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class AdcFunctionHandler extends AWSPlatformSupport implements RequestHandler<AdcReadings, Object> {

    @Override
    public Object handleRequest(AdcReadings input, Context context) {
        context.getLogger().log("Input: " + input);

        if (input.getValues()==null || input.getValues().length==0) {
        	return "OK";
        }

        AmazonDynamoDBAsync db = getAsyncDynamoDBClient();
        DynamoDBMapper mapper = new DynamoDBMapper(db);

        AveragedReading persisted = translate(input);
        mapper.save(persisted);

        return "OK";
    }

    private AveragedReading translate(AdcReadings input) {
    	AveragedReading reading = new AveragedReading();
    	reading.setChipId(input.getChipId());
    	BigDecimal averaged = new BigDecimal(0.00, MathContext.DECIMAL32);
    	for (Integer val : input.getValues()) {
    		averaged = averaged.add(BigDecimal.valueOf(val), MathContext.DECIMAL32);
    	}
    	averaged = averaged.divide(BigDecimal.valueOf(input.getValues().length), MathContext.DECIMAL32);
    	reading.setAveragedValue(averaged.round(MathContext.DECIMAL32));
    	return reading;
	}

}
