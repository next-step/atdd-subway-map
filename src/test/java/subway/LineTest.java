package subway;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class LineTest {
    
    public static Long 노선을_생성한다(String name, String color, long upStationId, long downStationId, int distance)
    {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", distance);

        ExtractableResponse<Response> response = 
            RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        return response.jsonPath().getLong("id");
    }

    public static List<String> 노선_이름목록을_조회한다() 
    {
        List<String> resultList = null;

        ExtractableResponse<Response> response =
            RestAssured.given().log().all()
                .when().get("/lines")
                .then().log().all()
                .extract();
                
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        resultList = List.copyOf(response.jsonPath().getList("name", String.class));
        return resultList;
    }

    public static JsonPath 노선을_조회한다(long id)
    {
        ExtractableResponse<Response> response =
            RestAssured.given().log().all()
                .when().get("/lines/" + id)
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        return response.jsonPath();
    }

    public static void 노선을_수정한다(long id, String name, String color, long upStationId, long downStationId, int distance)
    {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", distance);

        ExtractableResponse<Response> response = 
            RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/lines/" + id)
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 노선을_삭제한다(long id)
    {
        ExtractableResponse<Response> response =
            RestAssured.given().log().all()
                .pathParam("id", id)
                .when().delete("/lines/{id}")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
