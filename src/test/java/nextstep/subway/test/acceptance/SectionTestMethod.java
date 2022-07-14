package nextstep.subway.test.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

/**
 * @author a1101466 on 2022/07/10
 * @project subway
 * @description
 */
public class SectionTestMethod {
    public static ExtractableResponse<Response> 구간_생성(Long 구간시작ID, Long 구간종료ID, Long 라인ID){
        Map<String, Object> params = new HashMap<>();
        params.put("downStationId", 구간종료ID );
        params.put("upStationId", 구간시작ID);
        params.put("distance", "10");

        return RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines/{lineId}/sections", 라인ID)
                .then().log().all()
                .extract();
    }
    public static ExtractableResponse<Response> 구간_제거(Long 삭제대상라인ID, Long 삭제대상_StatsId){
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .delete("/lines/{lineId}/sections/{deleteStationId}",삭제대상라인ID, 삭제대상_StatsId)
                .then().log().all()
                .extract();

    }

    public static ExtractableResponse<Response> 구간목록을_조회한다(){
        return RestAssured
                .given().log().all()
                .when()
                .get("/lines/sections")
                .then().log().all()
                .extract();
    }
}
