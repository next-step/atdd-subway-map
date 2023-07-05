package subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;

public class LineTestFixture {

    // body 채워야함
    public static ExtractableResponse<Response> 지하철_노선_생성_요청(LineRequest lineRequest) {
        return RestAssured.given().log().all()
            .when().post("/lines")
            .then().log().all()
            .extract();
    }

    public static LineResponse 지하철_노선_생성_요청_노선_정보_반환(LineRequest lineRequest) {
        return 지하철_노선_생성_요청(lineRequest)
            .as(LineResponse.class);
    }

    public static ExtractableResponse<Response> 지하철_노선_목록_조회_요청() {
        return RestAssured.given().log().all()
            .when().get("/lines")
            .then().log().all()
            .extract();
    }

    public static List<LineResponse> 지하철_노선_목록_조회_요청_노선_목록_반환() {
        return 지하철_노선_목록_조회_요청()
            .jsonPath().getList(".", LineResponse.class);
    }

    public static ExtractableResponse<Response> 지하철_노선_수정_요청(Long id, LineUpdateRequest lineUpdateRequest) {
        return RestAssured.given().log().all()
            .when().patch("/lines/" + id)
            .then().log().all()
            .extract();
    }

    public static int 지하철_노선_수정_요청_상태_코드_반환(Long id, LineUpdateRequest lineUpdateRequest) {
        return 지하철_노선_수정_요청(id, lineUpdateRequest)
            .statusCode();
    }

    public static ExtractableResponse<Response> 지하철_노선_삭제_요청(Long id) {
        return RestAssured.given().log().all()
            .when().delete("/lines/" + id)
            .then().log().all()
            .extract();
    }

    public static int 지하철_노선_삭제_요청_상태_코드_반환(Long id) {
        return 지하철_노선_삭제_요청(id)
            .statusCode();
    }
}
