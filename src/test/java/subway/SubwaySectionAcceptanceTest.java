package subway;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.lines.Line;
import subway.lines.LineCreateRequest;
import subway.lines.LineRepository;
import subway.lines.LineService;
import subway.station.Station;
import subway.station.StationRepository;
import subway.station.StationResponse;

@DisplayName("지하철 구간 테스트")
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
public class SubwaySectionAcceptanceTest {

    Long downStationId;
    Long extraStationId;
    Long lineId;

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private LineService lineService;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @BeforeEach
    void setUp() {
        final Long upStationId = stationRepository.save(new Station("역1")).getId();
        downStationId = stationRepository.save(new Station("역2")).getId();
        extraStationId = stationRepository.save(new Station("역3")).getId();

        lineId = lineService
            .saveLines(
                new LineCreateRequest(
                    "노선1",
                    "색1",
                    upStationId,
                    downStationId,
                    10L
                ))
            .getId();
    }

    @AfterEach
    void cleanUp() {
        databaseCleaner.tableClear();
    }

    /**
     * When 지하철 구간을 추가하면
     * Then 지하철 노선 조회시 추가된 구간을 확인할 수 있다.
     */
    @Test
    void 지하철구간_추가() {
        // When
        final ExtractableResponse<Response> response = addSections(
            lineId,
            downStationId,
            extraStationId,
            10L
        );

        // Then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        // Then
        final List<Long> stationIdList = response
            .jsonPath()
            .getList("stations", StationResponse.class)
            .stream()
            .map(StationResponse::getId)
            .collect(Collectors.toList());

        assertThat(stationIdList).containsAnyOf(extraStationId);
    }

    /**
     * When 지하철 구간이 하행선이 아닌 곳에서 추가하면
     * Then 400 에러가 리턴된다.
     */
    @Test
    void 지하철구간_추가_하행선아닌_경우() {

    }

    /**
     * Given 3개의 역이 포함된 지하철 역이 주어지고
     * When 하행 종점역을 삭제하면
     * Then 정상 삭제가 된다.
     */
    @Test
    void 지하철구간_종점_제거() {

    }

    /**
     * Given 2개의 역이 포함된 지하철 역이 주어지고
     * When 하행 종점역을 삭제하면
     * Then 에러가 발생한다.
     */
    @Test
    void 지하철구간_종점제거_2개역_존재_경우() {

    }

    /**
     * Given 3개의 역이 포함된 지하철 역이 주어지고
     * When 하행 종점역이 아닌 역을 삭제하면
     * Then 에러가 발생한다.
     */
    @Test
    void 지하철구간_종점제거_하행종점역이_아닌_경우() {

    }

    private ExtractableResponse<Response> addSections(Long lineId, Long downStationId, Long upStationId, Long distance) {
        final Map<String, String> params = new HashMap<>();
        params.put("downStationId", downStationId.toString());
        params.put("upStationId", upStationId.toString());
        params.put("distance", distance.toString());

        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(params)
            .when()
            .post("/lines/" + lineId + "/sections")
            .then().log().all()
            .extract();
    }
}
