package nextstep.subway.acceptance.line;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.UpdateLineRequest;
import org.springframework.http.MediaType;

import java.util.List;

public class LineSteps {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static String params;

    public static ExtractableResponse<Response> 지하철노선_생성_요청(String name, String color, int distance, Long upStationId, Long downStationId) throws JsonProcessingException {
        params = objectMapper.writeValueAsString(new LineRequest(name, color, distance, upStationId, downStationId));

        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();
    }

    public static LineResponse 지하철노선_생성_결과(String name, String color, int distance, Long upStationId, Long downStationId) throws JsonProcessingException {
        return 지하철노선_생성_요청(name, color, distance, upStationId, downStationId).as(LineResponse.class);
    }

    public static ExtractableResponse<Response> 지하철노선_수정_요청(String path, String name, String color) throws JsonProcessingException {
        params = objectMapper.writeValueAsString(new UpdateLineRequest(name, color));

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

    public static ExtractableResponse<Response> 지하철노선_조회_요청(String path) {
        return RestAssured.given().log().all()
                .when().get(path)
                .then().log().all()
                .extract();
    }

    public static LineResponse 지하철노선_조회_결과(String path) {
        return 지하철노선_조회_요청(path).as(LineResponse.class);
    }

    public static ExtractableResponse<Response> 지하철노선목록_조회_요청() {
        return RestAssured.given().log().all()
                .when().get("/lines")
                .then().log().all()
                .extract();
    }

    public static List<LineResponse> 지하철노선목록_조회_결과() {
        return 지하철노선목록_조회_요청()
                .jsonPath().getList(".", LineResponse.class);
    }
}
