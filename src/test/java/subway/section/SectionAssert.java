package subway.section;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.line.LineRestAssured.Location_조회;
import static subway.line.LineRestAssured.노선_조회;

import io.restassured.path.json.JsonPath;

public class SectionAssert {
    
    public static void 구간_등록_검증(final Long lineId, final Long stationId) {
        JsonPath jsonPath = 노선_조회(lineId).jsonPath();

        assertThat(jsonPath.getLong("stations.id[1]")).isEqualTo(stationId);
    }

    public static void 구간_조회_검증(final String location, final Long downStationId) {
        JsonPath jsonPath = Location_조회(location).jsonPath();

        assertThat(jsonPath.getLong("stations.id[1]")).isEqualTo(downStationId);
    }
}
