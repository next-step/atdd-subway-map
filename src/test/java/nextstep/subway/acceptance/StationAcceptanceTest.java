package nextstep.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import nextstep.subway.utils.RequestMethod;
import nextstep.subway.utils.RequestParams;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("지하철역 관리 기능")
class StationAcceptanceTest extends AcceptanceTest {

    private static final String DEFAULT_PATH = "/stations";
    private static final String DEFAULT_NAME_KEY = "name";
    private static final String DEFAULT_NAME_VALUE = "강남역";

    /**
     * When 지하철역 생성을 요청 하면 Then 지하철역 생성이 성공한다.
     */
    @DisplayName("지하철역 생성")
    @Test
    void createStation() {
        // given
        RequestParams params = createDefaultParams();

        // when
        ExtractableResponse<Response> response = RequestMethod.post(DEFAULT_PATH, params);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    /**
     * Given 지하철역 생성을 요청 하고
     * Given 새로운 지하철역 생성을 요청 하고
     * When 지하철역 목록 조회를 요청 하면
     * Then 두 지하철역이 포함된 지하철역
     * 목록을 응답받는다
     */
    @DisplayName("지하철역 목록 조회")
    @Test
    void getStations() {
        /// given
        RequestParams params1 = new RequestParams(DEFAULT_NAME_KEY, "강남역");
        RequestMethod.post(DEFAULT_PATH, params1);

        RequestParams params2 = new RequestParams(DEFAULT_NAME_KEY, "역삼역");
        RequestMethod.post(DEFAULT_PATH, params2);

        // when
        ExtractableResponse<Response> response = RequestMethod.get(DEFAULT_PATH);

        List<String> stationNames = response.jsonPath().getList(DEFAULT_NAME_KEY);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(stationNames).containsExactly("강남역", "역삼역");
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
        RequestParams params = createDefaultParams();
        ExtractableResponse<Response> response = RequestMethod.post(DEFAULT_PATH, params);

        // when
        ExtractableResponse<Response> deleteResponse = RequestMethod.delete(response.header("Location"));

        // then
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    /**
     *     Given 지하철역 생성을 요청 하고
     *     When 같은 이름으로 지하철역 생성을 요청 하면
     *     Then 지하철역 생성이 실패한다.
     */
    @Test
    @DisplayName("중복이름으로 지하철역 생성시 실패")
    void duplicationStationNameExceptionTest() {
        //given
        RequestParams params = createDefaultParams();
        RequestMethod.post(DEFAULT_PATH, params);

        //when
        ExtractableResponse<Response> response = RequestMethod.post(DEFAULT_PATH, params);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    private static RequestParams createDefaultParams() {
        return new RequestParams(DEFAULT_NAME_KEY, DEFAULT_NAME_VALUE);
    }
}
