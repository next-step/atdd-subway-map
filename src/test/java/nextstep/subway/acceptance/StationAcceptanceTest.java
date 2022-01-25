package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관리 기능")
class StationAcceptanceTest extends AcceptanceTest {

    private static final String STATION_NAME_01 = "강남역";
    private static final String STATION_NAME_02 = "역삼역";

    /**
     * When 지하철역 생성을 요청 하면
     * Then 지하철역 생성이 성공한다.
     */
    @DisplayName("지하철역 생성")
    @Test
    void createStation() {
        // when
        ExtractableResponse<Response> createResponse =
                StationSteps.지하철역_생성_요청(STATION_NAME_01);

        // then
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(createResponse.header("Location")).isNotBlank();
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
        StationSteps.지하철역_생성_요청(STATION_NAME_01);
        StationSteps.지하철역_생성_요청(STATION_NAME_02);

        // when
        ExtractableResponse<Response> getResponse = StationSteps.지하철역_조회_요청();

        // then
        assertThat(getResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<String> stationNames = getResponse.jsonPath().getList("name");
        assertThat(stationNames).contains(STATION_NAME_01, STATION_NAME_02);
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
        ExtractableResponse<Response> createResponse =
                StationSteps.지하철역_생성_요청(STATION_NAME_01);

        // when
        ExtractableResponse<Response> deleteResponse =
                StationSteps.지하철역_삭제_요청(createResponse.header("Location"));

        // then
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    /**
     * Given 지하철역 생성을 요청 하고
     * When 같은 이름으로 지하철역 생성을 요청 하면
     * Then 지하철역 생성이 실패한다.
     */
    @DisplayName("중복이름으로 지하철역 생성")
    @Test
    void duplicateStation() {
        // given
        StationSteps.지하철역_생성_요청(STATION_NAME_01);

        // when
        ExtractableResponse<Response> duplicateResponse =
                StationSteps.지하철역_생성_요청(STATION_NAME_01);

        // then
        assertThat(duplicateResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
