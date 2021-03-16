package nextstep.subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class LineSteps {

    public static String getResponseURI(ExtractableResponse<Response> response){

        if(response==null) {
            return null;
        }

        return response.header("Location");
    }

    public static ExtractableResponse<Response> 지하철_노선_등록(String name, String color){

        ExtractableResponse<Response> createResponse;
        Map<String, String> param1 = new HashMap<>();

        param1.put("color", color);
        param1.put("name", name);

        createResponse = RestAssured.given().log().all()
                .body(param1)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().log().all()
                .extract();

        return createResponse;
    }

    public static ExtractableResponse<Response> 지하철_노선_등록(Map<String, String> param1){

        ExtractableResponse<Response> createResponse = RestAssured.given().log().all()
                .body(param1)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().log().all()
                .extract();

        return createResponse;
    }

    public static ExtractableResponse<Response> 지하철_노선_제거_요청(ExtractableResponse<Response> request){
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().delete(getResponseURI(request))
                .then().log().all().extract();

        return response;
    }

    public static ExtractableResponse<Response> 지하철_노선_조회_요청(Long lineId){
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().get("lines/{id}",lineId)
                .then().log().all().extract();
        return response;
    }

    public static ExtractableResponse<Response> 지하철_전체노선_조회_요청(ExtractableResponse<Response> request){
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().get(getResponseURI(request).split("/")[1])
                .then().log().all().extract();
        return response;
    }

    public static ExtractableResponse<Response> 지하철_노선_수정_요청(ExtractableResponse<Response> request,Map<String, String> param){
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(param)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .put(getResponseURI(request))
                .then().log().all().extract();

        return response;
    }

    public static ExtractableResponse<Response> 지하철_노선에_지하철역_등록(LineResponse line, StationResponse upStation,
                                                                StationResponse downStation, Integer distance){

        ExtractableResponse<Response> createResponse;

        //SectionRequest
        Map<String, Long> param1 = new HashMap<>();
        param1.put("upStationId", upStation.getId());
        param1.put("downStationId", downStation.getId());
        param1.put("distance",distance.longValue());

        createResponse = RestAssured.given().log().all()
                .body(param1)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines/{lineId}/sections",line.getId())
                .then().log().all()
                .extract();

        return createResponse;

    }

    //지하철_노선에서_지하철역_제거(초록2호선,선릉역)
    public static ExtractableResponse<Response> 지하철_노선에서_지하철역_제거(Long lineId, Long stationId) {
        return RestAssured.given().log().all().
                when().
                delete("/lines/{lineId}/sections?stationId={stationId}", lineId, stationId).
                then().
                log().all().
                extract();
    }

    public static ExtractableResponse<Response> 지하철_노선에_등록된역_조회_요청(Long lineId){
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().get("/lines/{lineId}/sections",lineId)
                .then().log().all().extract();

        return response;
    }

    public static ExtractableResponse<Response> 지하철_노선에서_마지막_구간_조회(Long lineId){
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().get("/lines/{lineId}/sections/last-section",lineId)
                .then().log().all().extract();
        return response;
    }

//    public static ExtractableResponse<Response> 지하철_노선_조회(LineResponse line){
//
//        ExtractableResponse<Response> createResponse = RestAssured.given().log().all()
//                .body(param1)
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .when()
//                .post("/lines/{lineId}",line.getId())
//                .then().log().all()
//                .extract();
//
//        return createResponse;
//    }

}
