package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.SectionRequest;
import org.springframework.http.MediaType;

import java.util.Map;

public class LineSteps {

    public static Long 지하철노선_생성_및_아이디추출(LineRequest lineRequest) {
        return 지하철노선_생성(lineRequest).jsonPath().getLong("id");
    }

    public static LineResponse 지하철노선_생성_및_객체추출(LineRequest lineRequest) {
        return 지하철노선_생성(lineRequest).as(LineResponse.class);
    }

    public static ExtractableResponse<Response> 지하철노선_생성(LineRequest lineRequest) {
        return RestAssured
                .given().log().all()
                .body(lineRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 지하철구간_생성(Long id, SectionRequest sectionRequest) {
        return RestAssured
                .given().log().all()
                .body(sectionRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines/" + id + "/sections")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 지하철노선_삭제(long id) {
        return RestAssured
                .given().log().all()
                .when().delete("/lines/" + id)
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 지하철노선_수정(long id, Map<String, String> params) {
        return RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/lines/" + id)
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 지하철노선_조회(long id) {
        return RestAssured
                .given().log().all()
                .when().get("/lines/" + id)
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 지하철노선_목록_조회(Map<String, String> params) {
        return RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines")
                .then().log().all().extract();
    }
}
