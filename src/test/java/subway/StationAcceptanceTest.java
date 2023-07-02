package subway;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.SubwayTestFixture.*;

@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class StationAcceptanceTest {
    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        ExtractableResponse<Response> stationCreateResponse = 지하철_역_생성_요청("강남역");

        // then
        assertThat(stationCreateResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        ExtractableResponse<Response> stationListResponse = 지하철_역_목록_조회_요청();
        List<String> stationNames = stationListResponse
                .jsonPath().getList("name", String.class);

        assertThat(stationNames).containsAnyOf("강남역");
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    // TODO: 지하철역 목록 조회 인수 테스트 메서드 생성
    @DisplayName("지하철역 목록을 조회한다")
    @Test
    void getStationList() {
        // when
        지하철_역_생성_요청("강남역");
        지하철_역_생성_요청("신논현역");

        // given
        ExtractableResponse<Response> response = 지하철_역_목록_조회_요청();

        // then
        List<String> stationNames = response
                .jsonPath().getList("name", String.class);

        assertThat(stationNames).containsExactly("강남역", "신논현역");
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    // TODO: 지하철역 제거 인수 테스트 메서드 생성
    @DisplayName("지하철역을 삭제 후 확인한다")
    @Test
    void deleteStation() {
        // when
        ExtractableResponse<Response> stationCreateResponse = 지하철_역_생성_요청("강남역");

        // given
        지하철_역_삭제_요청(stationCreateResponse.header("location"));

        // then
        ExtractableResponse<Response> response = 지하철_역_목록_조회_요청();

        List<String> stationNames = response
                .jsonPath().getList("name", String.class);

        assertThat(stationNames).isEmpty();
    }
}