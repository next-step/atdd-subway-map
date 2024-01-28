package config.fixtures.subway;

import subway.dto.StationLineRequest;

public class StationLineMockData {

    public static final String NAME_1 = "신분당선";
    public static final String COLOR_1 = "bg-red-600";
    public static final int UP_STATION_ID_1 = 1;
    public static final int DOWN_STATION_ID_1 = 2;
    public static final int DISTANCE_1 = 10;

    public static final String NAME_2 = "분당선";
    public static final String COLOR_2 = "bg-green-600";
    public static final int UP_STATION_ID_2 = 2;
    public static final int DOWN_STATION_ID_2 = 4;
    public static final int DISTANCE_2 = 20;

    public static StationLineRequest createMockRequest1() {
        return  new StationLineRequest(
                    StationLineMockData.NAME_1,
                    StationLineMockData.COLOR_1,
                    StationLineMockData.UP_STATION_ID_1,
                    StationLineMockData.DOWN_STATION_ID_1,
                    StationLineMockData.DISTANCE_1);
    }

    public static StationLineRequest createMockRequest2() {
        return  new StationLineRequest(
                StationLineMockData.NAME_2,
                StationLineMockData.COLOR_2,
                StationLineMockData.UP_STATION_ID_2,
                StationLineMockData.DOWN_STATION_ID_2,
                StationLineMockData.DISTANCE_2);
    }


}
