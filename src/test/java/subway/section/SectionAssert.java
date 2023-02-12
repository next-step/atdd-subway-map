package subway.section;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.line.LineRestAssured.Location_조회;
import static subway.section.SectionRestAssured.구간_조회;

import io.restassured.path.json.JsonPath;

public class SectionAssert {
    
    public static void 구간_등록_검증(final Long lineId, final Long stationId) {
        JsonPath jsonPath = 구간_조회(lineId, stationId).jsonPath();

        assertThat(jsonPath.getLong("downStationId")).isEqualTo(stationId);
    }

    public static void 구간_조회_검증(final String location, final Long downStationId) {
        JsonPath jsonPath = Location_조회(location).jsonPath();

        assertThat(jsonPath.getLong("stations.id[1]")).isEqualTo(downStationId);
    }
}
