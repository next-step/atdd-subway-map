package nextstep.subway.acceptance.section;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.acceptance.common.CommonSteps.생성_성공_응답;
import static nextstep.subway.acceptance.line.LineSteps.*;
import static nextstep.subway.acceptance.station.StationSteps.*;

public class SectionSteps {

    public static ExtractableResponse<Response> 구간이_등록되어_있다() {
        //Given 노선 생성
        ExtractableResponse<Response> 노선 = 노선_생성_요청(SHIN_BUNDANG_LINE_NAME, SHIN_BUNDANG_LINE_COLOR, SHIN_BUNDANG_UP_STATION_NAME, SHIN_BUNDANG_DOWN_STATION_NAME, DISTANCE);
        Long 노선_ID = 노선.jsonPath().getLong("id");
        Long 구간_상행역_ID = 지하철역_생성(SHIN_NONHYUN_STATION_NAME).jsonPath().getLong("id");
        Long 구간_하행역_ID = 지하철역_생성(GANGNAM_STATION_NAME).jsonPath().getLong("id");
        //When 구간 등록
        ExtractableResponse<Response> 구간 = 구간_등록_요청(노선_ID, 구간_상행역_ID, 구간_하행역_ID, DISTANCE);
        생성_성공_응답(구간);
        return 구간;
    }

    public static ExtractableResponse<Response> 구간_등록_요청(Long 노선_ID, Long 구간_상행역_ID, Long 구간_하행역_ID, Long distance) {
        return RestAssured.given().log().all()
                .body(구간_등록_PARAM(구간_상행역_ID, 구간_하행역_ID, distance))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines/" + 노선_ID + "/sections")
                .then().log().all()
                .extract();
    }

    private static Map<String, String> 구간_등록_PARAM(Long upStationId, Long downStationId, Long distance) {
        Map<String, String> param = new HashMap<>();
        param.put("upStationId", String.valueOf(upStationId));
        param.put("downStationId", String.valueOf(downStationId));
        param.put("distance", String.valueOf(distance));
        return param;
    }

    public static ExtractableResponse<Response> 구간_삭제(Long 노선ID, Long 구간_하행역_ID) {
        return RestAssured.given().log().all()
                .param("stationId", String.valueOf(구간_하행역_ID))
                .when().delete("/lines/" + 노선ID + "/sections")
                .then().log().all()
                .extract();
    }

}