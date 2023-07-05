package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class SubwayTestFixture {
    public static ExtractableResponse<Response> 지하철_역_생성_요청(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);

        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();
    }

    public static int 지하철_역_생성_요청_상태_코드_반환(String name) {
        return 지하철_역_생성_요청(name)
            .statusCode();
    }

    public static List<String> 지하철_역_목록_조회_요청_역_이름_목록_반환() {

        return RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/stations")
            .then().log().all()
            .extract()
            .jsonPath().getList("name", String.class);
    }

    public static ExtractableResponse<Response> 지하철_역_삭제_요청(Integer id) {

        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/stations/" + id)
                .then().log().all()
                .extract();
    }
}
