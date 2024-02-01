package config.fixtures.subway;

import subway.dto.StationSectionRequest;

public class StationSectionMockData {

    public static StationSectionRequest 지하철_구간(Long upStationId, Long downStationId, int distance) {
        return new StationSectionRequest(upStationId, downStationId, distance);
    }
}
