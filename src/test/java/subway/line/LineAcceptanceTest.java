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

    @Test
    void 지하철_노선_생성() {
        // given
        ExtractableResponse<Response> upStationCreateResponse = 지하철역_생성_요청(GANGNAM_STATION_NAME);
        Assertions.assertThat(upStationCreateResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        String upStationId = upStationCreateResponse.response().getHeaders().get("location").getValue().split("/stations/")[1];

        ExtractableResponse<Response> downStationCreateResponse = 지하철역_생성_요청(SEOCHO_STATION_NAME);
        Assertions.assertThat(downStationCreateResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        String downStationId = downStationCreateResponse.response().getHeaders().get("location").getValue().split("/stations/")[1];

        LineRequest request = new LineRequest(SINBUNDANG_LINE_NAME, "bg-red-600", Long.valueOf(upStationId), Long.valueOf(downStationId), 10L);

        // when
        ExtractableResponse<Response> createResponse = RestAssured
                .given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().log().all()
                .extract();

        // then
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        ExtractableResponse<Response> showResponse = RestAssured
                .given().log().all()
                .when()
                .get("/lines")
                .then().log().all()
                .extract();
        List<String> lineNames = showResponse.jsonPath().getList("name", String.class);

        assertThat(lineNames.size()).isEqualTo(1);
        assertThat(lineNames.get(0)).isEqualTo(SINBUNDANG_LINE_NAME);
    }
}
