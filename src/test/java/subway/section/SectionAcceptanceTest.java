package subway.section;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.station.StationResponse;
import subway.testsupport.AcceptanceTest;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.line.LineAcceptanceTest.지하철_노선_Id로_조회됨;
import static subway.line.LineAcceptanceTest.지하철_노선_생성됨;
import static subway.station.StationAcceptanceTest.지하철역_등록됨;

@DisplayName("지하철 구간 관련 기능")
public class SectionAcceptanceTest extends AcceptanceTest {

    private Long lineId;
    private Long 강남역_Id;
    private Long 역삼역_Id;
    private Long 선릉역_Id;
    private Long 강남역_역삼역_거리 = 10L;
    private Long 역삼역_선릉역_거리 = 15L;

    @BeforeEach
    void setup() {
        super.setUp();

        /**
         * Given 지하철 노선을 등록하고
         */
        강남역_Id = 지하철역_등록됨("강남역").body().jsonPath().getLong("id");
        역삼역_Id = 지하철역_등록됨("역삼역").body().jsonPath().getLong("id");
        선릉역_Id = 지하철역_등록됨("선릉역").body().jsonPath().getLong("id");
        lineId = 지하철_노선_생성됨("2호선", "green darken-2", 강남역_Id, 역삼역_Id, 강남역_역삼역_거리).jsonPath().getLong("id");
    }

    /**
     * When 지하철 구간을 등록하면
     * Then 지하철 구간 조회 시 생성한 구간을 찾을 수 있다.
     */
    @DisplayName("지하철 구간을 생성한다.")
    @Test
    void createSection() {
        // when
        지하철_구간_생성됨(lineId, new SectionRequest(선릉역_Id, 역삼역_Id, 역삼역_선릉역_거리));

        // then
        var response = 지하철_노선_Id로_조회됨(lineId).jsonPath();
        assertThat(response.getList("stations.id")).contains(역삼역_Id.intValue(), 선릉역_Id.intValue());
    }

    /**
     * When 새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이 아닐때
     * Then 예외가 발생한다.
     */
    @DisplayName("새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이 아닐때 예외 발생")
    @Test
    void createSection_WhenNotLastStopOfDownLine() {
        // when
        ExtractableResponse<Response> response = 지하철_구간_등록_요청(lineId, new SectionRequest(선릉역_Id, 강남역_Id, 역삼역_선릉역_거리));

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY.value());
    }

    /**
     * When 새로운 구간 등록 시 해당 노선에 등록되어 있는 역이면
     * Then 예외가 발생한다.
     */
    @DisplayName("새로운 지하철 구간 등록 시 상행종점역이 기존 구간에 존재하면")
    @Test
    void createSection_WhenAlreadySection() {
        // when
        ExtractableResponse<Response> response = 지하철_구간_등록_요청(lineId, new SectionRequest(선릉역_Id, 강남역_Id, 역삼역_선릉역_거리));

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY.value());
    }

    /**
     * When 지하철 노선에 등록된 구간을 삭제하면
     * Then 지하철 노선 조회시 해당 구간을 조회할 수 없다.
     */
    @DisplayName("지하철 구간을 삭제한다.")
    @Test
    void deleteSection() {
        // when
        Long stationId = 지하철_구간_생성됨(lineId, new SectionRequest(선릉역_Id, 역삼역_Id, 역삼역_선릉역_거리)).jsonPath().getList("stations", StationResponse.class).stream()
                .filter(station -> station.getName().equals("선릉역"))
                .findFirst()
                .map(StationResponse::getId)
                .get();

        지하철_구간_삭제됨(lineId, stationId);

        // then
        ExtractableResponse<Response> response = 지하철_노선_Id로_조회됨(lineId);
        assertThat(response.jsonPath().getList("stations.id")).doesNotContain(stationId);
    }

    /**
     * When 지하철 노선에 등록된 구간을 삭제할 때 마지막 구간이 아니라면
     * Then 예외가 발생한다.
     */
    @DisplayName("지하철 노선에 등록된 구간을 삭제할 때 마지막 구간이 아니라면 예외가 발생한다.")
    @Test
    void deleteSection_WhenNotLastStopOfDownLine() {
        // when
        지하철_구간_생성됨(lineId, new SectionRequest(선릉역_Id, 역삼역_Id, 역삼역_선릉역_거리));
        ExtractableResponse<Response> response = 지하철_구간_삭제_요청(lineId, 역삼역_Id);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY.value());
    }

    /**
     * When 지하철 노선에 등록된 구간이 한개일 경우 삭제 요청을 하면
     * Then 예외가 발생한다.
     */
    @DisplayName("지하철 노선에 등록된 구간을 삭제할 때 구간이 한 개라면 예외가 발생한다.")
    @Test
    void deleteSection_WhenOnlyOneSection() {
        // when
        ExtractableResponse<Response> response = 지하철_구간_삭제_요청(lineId, 강남역_Id);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY.value());
    }

    public ExtractableResponse<Response> 지하철_구간_생성됨(Long lineId, SectionRequest request) {
        var response = 지하철_구간_등록_요청(lineId, request);
        지하철_구간_생성요청_성공(response);
        return response;
    }

    public static ExtractableResponse<Response> 지하철_구간_등록_요청(Long lineId, SectionRequest request) {
        return RestAssured.given().log().all()
                .pathParam("id", lineId)
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines/{id}/sections")
                .then().log().all()
                .extract();
    }

    public static void 지하철_구간_생성요청_성공(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static ExtractableResponse<Response> 지하철_구간_삭제됨(Long lineId, Long stationId) {
        var response = 지하철_구간_삭제_요청(lineId, stationId);
        지하철_구간_삭제요청_성공(response);
        return response;
    }

    public static ExtractableResponse<Response> 지하철_구간_삭제_요청(Long lineId, Long stationId) {
        return RestAssured.given().log().all()
                .pathParam("id", lineId)
                .queryParam("stationId", stationId)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/lines/{id}/sections")
                .then().log().all()
                .extract();
    }

    public static void 지하철_구간_삭제요청_성공(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
