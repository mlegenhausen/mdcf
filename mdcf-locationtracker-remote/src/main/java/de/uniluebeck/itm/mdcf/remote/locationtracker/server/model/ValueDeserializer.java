package de.uniluebeck.itm.mdcf.remote.locationtracker.server.model;

import java.lang.reflect.Type;
import java.util.Date;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class ValueDeserializer implements JsonDeserializer<Value> {

	public Value deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		JsonObject jsonObject = json.getAsJsonObject();
		if (jsonObject.has("type") && jsonObject.has("value")) {
			int type = jsonObject.get("type").getAsInt();
			JsonElement jsonValue = jsonObject.get("value");
			Object value = null;
			switch (type) {
			case PropertyType.BINARY:
				value = context.deserialize(jsonValue, Binary.class);
				break;
			case PropertyType.BOOLEAN:
				value = jsonValue.getAsBoolean();
				break;
			case PropertyType.DATE:
				value = context.deserialize(jsonValue, Date.class);
				break;
			case PropertyType.DECIMAL:
				value = jsonValue.getAsBigDecimal();
				break;
			case PropertyType.DOUBLE:
				value = jsonValue.getAsDouble();
				break;
			case PropertyType.LONG:
				value = jsonValue.getAsLong();
				break;
			case PropertyType.STRING:
				value = jsonValue.getAsString();
				break;
			}
			return new Value(type, value);
		}
		return null;
	}

}
