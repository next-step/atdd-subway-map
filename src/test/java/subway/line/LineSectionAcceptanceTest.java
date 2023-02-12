package subway.line;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import subway.utils.DatabaseCleanser;
import subway.station.StationRestAssuredClient;

import java.util.List;
import java.util.Map;

import static java.util.Map.entry;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;


@DisplayName("지하철 구간 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // RandomPort 사용하는 이유, 각 Port는 언제 사용하면 좋은지
public class LineSectionAcceptanceTest {

    @LocalServerPort
    int port;

    @Autowired
    private DatabaseCleanser databaseCleanser;

    @BeforeEach
    void init() {
        RestAssured.port = port;
        databaseCleanser.execute();

        Fixture.createStations();
        Fixture.createLines();
    }

    /**
     * Given 2개의 지하철역(석촌역, 신논현역)을 가진 노선(9호선)이 있을때
     * When 새로운 구간(신논현역 - 논현역)을 추가하고
     *  And 지하철 노선(9호선)에 새로운 구간을 추가하면
     * Then 지하철 노선 조회시 변경된 지하철 노선(석촌역 - 신논현역 - 논현역)을 찾을 수 있다
     * */
    @DisplayName("지하철 노선에 구간 추가")
    @Test
    void addLineSection() {
        // given
        var beforeLines = LineRestAssuredClient.listLine();
        assertAll(
                () -> assertThat(beforeLines.jsonPath().getList("$").size()).isEqualTo(1),
                () -> assertThat(beforeLines.jsonPath().getLong("[0].id")).isEqualTo(1),
                () -> assertThat(beforeLines.jsonPath().getString("[0].name")).isEqualTo("9호선"),
                () -> assertThat(beforeLines.jsonPath().getString("[0].color")).isEqualTo("bg-red-600"),
                () -> assertThat(beforeLines.jsonPath().getMap("[0].stations[0]")).containsExactly(
                        entry("id", 1),
                        entry("name", "석촌역")
                ),
                () -> assertThat(beforeLines.jsonPath().getMap("[0].stations[1]")).containsExactly(
                        entry("id", 2),
                        entry("name", "신논현역")
                )
        );

        // when
        var addedLineSection = LineRestAssuredClient.addSection(
                1L,
                Map.ofEntries(
                        entry("downStationId", 4),
                        entry("upStationId", 2),
                        entry("distance", 10)
                )
        );
        // then
        assertThat(addedLineSection.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        var afterLines = LineRestAssuredClient.listLine();
        assertAll(
                () -> assertThat(afterLines.jsonPath().getList("$").size()).isEqualTo(1),
                () -> assertThat(afterLines.jsonPath().getLong("[0].id")).isEqualTo(1),
                () -> assertThat(afterLines.jsonPath().getString("[0].name")).isEqualTo("9호선"),
                () -> assertThat(afterLines.jsonPath().getString("[0].color")).isEqualTo("bg-red-600"),
                () -> assertThat(afterLines.jsonPath().getMap("[0].stations[0]")).containsExactly(
                        entry("id", 1),
                        entry("name", "석촌역")
                ),
                () -> assertThat(afterLines.jsonPath().getMap("[0].stations[1]")).containsExactly(
                        entry("id", 2),
                        entry("name", "신논현역")
                ),
                () -> assertThat(afterLines.jsonPath().getMap("[0].stations[2]")).containsExactly(
                        entry("id", 4),
                        entry("name", "동작역")
                )
        );
    }

    /**
     * Given 지하철 노선 9호선 (석촌역 - 신논현역 - 논현역)이 있을때,
     * When 지하철 노선에 하행종점역(논현역)을 제거하면,
     * Then 변경된 지하철 노선(석촌역 - 신논현역)을 찾을 수 있다
     * */
    @DisplayName("지하철 구간 삭제")
    @Test
    void deleteSection() {
        // given
        var lineId = 1L;
        Fixture.addLineSection(lineId);

        var beforeLines = LineRestAssuredClient.listLine();
        assertAll(
                () -> assertThat(beforeLines.jsonPath().getList("$").size()).isEqualTo(1),
                () -> assertThat(beforeLines.jsonPath().getLong("[0].id")).isEqualTo(1),
                () -> assertThat(beforeLines.jsonPath().getString("[0].name")).isEqualTo("9호선"),
                () -> assertThat(beforeLines.jsonPath().getString("[0].color")).isEqualTo("bg-red-600"),
                () -> assertThat(beforeLines.jsonPath().getMap("[0].stations[0]")).containsExactly(
                        entry("id", 1),
                        entry("name", "석촌역")
                ),
                () -> assertThat(beforeLines.jsonPath().getMap("[0].stations[1]")).containsExactly(
                        entry("id", 2),
                        entry("name", "신논현역")
                ),
                () -> assertThat(beforeLines.jsonPath().getMap("[0].stations[2]")).containsExactly(
                        entry("id", 4),
                        entry("name", "동작역")
                )
        );

        // when
        var stationId = 4L;
        var deletedLine = LineRestAssuredClient.deleteSection(lineId, stationId);
        assertThat(deletedLine.statusCode()).isEqualTo(HttpStatus.OK.value());

        // then
        var afterLines = LineRestAssuredClient.listLine();
        assertAll(
                () -> assertThat(afterLines.jsonPath().getList("$").size()).isEqualTo(1),
                () -> assertThat(afterLines.jsonPath().getLong("[0].id")).isEqualTo(1),
                () -> assertThat(afterLines.jsonPath().getString("[0].name")).isEqualTo("9호선"),
                () -> assertThat(afterLines.jsonPath().getString("[0].color")).isEqualTo("bg-red-600"),
                () -> assertThat(afterLines.jsonPath().getMap("[0].stations[0]")).containsExactly(
                        entry("id", 1),
                        entry("name", "석촌역")
                ),
                () -> assertThat(afterLines.jsonPath().getMap("[0].stations[1]")).containsExactly(
                        entry("id", 2),
                        entry("name", "신논현역")
                )
        );
    }

    private static class Fixture {
        private static final List<Map<String, Object>> sections = List.of(
                Map.ofEntries(
                        entry("upStationId", 4),
                        entry("downStationId", 3),
                        entry("distance", 6)
                )
        );

        private static final List<Map<String, Object>> lines =  List.of(
                Map.ofEntries(
                        entry("name", "9호선"),
                        entry("color", "bg-red-600"),
                        entry("upStationId", 1),
                        entry("downStationId", 2),
                        entry("distance", 10)
                )
        );

        private static final List<Map<String, Object>> stations =  List.of(
                Map.ofEntries(entry("name", "석촌역")),
                Map.ofEntries(entry("name", "신논현역")),
                Map.ofEntries(entry("name", "언주역")),
                Map.ofEntries(entry("name", "동작역"))
        );

        private static void createLines() {
            for (var line: lines) {
                LineRestAssuredClient.createLine(line);
            }
        }

        private static void createStations() {
            for (var station : stations) {
                StationRestAssuredClient.createStation(station);
            }
        }

        public static void addLineSection(long lineId) {
            for (var section : sections) {
                LineRestAssuredClient.addSection(lineId, section);
            }
        }
    }
}
