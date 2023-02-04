package subway.util;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.common.Comment;
import subway.controller.request.LineRequest;
import subway.controller.request.StationRequest;
import subway.controller.response.LineResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.restassured.http.ContentType.JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class BaseAcceptanceTest extends AcceptanceExecutor {

    public static final String STATION_PATH = "/stations";
    public static final String LINE_PATH = "/lines";
    public static final String SECTION_PATH = "/sections";

    public static final String SHIN_BUN_DANG = "신분당선";
    public static final String LEE_HO_SEON = "이호선";

    public static final String 강남역 = "강남역";
    public static final String 역삼역 = "역삼역";

    public Map<String, Object> 신분당선;
    public Map<String, Object> 이호선;

    @Comment("지하철 노선을 생성하는 메서드")
    public ExtractableResponse<Response> 지하철노선_생성(final Map<String, Object> line) {
        final LineRequest param = MapHelper.readValue(line, LineRequest.class);

        return RestAssured.given().log().all()
                .contentType(JSON)
                .body(param)
                .when().post(LINE_PATH)
                .then().log().all()
                .extract();
    }


    @Comment("지하철 노선 목록의 이름을 반환하는 함수")
    public List<String> 지하철노선목록의_이름_조회() {
        return 지하철노선_목록조회().stream()
                .map(LineResponse::getName)
                .collect(Collectors.toList());
    }

    @Comment("지하철 노선 목록을 반환하는 함수")
    public List<LineResponse> 지하철노선_목록조회() {
        return RestAssured
                .given().accept(APPLICATION_JSON_VALUE)
                .when().get(LINE_PATH)
                .then().statusCode(HttpStatus.OK.value())
                .extract().jsonPath()
                .getList("$", LineResponse.class);
    }

    @Comment("지하철 노선을 수정하는 메서드")
    public void 지하철_노선을_수정한다(final Long id, String newLineName, String newLinColor) {
        RestAssured
                .given()
                .contentType(APPLICATION_JSON_VALUE)
                .body(Map.of("name", newLineName, "color", newLinColor))
                .when()
                .put(LINE_PATH + "/{id}", id)
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Comment("지하철 노선을 조회하는 메서드")
    public LineResponse 지하철노선_단건조회(Long id) {
        return RestAssured.given().log().all()
                .when().get(LINE_PATH + "/{id}", id)
                .then().log().all()
                .extract().jsonPath().getObject("", LineResponse.class);
    }

    @Comment("지하철 노선을 삭제하는 메서드")
    public ExtractableResponse<Response> 지하철_노선을_삭제한다(Long id) {
        return RestAssured
                .given()
                .when()
                .delete(LINE_PATH + "/{id}", id)
                .then()
                .extract();
    }

    /**
     * 지하철을 입력받아 /station 에 post 요청하는 편의 메서드
     *
     * @param name
     * @return response
     */
    public ExtractableResponse<Response> 지하철역_생성(String name) {
        Map<String, Object> param = new HashMap<>();
        param.put("name", name);

        return RestAssured
                .given().body(param)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(STATION_PATH)
                .then().log().all()
                .extract();
    }

}
