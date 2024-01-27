package config.fixtures.subway;

import subway.dto.StationLineRequest;

public class StationLineMockData {

    public static final String stationLineName1 = "신분당선";
    public static final String stationLineColor1 = "bg-red-600";
    public static final int upStationId1 = 1;
    public static final int downStationId1 = 2;
    public static final int distance1 = 10;

    public static final String stationLineName2 = "분당선";
    public static final String stationLineColor2 = "bg-green-600";
    public static final int upStationId2 = 2;
    public static final int downStationId2 = 4;
    public static final int distance2 = 20;

    public static StationLineRequest createMockRequest1() {
        return  new StationLineRequest(
                    StationLineMockData.stationLineName1,
                    StationLineMockData.stationLineColor1,
                    StationLineMockData.upStationId1,
                    StationLineMockData.downStationId1,
                    StationLineMockData.distance1);
    }


}
