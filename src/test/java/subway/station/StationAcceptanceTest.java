package subway.station;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.AcceptanceTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관련 기능")
public class StationAcceptanceTest extends AcceptanceTest {

    public static final String GANGNAM_STATION_NAME = "강남역";
    public static final String SEOCHO_STATION_NAME = "서초역";

    @Test
    void 지하철역_생성() {
        // when
        ExtractableResponse<Response> createResponse = 지하철역_생성_요청(GANGNAM_STATION_NAME);

        // then
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        ExtractableResponse<Response> showResponse = 지하철역_목록_조회_요청();
        List<String> stationNames = showResponse.jsonPath().getList("name", String.class);

        assertThat(stationNames.size()).isEqualTo(1);
        assertThat(stationNames.get(0)).isEqualTo(GANGNAM_STATION_NAME);
    }

    @Test
    void 지하철역_목록_조회() {
        // given
        ExtractableResponse<Response> createResponse1 = 지하철역_생성_요청(GANGNAM_STATION_NAME);
        assertThat(createResponse1.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        ExtractableResponse<Response> createResponse2 = 지하철역_생성_요청(SEOCHO_STATION_NAME);
        assertThat(createResponse2.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // when
        ExtractableResponse<Response> showResponse = 지하철역_목록_조회_요청();
        List<String> stationNames = showResponse.jsonPath().getList("name", String.class);

        // then
        assertThat(stationNames.size()).isEqualTo(2);
        assertThat(stationNames.get(0)).isEqualTo(GANGNAM_STATION_NAME);
        assertThat(stationNames.get(1)).isEqualTo(SEOCHO_STATION_NAME);
    }

    @Test
    void 지하철역_제거() {
        // given
        ExtractableResponse<Response> createResponse = 지하철역_생성_요청(GANGNAM_STATION_NAME);
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // when
        String location = createResponse.response().getHeaders().get("location").getValue();
        ExtractableResponse<Response> deleteResponse = RestAssured
                .when()
                .delete(location)
                .then().log().all()
                .extract();

        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        // then
        ExtractableResponse<Response> showResponse = 지하철역_목록_조회_요청();
        List<StationResponse> stationResponse = showResponse.jsonPath().getList(".", StationResponse.class);

        assertThat(stationResponse.size()).isEqualTo(0);
    }

    public static ExtractableResponse<Response> 지하철역_생성_요청(String stationName) {
        Map<String, String> params = new HashMap<>();
        params.put("name", stationName);

        return RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철역_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .when()
                .get("/stations")
                .then().log().all()
                .extract();
    }
}
