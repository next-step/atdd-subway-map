package subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class LineApi {
    public static ExtractableResponse<Response> 지하철노선_생성(final LineCreateRequest lineCreateRequest) {
        return RestAssured
                    .given()
                        .contentType(APPLICATION_JSON_VALUE)
                        .accept(APPLICATION_JSON_VALUE)
                        .body(lineCreateRequest)
                    .when()
                        .post("/lines")
                    .then()
                    .extract();
    }

    public static LineResponse 지하철노선_생성후_응답객체반환(final LineCreateRequest lineCreateRequest) {
        return 지하철노선_생성(lineCreateRequest).as(LineResponse.class);
    }

    public static ExtractableResponse<Response> 지하철노선_목록_조회() {
        return RestAssured
                    .given()
                        .accept(APPLICATION_JSON_VALUE)
                    .when()
                        .get("/lines")
                    .then()
                        .statusCode(HttpStatus.OK.value())
                    .extract();
    }

    public static List<String> 지하철노선_이름_목록_조회() {
        return 지하철노선_목록_조회()
                    .jsonPath()
                    .getList("name", String.class);
    }

    public static ExtractableResponse<Response> 지하철노선_단건_조회(final Long id) {
        return RestAssured
                    .given()
                        .accept(APPLICATION_JSON_VALUE)
                    .when()
                        .get("/lines/{id}", id)
                    .then()
                .log().all()
                    .extract();
    }

    public static LineResponse 지하철노선_단건_조회후_응답객체반환(final Long id) {
        return 지하철노선_단건_조회(id).as(LineResponse.class);
    }

    public static ExtractableResponse<Response> 지하철노선_수정(final Long id, final LineUpdateRequest lineUpdateRequest) {
        return RestAssured
                    .given()
                        .contentType(APPLICATION_JSON_VALUE)
                        .body(lineUpdateRequest)
                    .when()
                       .put("/lines/{id}", id)
                    .then()
                    .extract();
    }

    public static ExtractableResponse<Response> 지하철노선_삭제(final Long id) {
        return RestAssured
                    .given()
                    .when()
                        .delete("/lines/{id}", id)
                    .then()
                    .extract();
    }
}
