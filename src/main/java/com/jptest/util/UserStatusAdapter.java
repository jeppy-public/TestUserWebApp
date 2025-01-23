package com.jptest.util;

import com.jptest.enums.UserStatus;
import com.nimbusds.jose.shaded.gson.*;

import java.lang.reflect.Type;

public class UserStatusAdapter implements JsonDeserializer<UserStatus> {
    @Override
    public UserStatus deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject statusObject = json.getAsJsonObject();
        int value = statusObject.get("value").getAsInt();
        return UserStatus.fromValue(value);
    }
}
