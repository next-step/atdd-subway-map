package subway;

import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;

@DisplayName("지하철역 관련 기능")
@AcceptanceTest
class StationAcceptanceTest {

    private static final String RESOURCE_URL = "/stations";


    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // given
        String stationName = "강남역";

        // when
        AcceptanceTestBuilder 지하철역_생성 = 지하철역_생성(stationName);
        ExtractableResponse<Response> 지하철역_생성_됨 = 지하철역_생성_됨(지하철역_생성);

        // then
        AcceptanceTestBuilder 지하철역_단건_조회 = 지하철역_단건_조회(지하철역_생성_됨.header(HttpHeaders.LOCATION));
        ValidatableResponse 지하철역_조회_됨 = 지하철역_조회_됨(지하철역_단건_조회);

        지하철역_조회_됨
                .body("name", equalTo(stationName));
    }


    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    // TODO: 지하철역 목록 조회 인수 테스트 메서드 생성

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    // TODO: 지하철역 제거 인수 테스트 메서드 생성
    private AcceptanceTestBuilder 지하철역_생성(String stationName) {
        Map<String, String> params = new HashMap<>();
        params.put("name", stationName);

        return AcceptanceTestBuilder
                .given().body(params).contentType(ContentType.JSON)
                .when(HttpMethod.POST, RESOURCE_URL);
    }

    private ExtractableResponse<Response> 지하철역_생성_됨(AcceptanceTestBuilder acceptanceTestBuilder) {
        return acceptanceTestBuilder.then(HttpStatus.CREATED).extract();
    }

    private AcceptanceTestBuilder 지하철역_단건_조회(String url) {
        return AcceptanceTestBuilder
                .given()
                .when(HttpMethod.GET, url);
    }

    private ValidatableResponse 지하철역_조회_됨(AcceptanceTestBuilder acceptanceTestBuilder) {
        return acceptanceTestBuilder.then(HttpStatus.OK);
    }
}