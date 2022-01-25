package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;

import static nextstep.subway.acceptance.StationFixture.*;
import static nextstep.subway.acceptance.StationSteps.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

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
        // when
        ExtractableResponse<Response> response = 지하철역_생성_요청(FIXTURE_강남역);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    /**
     *  Given 지하철역 생성을 요청하고
     *  When 같은 이름으로 지하철역 생성을 요청 하면
     *  Then 지하철역 생성이 실패한다.
     */
    @DisplayName("지하철역 중복 이름 생성 예외")
    @Test
    void createStationDuplicationNameException() {
        //given
        지하철역_생성_요청(FIXTURE_강남역);

        //when
        ExtractableResponse<Response> response = 지하철역_생성_요청(FIXTURE_강남역);
        int statusCode = response.statusCode();
        String errorMessage = response.body().asString();

        //then
        assertAll(
            () -> assertThat(statusCode).isEqualTo(HttpStatus.CONFLICT.value()),
            () -> assertThat(errorMessage).isEqualTo("같은 이름으로 등록된 데이터가 존재합니다.")
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
        지하철역_생성_요청(FIXTURE_강남역);
        지하철역_생성_요청(FIXTURE_역삼역);

        // when
        ExtractableResponse<Response> response = 지하철역_목록_조회();

        // then
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
        ExtractableResponse<Response> createResponse = 지하철역_생성_요청(FIXTURE_강남역);

        // when
        String uri = createResponse.header("Location");
        ExtractableResponse<Response> response = 지하철역_삭제(uri);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }


}
