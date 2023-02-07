package subway.section;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.line.LineRestAssured.노선_조회;

import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Assertions;

public class SectionAssert {

    /**
     * When 지하철 노선에 새로운 구간을 등록하면
     * Then 새로운 구간은 노선에 등록되어있는 하행 종점역이어야 한다.
     * And 새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이어야 한다.
     */
    public static void 구간_등록_검증(final Long lineId, final Long id, final String name, final Long upStationId) {
        JsonPath jsonPath = 노선_조회(lineId).jsonPath();

        Assertions.assertAll(
                () -> assertThat(jsonPath.getLong("stations.id[1]")).isEqualTo(id),
                () -> assertThat(jsonPath.getString("stations.name[1]")).isEqualTo(name),
                () -> assertThat(jsonPath.getLong("stations.upStationId[1]")).isEqualTo(upStationId)
        );
    }
}
