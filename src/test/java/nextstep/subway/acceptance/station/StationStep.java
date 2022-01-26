package nextstep.subway.acceptance.station;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.station.domain.dto.StationRequest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

@Service
public class StationStep {
    public static final String DUMMY_STATION_NAME = "1호선";

    public ExtractableResponse<Response> 지하철역_생성_요청(String name) {
        StationRequest request = new StationRequest(name);

        return RestAssured.given().log().all()
                          .body(request)
                          .contentType(MediaType.APPLICATION_JSON_VALUE)
                          .when()
                          .post("/stations")
                          .then().log().all()
                          .extract();
    }
}
