package nextstep.subway.api;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.StationRequest;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.List;

public class StationApiCall {

    // 지하철역 생성 요청
    public static ExtractableResponse<Response> createStation(StationRequest request) {
        return RestAssured.given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();
    }

    // 복수의 지하철역 생성 요청
    public static List<ExtractableResponse<Response>> createStations(String...stationNames) {
        List<ExtractableResponse<Response>> extractableResponses = new ArrayList<>();
        for (String stationName : stationNames) {
            extractableResponses.add(createStation(new StationRequest(stationName)));
        }
        return extractableResponses;
    }


}
