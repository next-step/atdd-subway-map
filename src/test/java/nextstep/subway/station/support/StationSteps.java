package nextstep.subway.station.support;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.station.dto.StationRequest;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.http.MediaType;

public class StationSteps {

    private static final String STATION_BASE_URI = "/stations";

    public static ExtractableResponse<Response> 지하철역_생성_요청(StationRequest stationRequest) {
        return RestAssured
                .given().log().all()
                .body(stationRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post(STATION_BASE_URI)
                .then().log().all()
                .extract();
    }

    public static StationResponse 지하철역_등록됨(StationRequest stationRequest) {
        ExtractableResponse<Response> response = 지하철역_생성_요청(stationRequest);
        return response.jsonPath().getObject(".", StationResponse.class);
    }

    public static ExtractableResponse<Response> 지하철역_삭제_요청(Long id) {
        return RestAssured.given().log().all()
                .when()
                .delete(STATION_BASE_URI + "/" + id)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철역_목록_조회요청() {
        return RestAssured.given().log().all()
                .when()
                .get(STATION_BASE_URI)
                .then().log().all()
                .extract();
    }
}
