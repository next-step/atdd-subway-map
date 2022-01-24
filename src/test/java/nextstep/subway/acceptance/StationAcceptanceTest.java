package nextstep.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.utils.CustomRestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@DisplayName("지하철역 관리 기능")
class StationAcceptanceTest extends AcceptanceTest {
    /**
     * When 지하철역 생성을 요청 하면
     * Then 지하철역 생성이 성공한다.
     */
    @DisplayName("지하철역 생성")
    @Test
    void createStation() {
        // given
        ExtractableResponse<Response> response = 지하철역_생성("강남역");

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(response.header("Location")).isNotBlank()
        );
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
        /// given
        String 강남역 = "강남역";
        지하철역_생성("강남역");

        String 역삼역 = "역삼역";
        지하철역_생성(역삼역);

        // when
        ExtractableResponse<Response> response = 지하철역_조회();

        //then
        List<String> stationNames = response.jsonPath().getList("name");

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(stationNames).contains(강남역, 역삼역)
        );
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
        ExtractableResponse<Response> createResponse = 지하철역_생성("강남역");
        String uri = createResponse.header("Location");

        // when
        ExtractableResponse<Response> response = CustomRestAssured.delete(uri);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }


    /**
     * Scenario: 중복이름으로 지하철역 생성
     * Given 지하철역 생성을 요청 하고
     * When 같은 이름으로 지하철역 생성을 요청 하면
     * Then 지하철역 생성이 실패한다.
     */
    @DisplayName("같은 이름의 지하철역은 1개만 생성할 수 있다.")
    @Test
    void createDuplicateStation() {
        // given
        지하철역_생성("강남역");

        // when
        ExtractableResponse<Response> duplicateCreateResponse = 지하철역_생성("강남역");

        // then
        assertThat(duplicateCreateResponse.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    private ExtractableResponse<Response> 지하철역_조회() {
        return CustomRestAssured.get("/stations");
    }

    private ExtractableResponse<Response> 지하철역_생성(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);

        return CustomRestAssured.post("/stations", params);
    }
}
