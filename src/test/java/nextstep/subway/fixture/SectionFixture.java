package nextstep.subway.fixture;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.utils.HttpRequestResponse.post;

public class SectionFixture {

    public static Map<String, String> 구간(Long upStationId, Long downStationId, int distance) {
        Map<String, String> section = new HashMap<>();
        section.put("upStationId", String.valueOf(upStationId));
        section.put("downStationId", String.valueOf(downStationId));
        section.put("distance", String.valueOf(distance));
        return section;
    }
}
