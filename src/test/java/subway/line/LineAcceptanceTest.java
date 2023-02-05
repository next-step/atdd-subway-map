package subway.line;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import subway.common.DatabaseCleanser;
import subway.line.dto.LineResponse;
import subway.station.dto.StationResponse;
import subway.utils.RestAssuredClient;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Map.entry;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("지하철 노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // RandomPort 사용하는 이유, 각 Port는 언제 사용하면 좋은지
public class LineAcceptanceTest {

    @LocalServerPort
    int port;

    @Autowired
    private DatabaseCleanser databaseCleanser;

    @BeforeEach
    void init() {
        RestAssured.port = port;
        databaseCleanser.execute();

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
        var newLine = RestAssuredClient.createLine(
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
                () -> assertEquals(newLine.jsonPath().getLong("id"), 1),
                () -> assertEquals(newLine.jsonPath().getString("name"),"신분당선"),
                () -> assertEquals(newLine.jsonPath().getString("color"), "bg-red-600"),
                () -> {
                    assertThat(
                            newLine.jsonPath()
                                    .getList("stations", StationResponse.class)
                                    .stream().map(StationResponse::getName)
                                    .collect(Collectors.toList())
                    ).containsExactly("강남역", "신논현역");
                }
        );

        // then
        List<LineResponse> lines = RestAssuredClient.listLine().jsonPath().getList("$", LineResponse.class);

        assertAll(
                () -> assertEquals(1, lines.size()),
                () -> assertEquals(1, lines.get(0).getId()),
                () -> assertEquals("신분당선", lines.get(0).getName()),
                () -> assertEquals("bg-red-600", lines.get(0).getColor()),
                () -> assertEquals(1L, lines.get(0).getStations().get(0).getId()),
                () -> assertEquals(2L, lines.get(0).getStations().get(1).getId())
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
        List<LineResponse> lines = RestAssuredClient.listLine().jsonPath().getList("$", LineResponse.class);

        // then
        assertAll(
                () -> assertEquals(2, lines.size()),
                () -> assertEquals(1, lines.get(0).getId()),
                () -> assertEquals("신분당선", lines.get(0).getName()),
                () -> assertEquals("bg-red-600", lines.get(0).getColor()),
                () -> assertEquals(1L, lines.get(0).getStations().get(0).getId()),
                () -> assertEquals(2L, lines.get(0).getStations().get(1).getId()),
                () -> assertEquals(2, lines.get(1).getId()),
                () -> assertEquals("분당선", lines.get(1).getName()),
                () -> assertEquals("bg-green-600", lines.get(1).getColor()),
                () -> assertEquals(1L, lines.get(1).getStations().get(0).getId()),
                () -> assertEquals(3L, lines.get(1).getStations().get(1).getId())
        );
    }

    /*
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     * */
    @DisplayName("지하철 노선 조회")
    @Test
    void getLine() {
        // given
        RestAssuredClient.createLine(
                Map.ofEntries(
                        entry("name", "신분당선"),
                        entry("color", "bg-red-600"),
                        entry("upStationId", 1),
                        entry("downStationId", 2),
                        entry("distance", 10)
                )
        );

        // when
        var line = RestAssuredClient.findLine(1L).as(LineResponse.class);

        // then
        assertAll(
                () -> assertNotNull(line),
                () -> assertEquals(1, line.getId()),
                () -> assertEquals("신분당선", line.getName()),
                () -> assertEquals("bg-red-600", line.getColor()),
                () -> assertEquals(1L, line.getStations().get(0).getId()),
                () -> assertEquals(2L, line.getStations().get(1).getId())
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
        RestAssuredClient.createLine(
                Map.ofEntries(
                        entry("name", "신분당선"),
                        entry("color", "bg-red-600"),
                        entry("upStationId", 1),
                        entry("downStationId", 2),
                        entry("distance", 10)
                )
        );

        var line = RestAssuredClient.findLine(1L).as(LineResponse.class);
        assertAll(
                () -> assertEquals(1, line.getId()),
                () -> assertEquals("신분당선", line.getName()),
                () -> assertEquals("bg-red-600", line.getColor())
        );

        // when
        RestAssuredClient.updateLine(
                1L,
                Map.ofEntries(
                        entry("name", "다른분당선"),
                        entry("color", "bg-blue-600")
                )
        );

        // then
        var updatedLine = RestAssuredClient.findLine(1L).as(LineResponse.class);
        assertAll(
                () -> assertNotNull(updatedLine),
                () -> assertEquals(1, updatedLine.getId()),
                () -> assertEquals("다른분당선", updatedLine.getName()),
                () -> assertEquals("bg-blue-600", updatedLine.getColor())
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
        RestAssuredClient.createLine(
                Map.ofEntries(
                        entry("name", "신분당선"),
                        entry("color", "bg-red-600"),
                        entry("upStationId", 1),
                        entry("downStationId", 2),
                        entry("distance", 10)
                )
        );

        // when
        var deleted = RestAssuredClient.deleteLine(1L);
        assertEquals(HttpStatus.NO_CONTENT.value(), deleted.statusCode());

        // then
        var line = RestAssuredClient.findLine(1L);
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
                RestAssuredClient.createStation(station);
            }
        }

        private static void createLines(List<Map<String, Object>> lines) {
            for (var line: lines) {
                RestAssuredClient.createLine(line);
            }
        }
    }
}
