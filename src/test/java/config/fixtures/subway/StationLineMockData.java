package config.fixtures.subway;

import subway.dto.StationLineRequest;

public class StationLineMockData {

    public static final String stationLineName1 = "신분당선";
    public static final String stationLineColor1 = "bg-red-600";
    public static final Long upStationId1 = 1L;
    public static final Long downStationId1 = 2L;
    public static final Long distance1 = 10L;

    public static final String stationLineName2 = "분당선";
    public static final String stationLineColor2 = "bg-green-600";
    public static final Long upStationId2 = 2L;
    public static final Long downStationId2 = 4L;
    public static final Long distance2 = 20L;

    public static StationLineRequest createMockRequest1() {
        return  new StationLineRequest(
                    StationLineMockData.stationLineName1,
                    StationLineMockData.stationLineColor1,
                    StationLineMockData.upStationId1,
                    StationLineMockData.downStationId1,
                    StationLineMockData.distance1);
    }


}
