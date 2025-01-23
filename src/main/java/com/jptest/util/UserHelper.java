package com.jptest.util;

import com.google.common.io.Resources;
import com.google.common.reflect.TypeToken;
import com.jptest.entity.User;
import com.jptest.enums.UserStatus;
import com.nimbusds.jose.shaded.gson.Gson;
import com.nimbusds.jose.shaded.gson.GsonBuilder;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UserHelper {
    public static List<User> getDummyUser() throws IOException {
        InputStream inputStream= Resources.getResource("dummyData/userList.json").openStream();
        String json = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        Type listType = new TypeToken<ArrayList<User>>(){}.getType();
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(UserStatus.class, new UserStatusAdapter())
                .create();

        return gson.fromJson(json, listType);
    }
}
