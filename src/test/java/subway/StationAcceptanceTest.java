package subway;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.StationApiRequest.강남역;
import static subway.StationApiRequest.양재역;
import static subway.StationApiRequest.지하철역_삭제_요청;
import static subway.StationApiRequest.지하철역_생성_요청;
import static subway.StationApiRequest.지하철역_조회_요청;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class StationAcceptanceTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        ExtractableResponse<Response> response = 지하철역_생성_요청(강남역);

        // then
        지하철역_생성됨(response, 강남역);

        // then
        지하철역_조회됨(지하철역_조회_요청(), 강남역);
    }

    private void 지하철역_생성됨(ExtractableResponse<Response> response, String stationName) {
        String name = response.jsonPath().getString("name");

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(name).isEqualTo(stationName);
    }

    private void 지하철역_조회됨(ExtractableResponse<Response> response, String ... stationNames) {
        List<String> names = response.jsonPath().getList("name", String.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(names).containsAnyOf(stationNames);
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    // TODO: 지하철역 목록 조회 인수 테스트 메서드 생성
    @DisplayName("지하철역 목록을 조회한다.")
    @Test
    void showStations() {
        // given
        지하철역_생성_요청(강남역);
        지하철역_생성_요청(양재역);

        // when
        ExtractableResponse<Response> response = 지하철역_조회_요청();

        // then
        지하철역_조회됨(response, 강남역, 양재역);
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    // TODO: 지하철역 삭제 인수 테스트 메서드 생성
    @DisplayName("지하철역을 삭제한다.")
    @Test
    void deleteStation() {
        // given
        long id = 지하철역_생성_요청(강남역).jsonPath().getLong("id");

        // when
        ExtractableResponse<Response> response = 지하철역_삭제_요청(id);

        // then
        지하철역_삭제됨(response, 강남역);
    }

    private void 지하철역_삭제됨(ExtractableResponse<Response> response, String ... stationNames) {
        List<String> names = 지하철역_조회_요청().jsonPath().getList("name", String.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        assertThat(names).doesNotContain(stationNames);
    }
}
