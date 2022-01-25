package nextstep.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.dto.StationRequest;

@DisplayName("지하철역 관리 기능")
class StationAcceptanceTest extends AcceptanceTest {
    private final String 강남역 = "강남역";
    private final String 역삼역 = "역삼역";

    /**
     * When 지하철역 생성을 요청 하면
     * Then 지하철역 생성이 성공한다.
     */
    @DisplayName("지하철역 생성")
    @Test
    void createStation() {
        // when
        ExtractableResponse<Response> response = StationSteps.지하철역_생성_요청(강남역);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    /**
     * Given 지하철역 생성을 요청 하고
     * When 같은 이름으로 지하철역 생성을 요청 하면
     * Then 지하철역 생성이 실패한다.
     */
    @DisplayName("지하철역 이름 중복")
    @Test
    void duplicateStationName() {
        // when
        StationSteps.지하철역_생성_요청(강남역);
        ExtractableResponse<Response> response = StationSteps.지하철역_생성_요청(강남역);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    /**
     * Given 지하철역 생성을 요청 하고
     * Given 새로운 지하철역 생성을 요청 하고
     * When 지하철역 목록 조회를 요청 하면
     * Then 두 지하철역이 포함된 지하철역 목록을 응답받는다
     */
    @DisplayName("지하철역 목록 조회")
    @Test
    void getStations() {
        // given
        StationSteps.지하철역_생성_요청(강남역);
        StationSteps.지하철역_생성_요청(역삼역);

        // when
        ExtractableResponse<Response> response = StationSteps.지하철역_목록_조회();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<String> stationNames = response.jsonPath().getList("name");
        assertThat(stationNames).contains(강남역, 역삼역);
    }

    /**
     * Given 지하철역 생성을 요청 하고
     * When 생성한 지하철역 조회를 요청 하면
     * Then 생성한 지하철역을 응답받는다
     */
    @DisplayName("지하철역 조회")
    @Test
    void getStation() {
        // given
        ExtractableResponse<Response> createResponse = StationSteps.지하철역_생성_요청(강남역);
        String uri = createResponse.header("Location");

        // when
        ExtractableResponse<Response> response = StationSteps.지하철역_조회(uri);

        // then
        JsonPath createResponseJson = createResponse.jsonPath();
        JsonPath responseJson = response.jsonPath();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(responseJson.getString("name")).isEqualTo(createResponseJson.getString("name"));
        assertThat(responseJson.getString("color")).isEqualTo(createResponseJson.getString("color"));
        assertThat(responseJson.getString("createdDate")).isEqualTo(createResponseJson.getString("createdDate"));
        assertThat(responseJson.getString("modifiedDate")).isEqualTo(createResponseJson.getString("modifiedDate"));
    }

    /**
     * Given 지하철역 생성을 요청 하고
     * When 지하철역 정보 수정을 요청 하면
     * Then 지하철역 정보 수정은 성공한다.
     */
    @DisplayName("지하철역 수정")
    @Test
    void updateStation() {
        // given
        StationRequest updateRequest = StationSteps.지하철역_데이터(역삼역);
        ExtractableResponse<Response> createResponse = StationSteps.지하철역_생성_요청(강남역);
        String uri = createResponse.header("Location");

        // when
        StationSteps.지하철역_수정(uri, updateRequest);
        ExtractableResponse<Response> response = StationSteps.지하철역_조회(uri);

        // then
        JsonPath createResponseJson = createResponse.jsonPath();
        JsonPath responseJson = response.jsonPath();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(responseJson.getString("name")).isNotEqualTo(createResponseJson.getString("name"));
        assertThat(responseJson.getString("color")).isNotEqualTo(createResponseJson.getString("color"));
        assertThat(responseJson.getString("createdDate")).isEqualTo(createResponseJson.getString("createdDate"));
        assertThat(responseJson.getString("modifiedDate")).isEqualTo(createResponseJson.getString("modifiedDate"));
    }

    /**
     * Given 지하철역 생성을 요청 하고
     * When 생성한 지하철역 삭제를 요청 하면
     * Then 생성한 지하철역 삭제가 성공한다.
     */
    @DisplayName("지하철역 삭제")
    @Test
    void deleteStation() {
        // given
        ExtractableResponse<Response> createResponse = StationSteps.지하철역_생성_요청(강남역);
        String uri = createResponse.header("Location");

        // when
        ExtractableResponse<Response> response = StationSteps.지하철역_삭제(uri);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
