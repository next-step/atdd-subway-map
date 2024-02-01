package config.fixtures.subway;

import subway.dto.StationLineRequest;

public class StationLineMockData {

    public static final StationLineRequest 신분당선 =
            new StationLineRequest("신분당선", "bg-red-600", 1L, 2L, 10);
    public static final StationLineRequest 분당선 =
            new StationLineRequest("신분당선", "bg-green-600", 2L, 4L, 20);

    public static final StationLineRequest 수정된_신분당선 =
            new StationLineRequest("수정된_신분당선", "bg-blue-100", 1L, 2L, 10);

    public static StationLineRequest 호남선_생성(Long upStationId, Long downStationId) {
        return new StationLineRequest("호남선", "bg-blue-100", upStationId, downStationId, 10);
    };
}
