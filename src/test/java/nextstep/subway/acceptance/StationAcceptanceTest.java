package nextstep.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("지하철역 관리 기능")
class StationAcceptanceTest extends AcceptanceTest {

    private static final String PATH_PREFIX = "/stations";

    /** When 지하철역 생성을 요청 하면 Then 지하철역 생성이 성공한다. */
    @DisplayName("지하철역 생성")
    @Test
    void createStation() {
        // given
        Map<String, String> params = new HashMap<>();
        params.put("name", "강남역");

        // when
        ExtractableResponse<Response> response = stationCreateRequest(PATH_PREFIX, params);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    /**
     * Given 지하철역 생성을 요청 하고 Given 새로운 지하철역 생성을 요청 하고 When 지하철역 목록 조회를 요청 하면 Then 두 지하철역이 포함된 지하철역
     * 목록을 응답받는다
     */
    @DisplayName("지하철역 목록 조회")
    @Test
    void getStations() {
        /// given
        String 강남역 = "강남역";
        Map<String, String> params1 = new HashMap<>();
        params1.put("name", 강남역);
        ExtractableResponse<Response> createResponse1 = stationCreateRequest(PATH_PREFIX, params1);

        String 역삼역 = "역삼역";
        Map<String, String> params2 = new HashMap<>();
        params2.put("name", 역삼역);
        ExtractableResponse<Response> createResponse2 = stationCreateRequest(PATH_PREFIX, params2);

        // when
        ExtractableResponse<Response> response =
                RestAssured.given()
                        .log()
                        .all()
                        .when()
                        .get(PATH_PREFIX)
                        .then()
                        .log()
                        .all()
                        .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<String> stationNames = response.jsonPath().getList("name");
        assertThat(stationNames).contains(강남역, 역삼역);
    }

    /** Given 지하철역 생성을 요청 하고 When 생성한 지하철역 삭제를 요청 하면 Then 생성한 지하철역 삭제가 성공한다. */
    @DisplayName("지하철역 삭제")
    @Test
    void deleteStation() {
        // given
        Map<String, String> params = new HashMap<>();
        params.put("name", "강남역");
        ExtractableResponse<Response> createResponse = stationCreateRequest(PATH_PREFIX, params);

        // when
        String uri = createResponse.header("Location");
        ExtractableResponse<Response> response =
                RestAssured.given().log().all().when().delete(uri).then().log().all().extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
    /** Given 지하철역 생성을 요청 하고 When 같은 이름으로 지하철역 생성을 요청 하면 Then 지하철역 생성이 실패한다. */
    @DisplayName("중복된 이름으로 역을 생성할 수 없다.")
    @Test
    void duplicateNameCreationTest() {
        // given
        Map<String, String> params = new HashMap<>();
        params.put("name", "강남역");

        ExtractableResponse<Response> creationResponse = stationCreateRequest(PATH_PREFIX, params);

        // when
        ExtractableResponse<Response> duplicateCreationResponse =
                stationCreateRequest(PATH_PREFIX, params);

        // then
        // TODO question: Bad request vs conflict 어떤 status가 맞을지 애매하네요.
        // 전 일단 bad_request로...
        assertThat(duplicateCreationResponse.statusCode())
                .isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    static ExtractableResponse<Response> stationCreateRequest(
            String path, Map<String, String> params) {
        return RestAssured.given()
                .log()
                .all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post(path)
                .then()
                .log()
                .all()
                .extract();
    }
}
