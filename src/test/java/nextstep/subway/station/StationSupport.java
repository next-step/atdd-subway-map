package nextstep.subway.station;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.station.dto.StationRequest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
public class StationSupport {

    public Long 지하철역_등록되어_있음(String station) {
        return 지하철역_생성_요청(station)
                .jsonPath().getLong("id");
    }

    public ExtractableResponse<Response> 지하철역_생성_요청(String station) {
        return RestAssured.given().log().all()
                .body(new StationRequest(station))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract();
    }

    public ExtractableResponse<Response> 지하철역_조회_요청() {
        return RestAssured.given().log().all()
                .when()
                .get("/stations")
                .then().log().all()
                .extract();
    }

    public ExtractableResponse<Response> 지하철역_제거_요청(Long stationId) {
        return RestAssured.given().log().all()
                .when()
                .delete("/stations/{stationId}", stationId)
                .then().log().all()
                .extract();
    }
}
