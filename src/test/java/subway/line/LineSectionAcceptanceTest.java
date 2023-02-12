package subway.line;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import subway.AcceptanceTest;
import subway.station.StationRestAssuredClient;

import java.util.List;
import java.util.Map;

import static java.util.Map.entry;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("지하철 구간 관련 기능")
public class LineSectionAcceptanceTest extends AcceptanceTest {

    private long lineId;

    private long upStationId;

    private long downStationId;


    @BeforeEach
    void init() {
        upStationId = Fixture.createStation("강남역").jsonPath().getLong("id");
        downStationId = Fixture.createStation("신논현역").jsonPath().getLong("id");
        lineId = Fixture.createLine("9호선", upStationId, downStationId).jsonPath().getLong("id");
    }

    /**
     * Given 지하철 노선 9호선 (1석촌역 - 2신논현역)이 있을때,
     * When 지하철 노선에 새로운 구간(2신논현역-3동작역)을 추가하면
     * Then 변경된 지하철 노선(1석촌역 - 2신논현역 - 3동작역)이 조회된다
     * */
    @DisplayName("지하철 노선에 구간 추가")
    @Test
    void addLineSection() {
        // given
        var newStationId = Fixture.createStation("동작역").jsonPath().getLong("id");

        // when
        var addedLineSection = Fixture.addSection(lineId, downStationId, newStationId);

        // then
        assertThat(addedLineSection.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        var afterLines = LineRestAssuredClient.listLine();
        Fixture.assertLineSectionEquals(afterLines, 1, this.lineId, "9호선", "bg-red-600", List.of(
                Pair.of(1L, "강남역"),
                Pair.of(2L, "신논현역"),
                Pair.of(3L, "동작역")
        ));
    }

    /**
     * Given 지하철 노선 9호선 (1석촌역 - 2신논현역 - 3동작역)이 있을때,
     * When 지하철 노선에 하행종점역(3동작역)을 제거하면,
     * Then 변경된 지하철 노선(1석촌역 - 2신논현역)이 조회된다.
     * */
    @DisplayName("지하철 구간 삭제")
    @Test
    void deleteSection() {
        // given
        var newStationId = Fixture.createStation("동작역").jsonPath().getLong("id");

        Fixture.addSection(lineId, downStationId, newStationId);

        // when
        var deletedLine = LineRestAssuredClient.deleteLineSection(lineId, 3L);
        assertThat(deletedLine.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        // then
        var afterLines = LineRestAssuredClient.listLine();
        Fixture.assertLineSectionEquals(afterLines, 1, lineId, "9호선", "bg-red-600", List.of(
                Pair.of(1L, "강남역"),
                Pair.of(2L, "신논현역")
        ));
    }

    private static class Fixture {
        private static ExtractableResponse<Response> addSection(long lineId, long downStationId, long newStationId) {
            return LineRestAssuredClient.addLineSection(
                    lineId,
                    Map.ofEntries(
                            entry("upStationId", downStationId),
                            entry("downStationId", newStationId),
                            entry("distance", 10)
                    )
            );
        }

        private static ExtractableResponse<Response> createLine(String name, Long upStationId, Long downStationId) {
            Map<String, Object> line =  Map.ofEntries(
                    entry("name", name),
                    entry("color", "bg-red-600"),
                    entry("upStationId", upStationId),
                    entry("downStationId", downStationId),
                    entry("distance", 10)
            );
            return LineRestAssuredClient.createLine(line);
        }

        private static ExtractableResponse<Response> createStation(String name) {
            return StationRestAssuredClient.createStation(
                    Map.of("name", name)
            );
        }

        private static void assertLineSectionEquals(ExtractableResponse<Response> lines, int size, long lineId, String name, String color, List<Pair<Long, String>> stations) {
            assertAll(
                    () -> assertThat(lines.jsonPath().getList("$").size()).isEqualTo(size),
                    () -> assertThat(lines.jsonPath().getLong("[0].id")).isEqualTo(lineId),
                    () -> assertThat(lines.jsonPath().getString("[0].name")).isEqualTo(name),
                    () -> assertThat(lines.jsonPath().getString("[0].color")).isEqualTo(color)
            );
            for (int i = 0; i < stations.size(); i++) {
                assertThat(lines.jsonPath().getLong("[0].stations["+i+"].id")).isEqualTo(stations.get(i).getFirst());
                assertThat(lines.jsonPath().getString("[0].stations["+i+"].name")).isEqualTo(stations.get(i).getSecond());
            };
        }
    }
}
