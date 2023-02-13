package subway.section;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;
import subway.line.SectionRequest;

public class SectionApi {
    public static ExtractableResponse<Response> 지하철구간_등록(final Long id, final SectionRequest sectionRequest) {
        return RestAssured
                    .given()
                        .accept(MediaType.ALL_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(sectionRequest)
                    .when()
                        .post("/lines/{id}/sections", id)
                    .then()
                    .extract();
    }

    public static ExtractableResponse<Response> 지하철구간_삭제(Long lineId, Long stationId) {
        return RestAssured
                    .given()
                        .accept(MediaType.ALL_VALUE)
                    .when()
                        .delete("/lines/{lineId}/sections?stationId={stationId}", lineId, stationId)
                    .then()
                    .extract();
    }
}
