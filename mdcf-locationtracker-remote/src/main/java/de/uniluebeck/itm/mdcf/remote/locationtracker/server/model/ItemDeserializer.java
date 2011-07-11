package de.uniluebeck.itm.mdcf.remote.locationtracker.server.model;

import static com.google.common.collect.Lists.newArrayList;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;


public class ItemDeserializer implements JsonDeserializer<Item> {

	public Item deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		JsonObject obj = json.getAsJsonObject();
		if (obj.has("identifier") && obj.has("values")) {
			String identifier = obj.get("identifier").getAsString();
			JsonArray jsonValues = obj.get("values").getAsJsonArray();
			List<Value> values = newArrayList();
			for (JsonElement element : jsonValues) {
				values.add((Value) context.deserialize(element, Value.class));
			}
			return new Property(identifier, values.toArray(new Value[0]));
		}
		return new Node();
	}
}
