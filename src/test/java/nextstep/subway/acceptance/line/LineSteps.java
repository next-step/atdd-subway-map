package nextstep.subway.acceptance.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static nextstep.subway.acceptance.common.CommonSteps.생성_성공_응답;
import static nextstep.subway.acceptance.station.StationSteps.지하철역_생성;
import static nextstep.subway.acceptance.station.StationSteps.지하철역_생성_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class LineSteps {


    public static final String SHIN_BUNDANG_LINE_NAME = "신분당선";
    public static final String SHIN_BUNDANG_LINE_COLOR = "bg-red-600";
    public static final String SHIN_BUNDANG_UP_STATION_NAME = "신논현역";
    public static final String SHIN_BUNDANG_DOWN_STATION_NAME = "강남역";

    public static final String FIRST_LINE_NAME = "1호선";
    public static final String FIRST_LINE_COLOR = "YELLOW";

    public static final String BUNDANG_LINE_NAME = "분당선";
    public static final String BUNDANG_LINE_COLOR = "bg-green-600";
    public static final String BUNDANG_UP_STATION_NAME = "분당노선상행역";
    public static final String BUNDANG_DOWN_STATION_NAME = "분당노선하행역";

    public static Long SHIN_BUNDANG_LINE_UP_STATION_ID = null;
    public static Long SHIN_BUNDANG_LINE_DOWN_STATION_ID = null;

    public static Long SHIN_BUNDANG_LINE_ID = null;

    public static final Long DISTANCE = 5L;
    public static final Long 노선_거리 = 5L;
//
//    public static final String 노선명 = "논현역";

    public static Long 노선_상행역_ID = null;
    public static Long 노선_하행역_ID = null;
    public static Long 노선_ID = null;
    static List<Long> 노선_역_목록_ID = new ArrayList<>();

    public static ExtractableResponse<Response> 노선_생성_요청(String 노선명, String 노선색, String 구간_상행역명, String 구간_하행역명, Long distance) {
        return RestAssured.given().log().all()
                .body(노선_생성_PARAM(노선명, 노선색, 지하철역_생성_ID(구간_상행역명), 지하철역_생성_ID(구간_하행역명), distance))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> 노선_상행역_생성(String 노선_상행역_명) {
        return 지하철역_생성(노선_상행역_명);
    }

    private static ExtractableResponse<Response> 노선_하행역_생성(String 노선_하행역_명) {
        return 지하철역_생성(노선_하행역_명);
    }

    public static List<Long> 노선_역_목록_ID(ExtractableResponse<Response> 노선_생성) {
        List<Long> 노선_역_목록_ID = new ArrayList<>();
        노선_역_목록_ID.add(노선_상행역_ID);
        노선_역_목록_ID.add(노선_하행역_ID);
        return 노선_역_목록_ID;
    }

    private static Map<String, String> 노선_생성_PARAM(String name, String color, Long upStationId, Long downStationId, Long distance) {
        Map<String, String> param = new HashMap<>();
        param.put("name", name);
        param.put("color", color);
        param.put("upStationId", String.valueOf(upStationId));
        param.put("downStationId", String.valueOf(downStationId));
        param.put("distance", String.valueOf(distance));
        return param;
    }

    public static ExtractableResponse<Response> 노선이_생성되어_있다(String 노선명, String 노선색, String 노선_상행역명, String 노선_하행역명, Long distance) {
        ExtractableResponse<Response> 노선_생성_응답 = 노선_생성_요청(노선명, 노선색, 노선_상행역명, 노선_하행역명, distance);
        노선_ID = 노선_생성_응답.jsonPath().getLong("id");
        노선_상행역_ID = 노선_생성_응답.jsonPath().getLong("upStation.id");
        노선_하행역_ID = 노선_생성_응답.jsonPath().getLong("downStation.id");
        생성된_노선_확인(노선_생성_응답, 노선명, 노선색);
        return 노선_생성_응답;
    }

    public static void 생성된_노선_확인(ExtractableResponse<Response> 노선_생성_응답, String 노선명, String 노선색) {
        assertAll(
                () -> 생성_성공_응답(노선_생성_응답),
                () -> assertThat(노선_생성_응답.jsonPath().getString("name")).isEqualTo(노선명),
                () -> assertThat(노선_생성_응답.jsonPath().getString("color")).isEqualTo(노선색),
                // then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
                () -> assertThat(지하철_노선_목록_조회().jsonPath().getList("name")).containsAnyOf(노선명)
        );
    }

    private static ExtractableResponse<Response> 지하철_노선_목록_조회() {
        return RestAssured.given().log().all()
                .when().get("/lines")
                .then().log().all()
                .extract();
    }

    public static void 생성된_노선_목록_확인(List<String> lineNames, String 노선명1, String 노선명2) {
        assertAll(
                () -> assertThat(lineNames.size()).isEqualTo(2),
                () -> assertThat(lineNames).containsAnyOf(노선명1),
                () -> assertThat(lineNames).containsAnyOf(노선명2)
        );
    }

    static List<String> 지하철_노선명_목록_조회() {
        return 지하철_노선_목록_조회().jsonPath().getList("name");
    }

    public static ExtractableResponse<Response> 지하철_노선_조회(Long id) {
        return RestAssured.given().log().all()
                .when().get("/lines/" + id)
                .then().log().all()
                .extract();
    }

    public static String 지하철_노선명_조회(Long id) {
        return 지하철_노선_조회(id).jsonPath().getString("name");
    }

    public static ExtractableResponse<Response> 지하철_노선_수정(Long id, String name, String color) {
        return RestAssured.given().log().all()
                .body(지하철_노선_수정_파라미터(name, color))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/lines/" + id)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_삭제(Long id) {
        return RestAssured.given().log().all()
                .when().delete("/lines/" + id)
                .then().log().all()
                .extract();
    }

    private static Map<String, String> 지하철_노선_수정_파라미터(String name, String color) {
        Map<String, String> param = new HashMap<>();
        param.put("name", name);
        param.put("color", color);
        return param;
    }
}

