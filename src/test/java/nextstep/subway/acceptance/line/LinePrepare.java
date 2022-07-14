package nextstep.subway.acceptance.line;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.UpdateLineRequest;
import nextstep.subway.domain.Line;
import org.springframework.http.MediaType;

import java.util.List;

public class LinePrepare {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static String params;

    public static ExtractableResponse<Response> 지하철노선_생성_요청(String name, String color, Long upStationId, Long downStationId) {
        try {
            params = objectMapper.writeValueAsString(new LineRequest(name, color, upStationId, downStationId));
        } catch (Exception e) {
            e.getStackTrace();
        }

        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철노선_수정_요청(String path, String name, String color) {
        try {
            params = objectMapper.writeValueAsString(new UpdateLineRequest(name, color));
        } catch (Exception e) {
            e.getStackTrace();
        }

        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put(path)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철노선_삭제_요청(String path) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete(path)
                .then().log().all()
                .extract();
    }

    public static Line 지하철노선_조회_요청(String path) {
        return RestAssured.given().log().all()
                .when().get(path)
                .then().log().all()
                .extract()
                .as(Line.class);
    }

    public static List<Line> 지하철노선목록_조회_요청() {
        return RestAssured.given().log().all()
                .when().get("/lines")
                .then().log().all()
                .extract().jsonPath().getList(".", Line.class);
    }
}
