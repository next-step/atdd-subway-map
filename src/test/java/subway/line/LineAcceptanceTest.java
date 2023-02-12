package subway.line;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
        assertAll(
                () -> assertNotNull(newLine.jsonPath().get()),
                () -> assertThat(newLine.jsonPath().getLong("id")).isEqualTo(1),
                () -> assertThat(newLine.jsonPath().getString("name")).isEqualTo("신분당선"),
                () -> assertThat(newLine.jsonPath().getString("color")).isEqualTo("bg-red-600"),
                () -> assertThat(newLine.jsonPath().getLong("stations[0].id")).isEqualTo(1),
                () -> assertThat(newLine.jsonPath().getString("stations[0].name")).isEqualTo("강남역"),
                () -> assertThat(newLine.jsonPath().getLong("stations[1].id")).isEqualTo(2),
                () -> assertThat(newLine.jsonPath().getString("stations[1].name")).isEqualTo("신논현역")
        );

        // then
        var lines = LineRestAssuredClient.listLine();
        assertAll(
                () -> assertThat(lines.jsonPath().getList("$").size()).isEqualTo(1),
                () -> assertThat(lines.jsonPath().getLong("[0].id")).isEqualTo(1),
                () -> assertThat(lines.jsonPath().getString("[0].name")).isEqualTo("신분당선"),
                () -> assertThat(lines.jsonPath().getString("[0].color")).isEqualTo("bg-red-600"),
                () -> assertThat(lines.jsonPath().getLong("[0].stations[0].id")).isEqualTo(1),
                () -> assertThat(lines.jsonPath().getString("[0].stations[0].name")).isEqualTo("강남역"),
                () -> assertThat(lines.jsonPath().getLong("[0].stations[1].id")).isEqualTo(2),
                () -> assertThat(lines.jsonPath().getString("[0].stations[1].name")).isEqualTo("신논현역")
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
        assertAll(
                () -> assertThat(lines.jsonPath().getList("$").size()).isEqualTo(2),
                () -> assertThat(lines.jsonPath().getLong("[0].id")).isEqualTo(1),
                () -> assertThat(lines.jsonPath().getString("[0].name")).isEqualTo("신분당선"),
                () -> assertThat(lines.jsonPath().getString("[0].color")).isEqualTo("bg-red-600"),
                () -> assertThat(lines.jsonPath().getMap("[0].stations[0]")).containsExactly(
                        entry("id", 1),
                        entry("name", "강남역")
                ),
                () -> assertThat(lines.jsonPath().getMap("[0].stations[1]")).containsExactly(
                        entry("id", 2),
                        entry("name", "신논현역")
                ),
                () -> assertThat(lines.jsonPath().getLong("[1].id")).isEqualTo(2),
                () -> assertThat(lines.jsonPath().getString("[1].name")).isEqualTo( "분당선"),
                () -> assertThat(lines.jsonPath().getString("[1].color")).isEqualTo( "bg-green-600"),
                () -> assertThat(lines.jsonPath().getMap("[1].stations[0]")).containsExactly(
                        entry("id", 1),
                        entry("name", "강남역")
                ),
                () -> assertThat(lines.jsonPath().getMap("[1].stations[1]")).containsExactly(
                        entry("id", 3),
                        entry("name", "석촌역")
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
        assertAll(
                () -> assertNotNull(line.jsonPath().get()),
                () -> assertThat(line.jsonPath().getLong("id")).isEqualTo(1),
                () -> assertThat(line.jsonPath().getString("name")).isEqualTo("신분당선"),
                () -> assertThat(line.jsonPath().getString("color")).isEqualTo("bg-red-600"),
                () -> assertThat(line.jsonPath().getLong("stations[0].id")).isEqualTo(1),
                () -> assertThat(line.jsonPath().getString("stations[0].name")).isEqualTo("강남역"),
                () -> assertThat(line.jsonPath().getLong("stations[1].id")).isEqualTo(2),
                () -> assertThat(line.jsonPath().getString("stations[1].name")).isEqualTo("신논현역")
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
        assertAll(
                () -> assertThat(line.jsonPath().getLong("id")).isEqualTo(1),
                () -> assertThat(line.jsonPath().getString("name")).isEqualTo("신분당선"),
                () -> assertThat(line.jsonPath().getString("color")).isEqualTo("bg-red-600")
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
        assertAll(
                () -> assertNotNull(updatedLine),
                () -> assertThat(updatedLine.jsonPath().getLong("id")).isEqualTo(1),
                () -> assertThat(updatedLine.jsonPath().getString("name")).isEqualTo("다른분당선"),
                () -> assertThat(updatedLine.jsonPath().getString("color")).isEqualTo("bg-blue-600")
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
    }
}
