package nextstep.subway.api;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Collections;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static nextstep.subway.validate.HttpStatusValidate.*;

public class StationApi {

    public static Long 지하철역_생성(String name) {
        ExtractableResponse<Response> response = RestAssured
            .given().log().all()
            .body(Collections.singletonMap("name", name))
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/stations")
            .then().log().all()
            .extract();

        상태코드_체크(response, HttpStatus.CREATED);

        return response.jsonPath().getLong("id");
    }

    public static List<String> 지하철역_목록_조회() {
        ExtractableResponse<Response> response = RestAssured
            .given().log().all()
            .when().get("/stations")
            .then().log().all()
            .extract();

        상태코드_체크(response, HttpStatus.OK);

        return response.jsonPath().getList("name");
    }

    public static void 지하철역_삭제(Long id) {
        ExtractableResponse<Response> response = RestAssured
            .given().log().all()
            .when().delete("/stations/{id}", id)
            .then().log().all()
            .extract();

        상태코드_체크(response, HttpStatus.NO_CONTENT);
    }

}
