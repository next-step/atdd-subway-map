package nextstep.subway.api;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import nextstep.subway.applicaion.dto.StationLineRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static nextstep.subway.validate.HttpStatusValidate.*;

public class SectionApi {

    public static void 지하철_노선_삭제(Long stationLineId) {
        ExtractableResponse<Response> response = RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().delete("/lines/{id}", stationLineId)
            .then().log().all()
            .extract();

        상태코드_체크(response, HttpStatus.NO_CONTENT);
    }

    public static Long 지하철_노선_생성(String lineName,
        String color,
        Long upStationId,
        Long downStationId,
        Integer distance) {
        ExtractableResponse<Response> response = RestAssured
            .given().log().all()
            .body(StationLineRequest.builder()
                .name(lineName)
                .color(color)
                .upStationId(upStationId)
                .downStationId(downStationId)
                .distance(distance)
                .build())
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/lines")
            .then().log().all()
            .extract();

        상태코드_체크(response, HttpStatus.CREATED);

        return response.jsonPath().getLong("id");
    }

    public static void 지하철_노선_업데이트(Long stationLineId, StationLineRequest stationLineRequest) {
        ExtractableResponse<Response> response = RestAssured
            .given().log().all()
            .body(stationLineRequest)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().put("/lines/{id}", stationLineId)
            .then().log().all()
            .extract();

        상태코드_체크(response, HttpStatus.OK);
    }

    public static List<String> 지하철_노선_목록_조회() {
        ExtractableResponse<Response> response = RestAssured
            .given().log().all()
            .when().get("/lines")
            .then().log().all()
            .extract();

        상태코드_체크(response, HttpStatus.OK);
        return response.jsonPath().getList("name");
    }

    public static ExtractableResponse<Response> 지하철_노선_조회(Long stationLineId) {
        ExtractableResponse<Response> response = RestAssured
            .given().log().all()
            .when().get("/lines/{id}", stationLineId)
            .then().log().all()
            .extract();

        상태코드_체크(response, HttpStatus.OK);

        return response;
    }

}
