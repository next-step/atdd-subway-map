package subway.line;

import java.util.HashMap;
import java.util.Map;

public class LineUtils {
    public static Map<String, String> generateLineCreateRequest(final String name,
                                                                final String color,
                                                                final Long upStationId,
                                                                final Long downStationId,
                                                                final Long distance) {
        Map<String, String> lineRequest = new HashMap<>();
        lineRequest.put("name", name);
        lineRequest.put("color", color);
        lineRequest.put("upStationId", String.valueOf(upStationId));
        lineRequest.put("downStationId", String.valueOf(downStationId));
        lineRequest.put("distance", String.valueOf(distance));
        return lineRequest;
    }
}
