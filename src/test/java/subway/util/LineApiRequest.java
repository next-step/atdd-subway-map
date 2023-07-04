package subway.util;

import static subway.util.StationApiRequest.강남역;
import static subway.util.StationApiRequest.양재역;
import static subway.util.StationApiRequest.이매역;
import static subway.util.StationApiRequest.지하철역_리스폰_변환;
import static subway.util.StationApiRequest.지하철역_생성_요청;
import static subway.util.StationApiRequest.판교역;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;
import subway.dto.LineCreateRequest;
import subway.dto.LineResponse;
import subway.dto.LineUpdateRequest;
import subway.dto.SectionCreateRequest;
import subway.dto.StationResponse;

public class LineApiRequest {

    public static final String 신분당선_이름 = "신분당선";
    public static final String 신분당선_색상 = "red";
    public static final String 경강선_이름 = "경강선";
    public static final String 경강선_색상 = "blue";

    public static ExtractableResponse<Response> 지하철_노선_생성_요청(String name, String color,
        Long upStationId, Long downStationId, Integer distance) {
        return RestAssured.given().log().all()
            .body(new LineCreateRequest(name, color, upStationId, downStationId, distance))
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/lines")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_목록_조회_요청() {
        return RestAssured.given().log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/lines")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_조회_요청(Long id) {
        return RestAssured.given().log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/lines/{id}", id)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_수정_요청(Long id, String name, String color) {
        return RestAssured.given().log().all()
            .body(new LineUpdateRequest(name, color))
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().put("/lines/{id}", id)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_삭제_요청(Long id) {
        return RestAssured.given().log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().delete("/lines/{id}", id)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선에_구간_생성_요청(Long id, Long upStationId,
        Long downStationId, Integer distance) {
        return RestAssured.given().log().all()
            .body(new SectionCreateRequest(upStationId, downStationId, distance))
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/lines/{id}/sections", id)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 신분당선_생성() {
        StationResponse upStation = 지하철역_리스폰_변환(지하철역_생성_요청(강남역));
        StationResponse downStation = 지하철역_리스폰_변환(지하철역_생성_요청(양재역));
        return 지하철_노선_생성_요청(신분당선_이름, 신분당선_색상, upStation.getId(), downStation.getId(), 10);
    }

    public static ExtractableResponse<Response> 경강선_생성() {
        StationResponse upStation = 지하철역_리스폰_변환(지하철역_생성_요청(판교역));
        StationResponse downStation = 지하철역_리스폰_변환(지하철역_생성_요청(이매역));
        return 지하철_노선_생성_요청(경강선_이름, 경강선_색상, upStation.getId(), downStation.getId(), 10);
    }

    public static LineResponse 지하철_노선_리스폰_변환(ExtractableResponse<Response> response) {
        return response.response()
            .as(LineResponse.class);
    }
}
