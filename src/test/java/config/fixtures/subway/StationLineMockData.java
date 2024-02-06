package config.fixtures.subway;

import subway.dto.StationLineRequest;
import subway.entity.StationLine;

public class StationLineMockData {

    public static final StationLineRequest 신분당선 =
            new StationLineRequest("신분당선", "bg-red-600", 1L, 2L, 10);
    public static final StationLineRequest 분당선 =
            new StationLineRequest("분당선", "bg-green-600", 2L, 4L, 20);

    public static final StationLineRequest 신림선 =
            new StationLineRequest("신림선", "bg-blue-600", 6L, 10L, 20);

    public static final StationLineRequest 수정된_신분당선 =
            new StationLineRequest("수정된_신분당선", "bg-blue-100", 1L, 2L, 10);

    public static StationLineRequest 호남선_생성(Long upStationId, Long downStationId) {
        return new StationLineRequest("호남선", "bg-blue-100", upStationId, downStationId, 10);
    };

    public static final StationLine 이호선 =
            new StationLine("이호선", "bg-green-600", 1L, 2L, 20);
}
