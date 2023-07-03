package subway.util;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;
import subway.dto.LineCreateRequest;

public class LineApiRequest {

    public static final String 신분당선_이름 = "신분당선";
    public static final String 신분당선_색상 = "red";
    public static final String 경강선_이름 = "경강선";
    public static final String 경강선_색상 = "blue";

    public static ExtractableResponse<Response> 지하철_노선_생성_요청(String name, String color,
        Long upStationId, Long downStationId, Integer distance) {
        return RestAssured.given().log().all()
            .body(new LineCreateRequest(name, color, upStationId, downStationId, distance))
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/lines")
            .then().log().all()
            .extract();
    }

}
