package subway.section.fixture;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;

public class SectionRequestFixture {

    public static ExtractableResponse<Response> 지하철구간_조회(ExtractableResponse<Response> response) {
        long sectionId = response.jsonPath().getLong("id");
        return RestAssured.given().log().all()
                .when().get("/sections/" + sectionId)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철구간_등록(Long upStationId, Long downStationId) {
        final long lineId = 1L;
        HashMap<String, Object> params = new HashMap<>();
        params.put("downStationId", downStationId);
        params.put("upStationId", upStationId);
        params.put("distance", 10);
        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines/" +  lineId + "/sections")
                .then().log().all().extract();
    }
}
