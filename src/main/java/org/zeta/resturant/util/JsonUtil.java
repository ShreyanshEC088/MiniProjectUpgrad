package org.zeta.resturant.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

public class JsonUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T parseRequest(InputStream input, Class<T> clazz) throws IOException {
        return objectMapper.readValue(input, clazz);
    }
}