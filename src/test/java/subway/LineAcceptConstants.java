package subway;

import org.junit.jupiter.api.BeforeEach;

import java.util.Map;

public class LineAcceptConstants {
    protected static final String LINE_NAME = "name";
    protected static final String LINE_COLOR = "color";
    protected static final String LINE_UP_STATION_ID = "upStationId";
    protected static final String LINE_DOWN_STATION_ID = "downStationId";
    protected static final String LINE_DISTANCE = "distance";

    protected Map<String, Object> 이호선;
    protected Map<String, Object> 신분당선;

    @BeforeEach
    void setUp() {
        이호선 = Map.of(
                LINE_NAME, "2호선",
                LINE_COLOR, "bg-green-600",
                LINE_UP_STATION_ID, 1,
                LINE_DOWN_STATION_ID, 2,
                LINE_DISTANCE, 24
        );

        신분당선 = Map.of(
                LINE_NAME, "신분당선",
                LINE_COLOR, "bg-red-600",
                LINE_UP_STATION_ID, 3,
                LINE_DOWN_STATION_ID, 4,
                LINE_DISTANCE, 10
        );
    }
}
