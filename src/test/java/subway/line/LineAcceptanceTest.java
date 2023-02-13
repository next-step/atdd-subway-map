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
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {
    @BeforeEach
    void init() {
        Fixture.createStations();
    }

    /*
    * When 지하철 노선을 생성하면
    * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
    * */
    @DisplayName("지하철 노선 생성")
    @Test
    void createLine() {
        // when
        var newLine = LineRestAssuredClient.createLine(
                Map.ofEntries(
                        entry("name", "신분당선"),
                        entry("color", "bg-red-600"),
                        entry("upStationId", 1),
                        entry("downStationId", 2),
                        entry("distance", 10)
                )
        );

        // then
        Fixture.assertLineEqual(
                newLine,
                1L,
                "신분당선",
                "bg-red-600",
                List.of(
                        Pair.of(1L, "강남역"),
                        Pair.of(2L, "신논현역")
                )
        );

        // then
        var lines = LineRestAssuredClient.listLine();

        Fixture.assertLineListEqual(lines, 0,
                1L,
                "신분당선",
                "bg-red-600",
                List.of(
                        Pair.of(1L, "강남역"),
                        Pair.of(2L, "신논현역")
                )
        );
    }

    /*
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     * */
    @DisplayName("지하철 노선 목록 조회")
    @Test
    void listLine() {
        // given
        Fixture.createLines(
                List.of(
                        Map.ofEntries(
                                entry("name", "신분당선"),
                                entry("color", "bg-red-600"),
                                entry("upStationId", 1),
                                entry("downStationId", 2),
                                entry("distance", 10)
                        ),
                        Map.ofEntries(
                                entry("name", "분당선"),
                                entry("color", "bg-green-600"),
                                entry("upStationId", 1),
                                entry("downStationId", 3),
                                entry("distance", 10)
                        )
                )
        );

        // when
        var lines = LineRestAssuredClient.listLine();

        // then
        Fixture.assertLineListEqual(lines, 0,
                1L,
                "신분당선",
                "bg-red-600",
                List.of(
                        Pair.of(1L, "강남역"),
                        Pair.of(2L, "신논현역")
                )
        );

        Fixture.assertLineListEqual(lines, 1,
                2L,
                "분당선",
                "bg-green-600",
                List.of(
                        Pair.of(1L, "강남역"),
                        Pair.of(3L, "석촌역")
                )
        );
    }

    /*
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     * */
    @DisplayName("지하철 노선 조회")
    @Test
    void findLine() {
        // given
        LineRestAssuredClient.createLine(
                Map.ofEntries(
                        entry("name", "신분당선"),
                        entry("color", "bg-red-600"),
                        entry("upStationId", 1),
                        entry("downStationId", 2),
                        entry("distance", 10)
                )
        );

        // when
        var line = LineRestAssuredClient.findLine(1L);

        // then
        Fixture.assertLineEqual(
                line,
                1L,
                "신분당선",
                "bg-red-600",
                List.of(
                        Pair.of(1L, "강남역"),
                        Pair.of(2L, "신논현역")
                )
        );
    }

    /*
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     * */
    @DisplayName("지하철 노선 수정")
    @Test
    void updateLine() {
        // given
        LineRestAssuredClient.createLine(
                Map.ofEntries(
                        entry("name", "신분당선"),
                        entry("color", "bg-red-600"),
                        entry("upStationId", 1),
                        entry("downStationId", 2),
                        entry("distance", 10)
                )
        );

        var line = LineRestAssuredClient.findLine(1L);

        Fixture.assertLineEqual(
                line,
                1L,
                "신분당선",
                "bg-red-600",
                List.of(
                        Pair.of(1L, "강남역"),
                        Pair.of(2L, "신논현역")
                )
        );

        // when
        LineRestAssuredClient.updateLine(1L,
                Map.ofEntries(
                        entry("name", "다른분당선"),
                        entry("color", "bg-blue-600")
                )
        );

        // then
        var updatedLine = LineRestAssuredClient.findLine(1L);

        Fixture.assertLineEqual(
                updatedLine,
                1L,
                "다른분당선",
                "bg-blue-600",
                List.of(
                        Pair.of(1L, "강남역"),
                        Pair.of(2L, "신논현역")
                )
        );
    }

    /*
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     * */
    @DisplayName("지하철 노선 삭제")
    @Test
    void deleteLine() {
        // given
        LineRestAssuredClient.createLine(
                Map.ofEntries(
                        entry("name", "신분당선"),
                        entry("color", "bg-red-600"),
                        entry("upStationId", 1),
                        entry("downStationId", 2),
                        entry("distance", 10)
                )
        );

        // when
        var deleted = LineRestAssuredClient.deleteLine(1L);
        assertEquals(HttpStatus.NO_CONTENT.value(), deleted.statusCode());

        // then
        var line = LineRestAssuredClient.findLine(1L);
        assertEquals(HttpStatus.NOT_FOUND.value(), line.statusCode());
    }

    private static class Fixture {
        private static final List<Map<String, Object>> stations =  List.of(
                Map.ofEntries(entry("name", "강남역")),
                Map.ofEntries(entry("name", "신논현역")),
                Map.ofEntries(entry("name", "석촌역")),
                Map.ofEntries(entry("name", "잠실역"))
        );

        private static void createStations() {
            for (var station : stations) {
                StationRestAssuredClient.createStation(station);
            }
        }

        private static void createLines(List<Map<String, Object>> lines) {
            for (var line: lines) {
                LineRestAssuredClient.createLine(line);
            }
        }

        private static void assertLineEqual(ExtractableResponse<Response> line, long lineId, String name, String color, List<Pair<Long, String>> stations) {
            assertNotNull(line.jsonPath().get());
            assertThat(line.jsonPath().getLong("id")).isEqualTo(lineId);
            assertThat(line.jsonPath().getString("name")).isEqualTo(name);
            assertThat(line.jsonPath().getString("color")).isEqualTo(color);

            for (int i = 0; i < stations.size(); i++) {
                assertThat(line.jsonPath().getLong("stations[" + i + "].id")).isEqualTo(stations.get(i).getFirst());
                assertThat(line.jsonPath().getString("stations[" + i + "].name")).isEqualTo(stations.get(i).getSecond());
            }
        }

        private static void assertLineListEqual(ExtractableResponse<Response> lines, int index, long lineId, String name, String color, List<Pair<Long, String>> stations) {
            assertThat(lines.jsonPath().getLong("[" + index + "].id")).isEqualTo(lineId);
            assertThat(lines.jsonPath().getString("[" + index + "].name")).isEqualTo(name);
            assertThat(lines.jsonPath().getString("[" + index + "].color")).isEqualTo(color);

            for (int i = 0; i < stations.size(); i++) {
                assertThat(lines.jsonPath().getLong("[" + index + "].stations[" + i + "].id")).isEqualTo(stations.get(i).getFirst());
                assertThat(lines.jsonPath().getString("[" + index + "].stations[" + i + "].name")).isEqualTo(stations.get(i).getSecond());
            }
        }
    }
}
