package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.utils.AssuredRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static nextstep.subway.common.ErrorMessages.DUPLICATE_STATION_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("지하철역 관리 기능")
class StationAcceptanceTest extends AcceptanceTest {

    private static final String END_POINT = "/stations";

    /**
     * When 지하철역 생성을 요청 하면
     * Then 지하철역 생성이 성공한다.
     */
    @DisplayName("지하철역 생성")
    @Test
    void createStation() {
        // given
        Map<String, String> params = new HashMap<>();
        params.put("name", "강남역");

        // when
        ExtractableResponse<Response> response = 지하철역_Create(params);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
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
        Map<String, String> params1 = new HashMap<>();
        params1.put("name", 강남역);
        지하철역_Create(params1);

        String 역삼역 = "역삼역";
        Map<String, String> params2 = new HashMap<>();
        params2.put("name", 역삼역);
        지하철역_Create(params2);

        // when
        ExtractableResponse<Response> response = 지하철역_목록_FindAll();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<String> stationNames = response.jsonPath().getList("name");
        assertThat(stationNames).contains(강남역, 역삼역);
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
        Map<String, String> params = new HashMap<>();
        params.put("name", "강남역");
        ExtractableResponse<Response> createResponse = 지하철역_Create(params);

        // when
        String uri = createResponse.header("Location");
        ExtractableResponse<Response> response = 지하철역_Delete(uri);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    /**
     * Given 지하철역 생성을 요청 하고
     * When 같은 이름으로 지하철역 생성을 요청 하면
     * Then 지하철역 생성이 실패한다.
     */
    @DisplayName("중복이름으로 지하철역 생성")
    @Test
    void duplicatStation() {
        // given
        String 강남역 = "강남역";
        Map<String, String> params1 = new HashMap<>();
        params1.put("name", 강남역);
        지하철역_Create(params1);

        Map<String, String> params2 = new HashMap<>();
        params2.put("name", 강남역);

        // when
        ExtractableResponse<Response> response = 지하철역_Create(params2);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(response.jsonPath().getString("errorMessage")).isEqualTo(DUPLICATE_STATION_NAME.getMessage())
        );
    }

    private ExtractableResponse<Response> 지하철역_Create(Map<String, String> map) {
        return AssuredRequest.doCreate(END_POINT, map);
    }

    private ExtractableResponse<Response> 지하철역_목록_FindAll() {
        return AssuredRequest.doFind(END_POINT);
    }

    private ExtractableResponse<Response> 지하철역_Delete(String uri) {
        return AssuredRequest.doDelete(uri);
    }
}
