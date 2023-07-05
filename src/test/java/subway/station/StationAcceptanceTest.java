package subway.station;

import common.AcceptanceTest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.station.StationTestFixture.*;

@DisplayName("지하철역 관련 기능")
@AcceptanceTest
public class StationAcceptanceTest {

    private static final String 강남역 = "강남역";
    private static final String 신논현역 = "신논현역";

    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        int statusCode = 지하철_역_생성_요청_상태_코드_반환(강남역);

        // then
        assertThat(statusCode).isEqualTo(HttpStatus.CREATED.value());

        // then
        List<String> stationNames = 지하철_역_목록_조회_요청_역_이름_목록_반환();

        assertThat(stationNames).containsAnyOf(강남역);
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역 목록을 조회한다")
    @Test
    void getStationList() {
        // when
        지하철_역_생성_요청(강남역);
        지하철_역_생성_요청(신논현역);

        // given
        List<String> stationNames = 지하철_역_목록_조회_요청_역_이름_목록_반환();

        // then
        assertThat(stationNames).containsExactly(강남역, 신논현역);
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역을 삭제 후 확인한다")
    @Test
    void deleteStation() {
        // when
        ExtractableResponse<Response> stationCreateResponse = 지하철_역_생성_요청(강남역);

        // given
        지하철_역_삭제_요청(stationCreateResponse.jsonPath().getInt("id"));

        // then
        List<String> stationNames = 지하철_역_목록_조회_요청_역_이름_목록_반환();

        assertThat(stationNames).isEmpty();
    }
}