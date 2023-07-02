package subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.AcceptanceTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static subway.station.StationAcceptanceTest.*;

@DisplayName("지하철 노선 관리 기능")
public class LineAcceptanceTest extends AcceptanceTest {

    private static final String SINBUNDANG_LINE_NAME = "신분당선";
    private static final String BUNDANG_LINE_NAME = "신분당선";

    @Test
    void 지하철_노선_생성() {
        // given
        LineRequest request = 지하철_노선_요청_객체_생성(SINBUNDANG_LINE_NAME, "bg-red-600", GANGNAM_STATION_NAME, SEOCHO_STATION_NAME, 10L);

        // when
        ExtractableResponse<Response> createResponse = 지하철_노선_생성_요쳥(request);

        // then
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        ExtractableResponse<Response> showResponse = 지하철_노선_목록_조회_요청();
        List<String> lineNames = showResponse.jsonPath().getList("name", String.class);

        assertThat(lineNames.size()).isEqualTo(1);
        assertThat(lineNames.get(0)).isEqualTo(SINBUNDANG_LINE_NAME);
    }

    @Test
    void 지하철_노선_목록_조회() {
        // given
        LineRequest request1 = 지하철_노선_요청_객체_생성(SINBUNDANG_LINE_NAME, "bg-red-600", GANGNAM_STATION_NAME, SEOCHO_STATION_NAME, 10L);
        LineRequest request2 = 지하철_노선_요청_객체_생성(BUNDANG_LINE_NAME, "bg-green-600", GANGNAM_STATION_NAME, SEOCHO_STATION_NAME, 10L);

        ExtractableResponse<Response> createResponse1 = 지하철_노선_생성_요쳥(request1);
        assertThat(createResponse1.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        ExtractableResponse<Response> createResponse2 = 지하철_노선_생성_요쳥(request2);
        assertThat(createResponse2.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // when
        ExtractableResponse<Response> showResponse = 지하철_노선_목록_조회_요청();
        List<String> lineNames = showResponse.jsonPath().getList("name", String.class);

        // then
        assertThat(lineNames.size()).isEqualTo(2);
        assertThat(lineNames.get(0)).isEqualTo(SINBUNDANG_LINE_NAME);
        assertThat(lineNames.get(1)).isEqualTo(BUNDANG_LINE_NAME);
    }

    private LineRequest 지하철_노선_요청_객체_생성(String lineName, String color, String upStationName, String downStationName, Long distance) {
        ExtractableResponse<Response> upStationCreateResponse = 지하철역_생성_요청(upStationName);
        Assertions.assertThat(upStationCreateResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        String upStationId = upStationCreateResponse.response().getHeaders().get("location").getValue().split("/stations/")[1];

        ExtractableResponse<Response> downStationCreateResponse = 지하철역_생성_요청(downStationName);
        Assertions.assertThat(downStationCreateResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        String downStationId = downStationCreateResponse.response().getHeaders().get("location").getValue().split("/stations/")[1];

        return new LineRequest(lineName, color, Long.valueOf(upStationId), Long.valueOf(downStationId), distance);
    }

    private ExtractableResponse<Response> 지하철_노선_생성_요쳥(LineRequest request) {
        return RestAssured
                .given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 지하철_노선_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .when()
                .get("/lines")
                .then().log().all()
                .extract();
    }
}
