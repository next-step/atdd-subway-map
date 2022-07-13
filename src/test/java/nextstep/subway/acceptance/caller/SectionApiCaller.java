package nextstep.subway.acceptance.caller;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.Map;

public class SectionApiCaller {

    public void setPort(int port) {
        RestAssured.port = port;
    }

    public ExtractableResponse<Response> createLineSectionById(long id, Map<String, Object> params) {
        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines/{id}/sections", id)
                .then().log().all()
                .extract();
    }

    public ExtractableResponse<Response> deleteLineSectionById(long lineId, long stationId) {
        return RestAssured.given().log().all()
                .when().delete("/lines/{lineId}/sections?stationId={stationId}", lineId, stationId)
                .then().log().all()
                .extract();
    }

    public ExtractableResponse<Response> getLineSectionById(long lineId) {
        return RestAssured.given().log().all()
                .when().get("/lines/{lineId}/sections", lineId)
                .then().log().all()
                .extract();
    }
}
