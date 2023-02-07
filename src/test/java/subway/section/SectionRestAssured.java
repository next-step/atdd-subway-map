package subway.section;

import static subway.line.LineRestAssured.LINE_BASE_PATH;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Map;

public class SectionRestAssured {

    private static final String SECTION_BASE_PATH = LINE_BASE_PATH + "/{id}/sections";

    public static ExtractableResponse<Response> 구간_등록(
            final Long lineId,
            final Long downStationId,
            final Long upStationId,
            final int distance
    ) {
        Map body = Map.of(
                "downStationId", downStationId.toString(),
                "upStationId", upStationId.toString(),
                "distance", distance
        );

        return RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post(SECTION_BASE_PATH, lineId)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 구간_제거(
            final Long lineId,
            final Long stationId
    ) {
        return RestAssured
                .given().log().all()
                .param("stationId",stationId)
                .when()
                .delete(SECTION_BASE_PATH, lineId)
                .then().log().all()
                .extract();
    }
}
