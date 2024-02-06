package config.fixtures.subway;

import subway.dto.StationSectionRequest;
import subway.entity.StationSection;

public class StationSectionMockData {

    public static StationSectionRequest 지하철_구간(Long upStationId, Long downStationId, int distance) {
        return new StationSectionRequest(upStationId, downStationId, distance);
    }

    public static final StationSection 강남_교대 = new StationSection(2L, 3L, 10);
    public static final StationSection 교대_서초 = new StationSection(3L, 4L, 10);}
