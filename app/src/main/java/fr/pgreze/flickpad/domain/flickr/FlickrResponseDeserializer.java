package fr.pgreze.flickpad.domain.flickr;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.Map;

import fr.pgreze.flickpad.data.flickr.model.FlickrFullUser;
import fr.pgreze.flickpad.data.flickr.model.FlickrGroups;
import fr.pgreze.flickpad.data.flickr.model.FlickrPhotos;
import fr.pgreze.flickpad.data.flickr.model.FlickrResponse;

/**
 * {@link fr.pgreze.flickpad.data.flickr.model.FlickrResponse} deserializer.
 */
public class FlickrResponseDeserializer implements JsonDeserializer<FlickrResponse> {

    @Override
    public FlickrResponse deserialize(JsonElement json,
                                      Type typeOfT,
                                      JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String stat = jsonObject.get("stat").getAsString();

        if (!stat.equals("ok")) {
            // Parse error fields
            return FlickrResponse.create(stat,
                    jsonObject.get("code").getAsInt(),
                    jsonObject.get("message").getAsString());
        }

        // Parse response

        for (Map.Entry<String, JsonElement> entry: jsonObject.entrySet()) {
            Type type;
            switch (entry.getKey()) {
                case "user":
                    type = FlickrFullUser.class;
                    break;
                case "photos":
                    type = FlickrPhotos.class;
                    break;
                case "groups":
                    type = FlickrGroups.class;
                    break;
                default:
                    // Skip this key
                    continue;
            }
            return FlickrResponse.create(stat, context.deserialize(entry.getValue(), type));
        }

        throw new RuntimeException("No response found with items: " + jsonObject.entrySet());
    }
}
