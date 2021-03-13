package nextstep.subway.section;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.section.dto.SectionRequest;
import org.springframework.http.MediaType;

public class SectionHelper {

    public static SectionRequest Section_요청_만들기(Long upStationId, Long downStationId, int distance){
        return new SectionRequest(upStationId, downStationId, distance);

    }

    public static ExtractableResponse<Response> 구간_추가_요청(Long lineId, Long upStationId, Long downStationId, int distance){
        return RestAssured.given().pathParam("lineId", lineId)
                .body(Section_요청_만들기(upStationId, downStationId, distance))
                .contentType(MediaType.APPLICATION_JSON_VALUE).log().all()
                .when().post("/lines/{lineId}/sections")
                .then().log().all().extract();
    }
}
