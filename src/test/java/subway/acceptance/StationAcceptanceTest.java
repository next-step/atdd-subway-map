package subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.assertj.core.api.AbstractIntegerAssert;
import org.assertj.core.api.AbstractStringAssert;
import org.assertj.core.api.ListAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

@Sql("/cleanup.sql")
@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class StationAcceptanceTest {

    private static final String STATION_NAME_KEY = "name";
    private static final String STATION_ID_KEY = "id";
    private static final String STATION_BASE_URL = "/stations";

    @LocalServerPort
    int port;

    @Autowired
    RestAssuredUtil restAssuredUtil;

    @DisplayName("RestAssured 요청 포트 번호를 설정합니다.")
    @BeforeEach
    void setup() {
        restAssuredUtil.initializePort(port);
        restAssuredUtil.cleanup();
    }

    public static ExtractableResponse<Response> 지하철_역_생성(String name) {
        return RestAssuredUtil.createWithCreated(STATION_BASE_URL, Map.of(STATION_NAME_KEY, name));
    }

    public static ExtractableResponse<Response> 지하철_역_목록을_조회합니다() {
        return RestAssuredUtil.findAllWithOk(STATION_BASE_URL);
    }

    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        String name = "강남역";
        ExtractableResponse<Response> response = 지하철_역_생성(name);

        // then
        지하철_역_정상_생성되었습니다(response);

        // then
        List<String> stationNames = 지하철_역_목록_조회();
        지하철_역_목록에_생성한_역이_존재합니다(stationNames, name);
    }

    private ListAssert<String> 지하철_역_목록에_생성한_역이_존재합니다(List<String> stationNames, String name) {
        return assertThat(stationNames).containsAnyOf(name);
    }

    private List<String> 지하철_역_목록_조회() {
        return 지하철_역_목록을_조회합니다().jsonPath().getList(STATION_NAME_KEY, String.class);
    }

    private void 지하철_역_정상_생성되었습니다(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    // TODO: 지하철역 목록 조회 인수 테스트 메서드 생성
    @DisplayName("지하철역 목록을 조회한다.")
    @Test
    void readStationList() {
        // given
        String station1 = "강남역";
        String station2 = "양재역";
        지하철_역_목록을_생성합니다(station1, station2);

        // when
        ExtractableResponse<Response> response = 지하철_역_목록을_조회합니다();

        // then
        응답_지하철_역_목록에_이름들이_포함됩니다(response, station1, station2);
    }

    private void 응답_지하철_역_목록에_이름들이_포함됩니다(
        ExtractableResponse<Response> response, String... name) {
        assertAll(
            () -> 지하철_역_목록이_정상조회되었습니다(response),
            () -> 지하철_역_목록에_이름들이_포함됩니다(response, name),
            () -> 응답결과_타입이_JSON_입니다(response)
        );
    }

    private AbstractStringAssert<?> 응답결과_타입이_JSON_입니다(ExtractableResponse<Response> response) {
        return assertThat(response.contentType()).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
    }

    private ListAssert<String> 지하철_역_목록에_이름들이_포함됩니다(
        ExtractableResponse<Response> response, String... names) {
        return assertThat(
            response.jsonPath().getList(STATION_NAME_KEY, String.class)).containsExactly(names);
    }

    private AbstractIntegerAssert<?> 지하철_역_목록이_정상조회되었습니다(ExtractableResponse<Response> response) {
        return assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 지하철_역_목록을_생성합니다(String station1, String station2) {
        Stream.of(station1, station2).forEach(StationAcceptanceTest::지하철_역_생성);
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    // TODO: 지하철역 제거 인수 테스트 메서드 생성
    @DisplayName("지하철역을 제거한다.")
    @Test
    void deleteStation() {
        // given
        Long savedStationId = 생성한_지하철_역_ID();

        // when
        ExtractableResponse<Response> response = ID에_해당하는_지하철_역을_삭제합니다(savedStationId);

        // then
        지하철_역_목록에서_생성한_역을_찾을수_없습니다(savedStationId, response);
    }

    private void 지하철_역_목록에서_생성한_역을_찾을수_없습니다(Long savedStationId, ExtractableResponse<Response> response) {
        assertAll(
            () -> 응답값의_BODY_빈_문자열입니다(response),
            () -> 응답값의_상태코드는_NO_CONTENT_입니다(response),
            () -> 응답_지하철_역_목록에서_생성한_역을_찾을수_없습니다(savedStationId)
        );
    }

    private ListAssert<Long> 응답_지하철_역_목록에서_생성한_역을_찾을수_없습니다(Long savedStationId) {
        return assertThat(
            지하철_역_목록을_조회합니다().jsonPath().getList(STATION_ID_KEY, Long.class)).doesNotContain(
            savedStationId);
    }

    private AbstractIntegerAssert<?> 응답값의_상태코드는_NO_CONTENT_입니다(ExtractableResponse<Response> response) {
        return assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private AbstractStringAssert<?> 응답값의_BODY_빈_문자열입니다(ExtractableResponse<Response> response) {
        return assertThat(response.body().asString()).isBlank();
    }

    private ExtractableResponse<Response> ID에_해당하는_지하철_역을_삭제합니다(Long savedStationId) {
        return deleteStation(savedStationId);
    }

    private long 생성한_지하철_역_ID() {
        return 지하철_역_생성("강남역").jsonPath().getLong(STATION_ID_KEY);
    }

    private ExtractableResponse<Response> deleteStation(Long id) {
        return RestAssuredUtil.deleteWithNoContent("/stations/{id}", id);
    }

}
