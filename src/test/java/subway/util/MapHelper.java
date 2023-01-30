package subway.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Map;

public class MapHelper {

    private static ObjectMapper mapper;

    static {
        mapper = new ObjectMapper();
    }

    public static <T> T readValue(Map<String, Object> map, Class<T> clazz) {
        try {
            final String json = mapper.writeValueAsString(map);
            return mapper.readValue(json, clazz);
        } catch (IOException e) {
            throw new RuntimeException("failed to convert", e);
        }
    }
}
