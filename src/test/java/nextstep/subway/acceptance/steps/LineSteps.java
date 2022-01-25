package nextstep.subway.acceptance.steps;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class LineSteps {

    private LineSteps() {}

    public static ExtractableResponse<Response> 지하철노선_생성요청(LineRequest request) {
        Map<String, String> params = new HashMap<>();
        params.put("name", request.getName());
        params.put("color", request.getColor());
        params.put("upStationId", String.valueOf(request.getUpStationId()));
        params.put("downStationId", String.valueOf(request.getDownStationId()));
        params.put("distance", String.valueOf(request.getDistance()));

        return RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 지하철노선_단건조회(Long lineId) {
        return RestAssured
                .given().log().all()
                .when().get("/lines/" + lineId)
                .then().log().all().extract();
    }
}
