package subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static subway.common.fixture.FieldFixture.역_이름;
import static subway.common.fixture.StationFixture.강남역;
import static subway.common.fixture.StationFixture.서울대입구역;

@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class StationAcceptanceTest {

    public static final String REQUEST_STATION_URL = "/stations";

    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        Map<String, String> 강남역_데이터 = new HashMap<>();
        강남역_데이터.put(역_이름.필드명(), 강남역.역_이름());

        ExtractableResponse<Response> response = 지하철역_생성_요청(강남역_데이터);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        List<String> 등록된_지하철역_이름_목록 = 지하철역_목록_조회_요청()
                .jsonPath().getList(역_이름.필드명(), String.class);

        assertThat(등록된_지하철역_이름_목록).containsAnyOf(강남역.역_이름());
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역 목록을 조회한다.")
    @Test
    void selectStations() {
        // given
        Map<String, String> 강남역_데이터 = new HashMap<>();
        강남역_데이터.put(역_이름.필드명(), 강남역.역_이름());
        지하철역_생성_요청(강남역_데이터);

        Map<String, String> 서울대입구역_데이터 = new HashMap<>();
        서울대입구역_데이터.put(역_이름.필드명(), 서울대입구역.역_이름());
        지하철역_생성_요청(서울대입구역_데이터);

        // when
        List<String> 등록된_지하철역_이름_목록 = 지하철역_목록_조회_요청()
                .jsonPath().getList(역_이름.필드명(), String.class);

        // then
        assertThat(등록된_지하철역_이름_목록).contains(강남역.역_이름(), 서울대입구역.역_이름());
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역을 삭제한다.")
    @Test
    void deleteStation() {
        // given
        Map<String, String> 강남역_데이터 = new HashMap<>();
        강남역_데이터.put(역_이름.필드명(), 강남역.역_이름());
        지하철역_생성_요청(강남역_데이터);

        // when
        지하철역_삭제_요청(1L);

        // then
        List<String> 등록된_지하철역_이름_목록 = 지하철역_목록_조회_요청()
                .jsonPath().getList(역_이름.필드명(), String.class);

        assertThat(등록된_지하철역_이름_목록).doesNotContain(강남역.역_이름());
    }


    private static ExtractableResponse<Response> 지하철역_생성_요청(Map<String, String> requestBody) {
        return given().log().all()
                    .body(requestBody)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(REQUEST_STATION_URL)
                .then().log().all()
                    .extract();
    }

    private static void 지하철역_삭제_요청(Long stationId) {
        given().log().all()
        .when().delete(REQUEST_STATION_URL + "/" + stationId)
        .then().log().all()
            .extract();
    }

    private static ExtractableResponse<Response> 지하철역_목록_조회_요청() {
        return given().log().all()
                .when().get(REQUEST_STATION_URL)
                .then().log().all()
                    .extract();
    }
}
