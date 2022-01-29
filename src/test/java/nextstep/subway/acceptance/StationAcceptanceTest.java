package nextstep.subway.acceptance;

import static nextstep.subway.step.StationStep.역_삭제;
import static nextstep.subway.step.StationStep.역_생성;
import static nextstep.subway.step.StationStep.역_조회;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("지하철역 관리 기능")
class StationAcceptanceTest extends AcceptanceTest {

    /**
     * When 지하철역 생성을 요청 하면 Then 지하철역 생성이 성공한다.
     */
    @DisplayName("지하철역 생성")
    @Test
    void createStation() {
        // given
        // when
        ExtractableResponse<Response> response = 역_생성("강남역");

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
        역_생성("강남역");
        역_생성("역삼역");

        // when
        List<String> stationNames = 역_조회().jsonPath().getList("name");

        // then
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
        ExtractableResponse<Response> response = 역_생성("강남역");

        // when
        ExtractableResponse<Response> deleteResponse = 역_삭제(response.header("Location"));

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
        역_생성("강남역");

        //when
        ExtractableResponse<Response> response = 역_생성("강남역");

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
    }

}
