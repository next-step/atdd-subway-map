package support.step;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class StationSteps {

    public static final String BASE_PATH = "/stations";

    public static ExtractableResponse<Response> 지하철역_목록_조회_요청() {
        return RestAssured.given().log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get(BASE_PATH)
            .then().log().all()
            .extract();
    }


    public static ExtractableResponse<Response> 지하철_역_생성_요청(Map<String, String> request) {
        return RestAssured.given()
            .body(request)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post(BASE_PATH)
            .then()
            .statusCode(HttpStatus.CREATED.value())
            .extract();
    }

    public static ExtractableResponse<Response> 지하철_역_삭제_요청(Long 지하철역_아이디) {
        return RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().delete(String.format("%s/{stationId}", BASE_PATH), 지하철역_아이디)
            .then().log().all()
            .extract();
    }


    public static Long 지하철역_응답에서_아이디_추출(ExtractableResponse<Response> 지하철역_응답) {
        return 지하철역_응답.jsonPath().getLong("id");
    }

    public static List<String> 지하철역_응답에서_이름_목록_추출(ExtractableResponse<Response> 지하철역_응답) {
        return 지하철역_응답.jsonPath().getList("name", String.class);
    }
}
