package subway.section;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.apache.groovy.util.Maps;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import subway.line.LineAcceptanceTest;
import subway.line.LineRequest;
import subway.line.LineResponse;
import subway.station.StationAcceptanceTest;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 구간 관련 기능")
@Sql(statements = "TRUNCATE TABLE STATION;", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class SectionAcceptanceTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        StationAcceptanceTest.requestSaveStation("강남역", "망포역", "정자역", "판교역");
    }

    /**
     * Scenario: 노선 하행 종점역에 새로운 구간을 등록한다.
     * Given 노선을 생성하고
     * When 노선의 하행 종점역에 새로운 구간을 등록하면
     * Then 구간이 등록된다.
     * Then 노선의 구간 목록에서 조회할 수 있다.
     */
    @Test
    void 노선_하행_종점역에_새로운_구간을_등록한다() {
        //given
        LineResponse line = 노선_생성(new LineRequest("분당선", "bg-black-600", 1L, 2L, 10));

        //when
        ExtractableResponse<Response> saveResponse = 구간_생성(line.getId(),
                Maps.of("upStationId", 2L,
                        "downStationId", 3L,
                        "distance", 10
                ));
        //then
        assertThat(saveResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        //then
        ExtractableResponse<Response> listResponse = 노선_구간_목록_조회(line.getId());
        List<Long> stationIds = extractStationIds(listResponse.jsonPath().getList("stations.id", String.class));
        assertThat(stationIds).containsAnyOf(3L);
    }

    /**
     * Scenario: 노선의 하행 종점역이 아닌 역에 구간을 등록한다.
     * Given 노선을 생성하고
     * When 노선의 하행 종점역이 아닌 역에 새로운 구간을 등록하면
     * Then 구간을 등록할 수 없고 에러가 발생한다.
     */
    @Test
    void 노선의_하행_종점역이_아닌_역에_구간을_등록한다() {
        //given
        LineResponse line = 노선_생성(new LineRequest("분당선", "bg-black-600", 1L, 2L, 10));

        //when
        ExtractableResponse<Response> saveResponse = 구간_생성(line.getId(),
                Maps.of("upStationId", 1L,
                        "downStationId", 3L,
                        "distance", 10
                ));

        //then
        assertThat(saveResponse.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    /**
     * Scenario: 노선 하행 종점역에 중복된 역이 포함된 구간을 등록한다.
     * Given 노선을 생성하고
     * When 노선의 하행 종점역에 노선에 존재하는 역을 포함한 구간을 등록하면
     * Then 구간을 등록할 수 없고 에러가 발생한다.
     */
    @Test
    void 노선_하행_종점역에_중복된_역이_포함된_구간을_등록한다() {
        //given
        LineResponse line = 노선_생성(new LineRequest("분당선", "bg-black-600", 1L, 2L, 10));

        //when
        ExtractableResponse<Response> saveResponse = 구간_생성(line.getId(),
                Maps.of("upStationId", 2L,
                        "downStationId", 1L,
                        "distance", 10
                ));

        //then
        assertThat(saveResponse.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    /**
     * Scenario: 노선의 하행 종점역 구간을 삭제한다.
     * Given 노선 1개를 생성하고
     * Given 구간 1개를 생성하고
     * When 노선의 하행 종점역 구간을 삭제하면
     * Then 구간이 삭제된다.
     * Then 노선의 구간 목록에서 조회되지 않는다.
     */
    @Test
    void 노선의_하행_종점역_구간을_삭제한다() {
        //given
        LineResponse line = 노선_생성(new LineRequest("분당선", "bg-black-600", 1L, 2L, 10));
        ExtractableResponse<Response> saveResponse = 구간_생성(line.getId(),
                Maps.of("upStationId", 2L,
                        "downStationId", 3L,
                        "distance", 10
                ));

        //when
        ExtractableResponse<Response> deleteResponse = 구간_삭제(line.getId(), 3L);
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        //then
        ExtractableResponse<Response> listResponse = 노선_구간_목록_조회(line.getId());
        List<Long> stationIds = extractStationIds(listResponse.jsonPath().getList("stations.id", String.class));
        assertThat(stationIds).doesNotContain(3L);
    }

    /**
     * Scenario: 노선에 존재하지 않는 구간을 삭제한다.
     * Given 노선 1개를 생성하고
     * Given 구간 1개를 생성하고
     * When 노선에 존재하지 않는 구간을 삭제하면
     * Then 구간 삭제를 실패하고 에러가 발생한다.
     */
    @Test
    void 노선에_존재하지_않는_구간을_삭제한다() {
        //given
        LineResponse line = 노선_생성(new LineRequest("분당선", "bg-black-600", 1L, 2L, 10));
        ExtractableResponse<Response> saveResponse = 구간_생성(line.getId(),
                Maps.of("upStationId", 2L,
                        "downStationId", 3L,
                        "distance", 10
                ));

        //when
        ExtractableResponse<Response> deleteResponse = 구간_삭제(line.getId(), 4L);

        //then
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());

        //then
        ExtractableResponse<Response> listResponse = 노선_구간_목록_조회(line.getId());
        List<Long> stationIds = extractStationIds(listResponse.jsonPath().getList("stations.id", String.class));
        assertThat(stationIds).containsAnyOf(1L, 2L, 3L);
    }

    /**
     * Scenario: 노선의 하행 종점역이 아닌 구간을 삭제한다.
     * Given 노선 1개를 생성하고
     * Given 구간 1개를 생성하고
     * When 노선의 하행 종점역이 아닌 구간을 삭제하면
     * Then 구간 삭제를 실패하고 에러가 발생한다.
     * Then 노선의 구간 목록에서 조회할 수 있다.
     */
    @Test
    void 노선의_하행_종점역이_아닌_구간을_삭제한다() {
        //given
        LineResponse line = 노선_생성(new LineRequest("분당선", "bg-black-600", 1L, 2L, 10));
        ExtractableResponse<Response> saveResponse = 구간_생성(line.getId(),
                Maps.of("upStationId", 2L,
                        "downStationId", 3L,
                        "distance", 10
                ));

        //when
        ExtractableResponse<Response> deleteResponse = 구간_삭제(line.getId(), 2L);

        //then
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());

        //then
        ExtractableResponse<Response> listResponse = 노선_구간_목록_조회(line.getId());
        List<Long> stationIds = extractStationIds(listResponse.jsonPath().getList("stations.id", String.class));
        assertThat(stationIds).containsAnyOf(1L, 2L, 3L);
    }

    /**
     * Scenario: 1개의 구간만 존재하는 노선의 구간을 삭제한다.
     * Given 노선 1개를 생성하고
     * When 노선의 구간을 삭제하면
     * Then 구간 삭제를 실패하고 에러가 발생한다.
     */
    @Test
    void 한개의_구간만_존재하는_노선의_구간을_삭제한다() {
        //given
        LineResponse line = 노선_생성(new LineRequest("분당선", "bg-black-600", 1L, 2L, 10));

        //when
        ExtractableResponse<Response> deleteResponse = 구간_삭제(line.getId(), 2L);

        //then
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());

        ExtractableResponse<Response> listResponse = 노선_구간_목록_조회(line.getId());
        List<Long> stationIds = extractStationIds(listResponse.jsonPath().getList("stations.id", String.class));
        assertThat(stationIds).containsAnyOf(1L, 2L);
    }

    public static LineResponse 노선_생성(LineRequest lineRequest) {
        return LineAcceptanceTest.requestSaveLine(lineRequest)
                .body()
                .as(LineResponse.class);
    }

    public static ExtractableResponse<Response> 구간_생성(Long lineId, Map<String, Object> params) {
        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines/" + lineId + "/sections")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 구간_삭제(Long lineId, Long id) {
        return RestAssured.given().log().all()
                .when().delete("/lines/" + lineId + "/sections?stationId=" + id)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 노선_구간_목록_조회(Long lineId) {
        return RestAssured.given().log().all()
                .when().get("/lines/" + lineId + "/sections")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }
    private List<Long> extractStationIds(List<String> stationIds) {
        System.out.println("stationIds = " + stationIds);
        return String.join("", stationIds)
                .chars()
                .mapToObj(ch -> (char) ch)
                .filter(Character::isDigit)
                .map(String::valueOf)
                .map(Long::valueOf)
                .collect(Collectors.toList());
    }
}
