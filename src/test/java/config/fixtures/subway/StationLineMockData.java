package config.fixtures.subway;

import subway.dto.StationLineRequest;

public class StationLineMockData {

    public static final StationLineRequest 신분당선 =
            new StationLineRequest("신분당선", "bg-red-600", 1, 2, 10);
    public static final StationLineRequest 분당선 =
            new StationLineRequest("신분당선", "bg-green-600", 2, 4, 20);

    public static final StationLineRequest 수정된_신분당선 =
            new StationLineRequest("수정된_신분당선", "bg-blue-100", 1, 2, 10);
}
