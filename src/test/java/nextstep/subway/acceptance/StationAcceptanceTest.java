package nextstep.subway.acceptance;

import static nextstep.subway.acceptance.StationStep.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.utils.AcceptanceTestUtils;

@DisplayName("지하철역 관리 기능")
class StationAcceptanceTest extends AcceptanceTest {
    /**
     * When 지하철역 생성을 요청 하면
     * Then 지하철역 생성이 성공한다.
     */
    @DisplayName("지하철역 생성")
    @Test
    void createStation() {
        // given, when
        ExtractableResponse<Response> response = 지하철역_생성_요청(지하철역_생성_수정_Params.강남역.getName());

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(AcceptanceTestUtils.getLocation(response)).isNotBlank();
    }

    /**
     * Given 지하철역 생성을 요청 하고
     * When 같은 이름으로 지하철역 생성을 요청 하면
     * Then 지하철역 생성이 실패한다.
     */
    @DisplayName("지하철역 생성 실패 - 중복 이름")
    @Test
    void createStationThatFailing() {
        // given
        지하철역_생성_요청(지하철역_생성_수정_Params.강남역.getName());

        // when
        ExtractableResponse<Response> response = 지하철역_생성_요청(지하철역_생성_수정_Params.강남역.getName());

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
        assertThat(AcceptanceTestUtils.getLocation(response)).isNull();
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
        지하철역_생성_요청(지하철역_생성_수정_Params.강남역.getName());
        지하철역_생성_요청(지하철역_생성_수정_Params.역삼역.getName());

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                                                            .when()
                                                            .get("/stations")
                                                            .then().log().all()
                                                            .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<String> stationNames = response.jsonPath().getList("name");
        assertThat(stationNames).contains(지하철역_생성_수정_Params.강남역.getName(), 지하철역_생성_수정_Params.역삼역.getName());
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
        ExtractableResponse<Response> createResponse = 지하철역_생성_요청(지하철역_생성_수정_Params.강남역.getName());

        // when
        ExtractableResponse<Response> response = AcceptanceTestUtils.requestLocation(createResponse, Method.DELETE);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
