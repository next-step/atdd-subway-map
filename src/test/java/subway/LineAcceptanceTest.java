package subway;

import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.line.dto.LineRequest;
import subway.line.dto.LineUpdateRequest;
import subway.station.entity.Station;
import subway.station.repository.StationRepository;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends BaseTest {

    @Autowired
    private StationRepository stationRepository;

    private static final String API_PATH = "/lines";

    private Long 강남역_ID;
    private Long 역삼역_ID;
    private Long 지하철역_ID;

    @Override
    @BeforeEach
    void setUp() {
        super.setUp();

        final Station 강남역 = stationRepository.save(new Station("강남역"));
        강남역_ID = 강남역.getId();

        final Station 역삼역 = stationRepository.save(new Station("역삼역"));
        역삼역_ID = 역삼역.getId();

        final Station 지하철역 = stationRepository.save(new Station("지하철역"));
        지하철역_ID = 지하철역.getId();
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void 지하철_노선_생성() {
        // when
        final LineRequest request = new LineRequest("신분당선", "bg-red-600", 강남역_ID, 역삼역_ID, 10);
        callCreateApi(request, API_PATH);

        // then
        final JsonPath jsonPath = this.getSubwayLineList();

        final List<String> lineNames = jsonPath.getList("name", String.class);
        assertThat(lineNames).containsAnyOf("신분당선");
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void 지하철_노선_목록_조회() {
        // given
        final LineRequest 강남역_생성_요청 = new LineRequest("신분당선", "bg-red-600", 강남역_ID, 역삼역_ID, 10);
        callCreateApi(강남역_생성_요청, API_PATH);

        final LineRequest 지하철노선_생성_요청 = new LineRequest("지하철노선", "bg-green-600", 강남역_ID, 지하철역_ID, 15);
        callCreateApi(강남역_생성_요청, API_PATH);

        // when
        final JsonPath jsonPath = this.getSubwayLineList();

        // then
        final List<String> lineNames = jsonPath.getList("name", String.class);
        assertThat(lineNames).containsExactly("신분당선", "지하철노선");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void 지하철_노선_조회() {
        // given
        final LineRequest request = new LineRequest("신분당선", "bg-red-600", 강남역_ID, 역삼역_ID, 10);
        final ExtractableResponse<Response> createSubwayLineResponse = callCreateApi(request, API_PATH);
        final String subwayLineId = this.getCreatedSubwayLineId(createSubwayLineResponse);

        // when
        final JsonPath jsonPath = this.getSubwayLine(subwayLineId);

        // then
        final String lineName = jsonPath.get("name");
        assertThat(lineName).isEqualTo("신분당선");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void 지하철_노선_수정() {
        // given
        final LineRequest createRequest = new LineRequest("신분당선", "bg-red-600", 강남역_ID, 역삼역_ID, 10);
        final ExtractableResponse<Response> createSubwayLineResponse = callCreateApi(createRequest, API_PATH);
        final String subwayLineId = getCreatedSubwayLineId(createSubwayLineResponse);

        final LineUpdateRequest updateRequest = new LineUpdateRequest("2호선", "bg-yellow-600");

        // when
        given()
            .log().all()
            .body(updateRequest)
            .contentType(ContentType.JSON)
        .when()
            .put("lines/{id}", subwayLineId)
        .then()
            .statusCode(HttpStatus.OK.value())
            .log().all();

        // then
        final JsonPath afterUpdatedSubwayLine = this.getSubwayLine(subwayLineId);

        final String updatedName = afterUpdatedSubwayLine.get("name");
        assertThat(updatedName).isEqualTo("2호선");

        final String updatedColor = afterUpdatedSubwayLine.get("color");
        assertThat(updatedColor).isEqualTo("bg-yellow-600");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("지하철 노선을 삭제한다.")
    @Test
    void 지하철_노선_삭제() {
        // given
        final LineRequest createRequest = new LineRequest("신분당선", "bg-red-600", 강남역_ID, 역삼역_ID, 10);
        final ExtractableResponse<Response> createSubwayLineResponse = callCreateApi(createRequest, API_PATH);
        final String subwayLineId = this.getCreatedSubwayLineId(createSubwayLineResponse);

        // when
        given()
        .when()
            .delete("/lines/{id}", subwayLineId)
        .then()
            .statusCode(HttpStatus.NO_CONTENT.value())
            .log().all();

        // then
        final JsonPath jsonPath = this.getSubwayLineList();
        final List<String> lineNames = jsonPath.getList("name", String.class);

        assertThat(lineNames).doesNotContain("신분당선");
    }

    private String getCreatedSubwayLineId(final ExtractableResponse<Response> createSubwayLineResponse) {
        final String location = createSubwayLineResponse.header("Location");
        final String subwayLineId = location.replaceAll(".*/(\\d+)$", "$1");

        return subwayLineId;
    }

    private JsonPath getSubwayLineList() {
        final JsonPath response =
                given()
                    .log().all()
                .when()
                    .get("/lines")
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .log().all()
                .extract()
                    .jsonPath();

        return response;
    }

    private JsonPath getSubwayLine(final String subwayLineId) {
        final JsonPath response = given()
                .when()
                .get("/lines/{id}", subwayLineId)
                .then()
                .statusCode(HttpStatus.OK.value())
                .log().all()
                .extract()
                .jsonPath();

        return response;
    }

}
