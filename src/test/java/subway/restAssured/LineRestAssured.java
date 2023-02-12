package subway.restAssured;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.dto.LineRequestTestDto;

import java.util.HashMap;
import java.util.Map;

public class LineRestAssured {

    public ExtractableResponse<Response> update(Long id, String lineChangeName, String lineChangeColor) {
        Map<String, String> lineParams = new HashMap<>();
        lineParams.put("name", lineChangeName);
        lineParams.put("color", lineChangeColor);

        return updateLine(lineParams, id);
    }

    public Long save(LineRequestTestDto testLineDto) {
        final Map<String, String> lineParams = new HashMap<>();
        putParams(lineParams, testLineDto);

        return saveLine(lineParams);
    }

    public ExtractableResponse<Response> delete(Long id) {
        return RestAssured
                .given().log().all()
                .when().delete("/lines/" + id)
                .then().log().all()
                .assertThat().statusCode(HttpStatus.NO_CONTENT.value())
                .extract();
    }

    private ExtractableResponse<Response> updateLine(Map<String, String> params, Long id) {
        return RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/lines/" + id)
                .then().log().all()
                .assertThat().statusCode(HttpStatus.OK.value())
                .extract();
    }

    public ExtractableResponse<Response> findById(Long id) {
        return RestAssured
                .given().log().all()
                .when().get("/lines/" + id)
                .then().log().all()
                .assertThat().statusCode(HttpStatus.OK.value())
                .extract();
    }

    public ExtractableResponse<Response> findAll() {
        return RestAssured
                .given().log().all()
                .when().get("/lines")
                .then().log().all()
                .assertThat().statusCode(HttpStatus.OK.value())
                .extract();
    }

    private Long saveLine(Map<String, String> params) {
        return RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .assertThat().statusCode(HttpStatus.CREATED.value())
                .extract().jsonPath().getObject("id", Long.class);
    }

    private void putParams(Map<String, String> params, LineRequestTestDto lineTestDto) {
        params.put("name", lineTestDto.getName());
        params.put("color", lineTestDto.getColor());
        params.put("upStationId", String.valueOf(lineTestDto.getUpStationId()));
        params.put("downStationId", String.valueOf(lineTestDto.getDownStationId()));
        params.put("distance", String.valueOf(lineTestDto.getDistance()));
    }
}
