package subway.restAssured;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class StationRestAssured {

    public Long save(String stationName) {
        Map<String, String> params = new HashMap<>();
        params.put("name", stationName);
        return RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .assertThat().statusCode(HttpStatus.CREATED.value())
                .extract().jsonPath().getObject("id", Long.class);
    }

    public ExtractableResponse<Response> findAll() {
        return RestAssured
                .given().log().all()
                .when().get("/stations")
                .then().log().all()
                .assertThat().statusCode(HttpStatus.OK.value())
                .extract();
    }

    public void delete(Long id) {
        RestAssured
                .given().log().all()
                .when()
                .delete("/stations/" + id)
                .then().log().all()
                .assertThat().statusCode(HttpStatus.NO_CONTENT.value())
                .extract();
    }
}
