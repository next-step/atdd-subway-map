package support.fixture;

import java.util.Map;

public class SectionFixture {

    public static Map<String, Object> 구간_생성(Long downStationId, Long upStationId, Long distance) {
        return Map.of(
            "downStationId", downStationId,
            "upStationId", upStationId,
            "distance", distance
        );
    }
}


