package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("노선 관련 기능")
@Sql({"/station.sql", "/line.sql"})
public class LineAcceptanceTest extends AcceptanceTest {

    public static ExtractableResponse<Response> 지하철노선_생성_요청_응답(String lineName, long upStationId, long downStationId, String color, int distance) {

        Map<String, Object> params = new HashMap<>();
        params.put("name", lineName);
        params.put("color", color);
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", distance);

        //when(지하철 노선을 생성한다.)
        ExtractableResponse<Response> createResponse = post("/lines", params);

        return createResponse;
    }

    public static ExtractableResponse<Response> 지하철노선_생성_요청_응답(String lineName, String upStationName, String downStationName, String color, int distance) {
        ExtractableResponse<Response> upStationResponse = StationAcceptanceTest.지하철역_생성_요청_응답(upStationName);
        assertThat(upStationResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        Long upStationId = upStationResponse
                .jsonPath()
                .getLong("id");

        ExtractableResponse<Response> downStationResponse = StationAcceptanceTest.지하철역_생성_요청_응답(downStationName);
        assertThat(downStationResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        Long downStationId = downStationResponse
                .jsonPath()
                .getLong("id");

        Map<String, Object> params = new HashMap<>();
        params.put("name", lineName);
        params.put("color", color);
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", distance);

        //when(지하철 노선을 생성한다.)

        ExtractableResponse<Response> createResponse = post("/lines", params);
        return createResponse;
    }

    public static ExtractableResponse<Response> 지하철노선_리스트_조회_응답() {
        ExtractableResponse<Response> response = get("/lines");
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        return response;
    }

    public static ExtractableResponse<Response> 지하철노선_조회_응답(long lineId) {
        ExtractableResponse<Response> response = get("/lines/" + lineId);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        return response;
    }

    @DisplayName("지하철 노선 생성")
    @Test
    void createLine() {

        //given(상행역, 하행역 지하철역을 생성한다.)
        final String 시청역 = "시청역";
        final String 강남역 = "강남역";
        final String 신분당선 = "신분당선";

        ExtractableResponse<Response> createResponse = 지하철노선_생성_요청_응답(신분당선, 시청역, 강남역, "bg-red-600", 10);

        //then
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("지하철 노선 생성 시 distance가 0보다 작거나 같을 때 예외")
    @Test
    void InternalServerErrorIfCreateLineByDistanceZeroAndNegative() {

        //given(지하철역을 생성한다)
        final String 시청역 = "시청역";
        final String 강남역 = "강남역";
        final String 신분당선 = "신분당선";

        //경계선 테스트를 위한 distance 0
        ExtractableResponse<Response> createResponse = 지하철노선_생성_요청_응답(신분당선, 시청역, 강남역, "bg-red-600", 0);
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @DisplayName("지하철 노선 생성 시 상행과 하행이 같을 때 예외")
    @Test
    void InternalServerErrorIfCreateLineByStationEquals() {

        //given
        final String 시청역 = "시청역";
        final String 신분당선 = "신분당선";

        ExtractableResponse<Response> createStationResponse = StationAcceptanceTest.지하철역_생성_요청_응답(시청역);
        assertThat(createStationResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        long stationId = createStationResponse.jsonPath().getLong("id");

        //when
        ExtractableResponse<Response> createResponse = 지하철노선_생성_요청_응답(신분당선, stationId, stationId, "bg-red-600", 10);

        //then
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @DisplayName("지하철 노선 목록 조회")
    @Test
    void getLines() {

        //given(2개 이상의 지하철 노선을 생성한다.)
        final String 신분당선 = "신분당선";
        final String 강남역 = "강남역";
        final String 시청역 = "시청역";

        final String 일호선 = "일호선";
        final String 구로역 = "구로역";
        final String 신도림역 = "신도림역";

        ExtractableResponse<Response> lineResponseOne = 지하철노선_생성_요청_응답(신분당선, 시청역, 강남역, "bg-red-600", 10);
        ExtractableResponse<Response> lineResponseTwo = 지하철노선_생성_요청_응답(일호선, 구로역, 신도림역, "bg-red-200", 15);
        assertThat(lineResponseOne.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(lineResponseTwo.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        //when
        ExtractableResponse<Response> getResponse = 지하철노선_리스트_조회_응답();

        //then
        //상태코드 200 확인
        assertThat(getResponse.statusCode()).isEqualTo(HttpStatus.OK.value());

        //앞서 생성한 지하철 노선이 지하철 노선 목록 중에 존재하는지 확인
        assertThat(getResponse.jsonPath().getList("name", String.class)).contains(신분당선, 일호선);
    }

    @DisplayName("지하철 노선 조회")
    @Test
    void getLine() {

        //given(지하철 노선을 생성한다.)
        final String 신분당선 = "신분당선";
        final String 강남역 = "강남역";
        final String 시청역 = "시청역";
        final String color = "bg-red-600";

        ExtractableResponse<Response> createResponse = 지하철노선_생성_요청_응답(신분당선, 시청역, 강남역, color, 10);
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        long lineId = createResponse.jsonPath().getLong("id");

        //when(생성한 지하철 노선을 조회한다.)
        ExtractableResponse<Response> getResponse = 지하철노선_조회_응답(lineId);

        //then
        //지하철 노선 id 확인
        assertThat(getResponse.jsonPath().getLong("id")).isEqualTo(lineId);

        //지하철 노선명 확인
        assertThat(getResponse.jsonPath().getString("name")).isEqualTo(신분당선);
    }

    @DisplayName("지하철 노선 수정")
    @Test
    void updateLine() {

        //given(지하철 노선을 생성한다.)
        final String 신분당선 = "신분당선";
        final String 강남역 = "강남역";
        final String 시청역 = "시청역";
        final String 일호선 = "일호선";
        final String color = "bg-red-600";
        final String updateColor = "bg-red-700";

        ExtractableResponse<Response> createResponse = 지하철노선_생성_요청_응답(신분당선, 시청역, 강남역, color, 10);
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        long lineId = createResponse.jsonPath().getLong("id");

        //when(생성한 지하철 노선을 수정한다.)
        Map<String, Object> params = new HashMap<>();
        params.put("name", 일호선);
        params.put("color", updateColor);
        ExtractableResponse<Response> updateResponse = put("/lines/" + lineId, params);

        //then(수정된 지하철 노선을 확인한다.)
        
        //상태코드 200 확인
        assertThat(updateResponse.statusCode()).isEqualTo(HttpStatus.OK.value());

        ExtractableResponse<Response> getResponse = 지하철노선_조회_응답(lineId);

        //수정된 지하철 노선명 확인
        assertThat(getResponse.jsonPath().getString("name")).isEqualTo(일호선);

        //수정된 지하철 노선 색 확인
        assertThat(getResponse.jsonPath().getString("color")).isEqualTo(updateColor);

    }

    @DisplayName("지하철 노선 삭제")
    @Test
    void deleteLine() {

        //given(지하철 노선을 생성한다.)
        final String 신분당선 = "신분당선";
        final String 강남역 = "강남역";
        final String 시청역 = "시청역";
        final String color = "bg-red-600";

        ExtractableResponse<Response> createResponse = 지하철노선_생성_요청_응답(신분당선, 시청역, 강남역, color, 10);
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        long lineId = createResponse.jsonPath().getLong("id");

        //when(생성한 지하철 노선을 삭제한다.)
        ExtractableResponse<Response> deleteResponse = delete("/lines/" + lineId);

        //then(삭제가 되었는지 확인한다.)
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    /**
     * given : 지하철 노선의 상행과 하행역(지하철역)을 생성한다.
     * given : 지하철 노선을 생성한다.
     * when : 지하철 노선에 구간(새로운구간의 상행이 지하철 노선의 하행)을 등록 요청하면
     * then : 지하철 노선에 구간이 등록된다.
     *
     *         신분당선         추가되는 구간
     * 강남역 ---------- 시청역 ---------- 구로디지털단지역
     *
     */
    @DisplayName("지하철 노선에 구간을 등록한다. (새로운 구간의 상행이 지하철 노선의 하행과 동일)")
    @Test
    void addSectionOfUpStationEqualsDownStation() {

        final String 신분당선 = "신분당선";
        final String 강남역 = "강남역";
        final String 시청역 = "시청역";
        final String 구로디지털단지역 = "구로디지털단지역";
        final String color = "bg-red-600";
        final int distance = 10;

        //given
        ExtractableResponse<Response> 구로디지털단지역_응답 = StationAcceptanceTest.지하철역_생성_요청_응답(구로디지털단지역);
        long 구로디지털단지역_아이디 = 구로디지털단지역_응답.jsonPath().getLong("id");

        ExtractableResponse<Response> 강남역_응답 = StationAcceptanceTest.지하철역_생성_요청_응답(강남역);
        long 강남역_아이디 = 강남역_응답.jsonPath().getLong("id");

        ExtractableResponse<Response> 시청역_응답 = StationAcceptanceTest.지하철역_생성_요청_응답(시청역);
        long 시청역_아이디 = 시청역_응답.jsonPath().getLong("id");

        ExtractableResponse<Response> 신분당선_응답 = 지하철노선_생성_요청_응답(신분당선, 강남역_아이디, 시청역_아이디, color, distance);
        long 신분당선_아이디 = 신분당선_응답.jsonPath().getLong("id");

        final String url = "/lines/" + 신분당선_아이디 + "/sections";
        Map<String, Object> params = new HashMap<>();
        params.put("downStationId", 구로디지털단지역_아이디); // 구로디지털단지역
        params.put("upStationId", 시청역_아이디); // 시청역
        params.put("distance", 5);

        //when
        ExtractableResponse<Response> 구간등록_응답 = post(url, params);

        //then
        assertThat(구간등록_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    /**
     * given : 지하철 노선의 상행과 하행역(지하철역)을 생성한다.
     * given : 지하철 노선을 생성한다.
     * when : 지하철 노선에 구간(새로운구간의 하행이 지하철 노선의 상행)을 등록 요청하면
     * then : 지하철 노선에 구간이 등록된다.
     *
     *               추가되는 구간        신분당선
     * 구로디지털단지역 ---------- 강남역 ---------- 시청역
     *
     */
    @DisplayName("지하철 노선에 구간을 등록한다. (새로운 구간의 하행이 지하철 노선의 상행과 동일)")
    @Test
    void addSectionOfDownStationEqualsUpStation() {

        final String 신분당선 = "신분당선";
        final String 강남역 = "강남역";
        final String 시청역 = "시청역";
        final String 구로디지털단지역 = "구로디지털단지역";
        final String color = "bg-red-600";
        final int distance = 10;

        //given
        ExtractableResponse<Response> 구로디지털단지역_응답 = StationAcceptanceTest.지하철역_생성_요청_응답(구로디지털단지역);
        long 구로디지털단지역_아이디 = 구로디지털단지역_응답.jsonPath().getLong("id");

        ExtractableResponse<Response> 강남역_응답 = StationAcceptanceTest.지하철역_생성_요청_응답(강남역);
        long 강남역_아이디 = 강남역_응답.jsonPath().getLong("id");

        ExtractableResponse<Response> 시청역_응답 = StationAcceptanceTest.지하철역_생성_요청_응답(시청역);
        long 시청역_아이디 = 시청역_응답.jsonPath().getLong("id");

        ExtractableResponse<Response> 신분당선_응답 = 지하철노선_생성_요청_응답(신분당선, 강남역_아이디, 시청역_아이디, color, distance);
        long 신분당선_아이디 = 신분당선_응답.jsonPath().getLong("id");

        final String url = "/lines/" + 신분당선_아이디 + "/sections";
        Map<String, Object> params = new HashMap<>();
        params.put("downStationId", 강남역_아이디); // 강남역
        params.put("upStationId", 구로디지털단지역_아이디); // 구로디지털단지역
        params.put("distance", 5);

        //when
        ExtractableResponse<Response> 구간등록_응답 = post(url, params);

        //then
        assertThat(구간등록_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    /**
     * given : 지하철 노선의 상행과 하행을 생성한다.
     * given : 지하철 노선을 생성한다.
     * when : 지하철 노선에 구간을 등록한다. (예) 지하철 노선의 하행과 새로운 구간의 하행, 지하철 노선의 상행과 새로운 구간의 상행
     * then : 예외가 발생한다.
     */
    @DisplayName("지하철 노선에 구간을 등록한다. (지하철 노선의 하행과 새로운 구간의 하행일경우 혹은 지하철 노선의 상행과 새로운 구간의 상행일경우)")
    @Test
    void addSectionIfMiddleOfLine() {

        final String 신분당선 = "신분당선";
        final String 강남역 = "강남역";
        final String 시청역 = "시청역";
        final String color = "bg-red-600";
        final int distance = 10;

        //given
        // 지하철 노선의 상행 생성
        ExtractableResponse<Response> 강남역_응답 = StationAcceptanceTest.지하철역_생성_요청_응답(강남역);
        long 강남역_아이디 = 강남역_응답.jsonPath().getLong("id");

        // 지하철 노선의 하행 생성
        ExtractableResponse<Response> 시청역_응답 = StationAcceptanceTest.지하철역_생성_요청_응답(시청역);
        long 시청역_아이디 = 시청역_응답.jsonPath().getLong("id");

        // 지하철 노선 생성
        ExtractableResponse<Response> 신분당선_응답 = 지하철노선_생성_요청_응답(신분당선, 강남역_아이디, 시청역_아이디, color, distance);
        long 신분당선_아이디 = 신분당선_응답.jsonPath().getLong("id");

        final String url = "/lines/" + 신분당선_아이디 + "/sections";
        Map<String, Object> params = new HashMap<>();
        params.put("downStationId", 시청역_아이디); // 시청역
        params.put("upStationId", 강남역_아이디); // 강남역
        params.put("distance", 5);

        //when
        ExtractableResponse<Response> 구간등록_응답 = post(url, params);

        //then
        assertThat(구간등록_응답.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    /**
     * given : 지하철역을 생성한다.
     * given : 지하철 노선을 생성한다.
     * given : 지하철 구간을 등록한다.
     * when : 등록된 구간을 제거한다.
     * then : 지하철 노선에 구간이 1개 존재한다.
     */
    @DisplayName("지하철 노선에 구간을 제거하는 기능")
    @Test
    void removeSection() {

        final String 신분당선 = "신분당선";
        final String 강남역 = "강남역";
        final String 시청역 = "시청역";
        final String 구로디지털단지역 = "구로디지털단지역";
        final String color = "red";
        final int distance = 10;

        //given
        ExtractableResponse<Response> 강남역_응답 = StationAcceptanceTest.지하철역_생성_요청_응답(강남역);
        long 강남역_아이디 = 강남역_응답.jsonPath().getLong("id");

        ExtractableResponse<Response> 시청역_응답 = StationAcceptanceTest.지하철역_생성_요청_응답(시청역);
        long 시청역_아이디 = 시청역_응답.jsonPath().getLong("id");

        ExtractableResponse<Response> 구로디지털단지역_응답 = StationAcceptanceTest.지하철역_생성_요청_응답(구로디지털단지역);
        long 구로디지털단지역_아이디 = 구로디지털단지역_응답.jsonPath().getLong("id");

        ExtractableResponse<Response> 신분당선_응답 = 지하철노선_생성_요청_응답(신분당선, 강남역_아이디, 시청역_아이디, color, distance);
        long 신분당선_아이디 = 신분당선_응답.jsonPath().getLong("id");

        final String url = "/lines/" + 신분당선_아이디 + "/sections";
        Map<String, Object> params = new HashMap<>();
        params.put("downStationId", 구로디지털단지역_아이디); // 구로디지털단지역
        params.put("upStationId", 시청역_아이디); // 시청역
        params.put("distance", 5);

        ExtractableResponse<Response> 구간등록_응답 = post(url, params);
        assertThat(구간등록_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        //when
        ExtractableResponse<Response> 제거_응답 = delete("/lines/" + 신분당선_아이디 + "/sections?stationId=" + 구로디지털단지역_아이디);

        //then
        assertThat(제거_응답.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    /**
     * given : 지하철역을 생성한다.
     * given : 지하철 노선을 생성한다.
     * when : 지하철 구간을 제거한다.
     * then : 예외가 발생한다.
     */
    @DisplayName("지하철 노선에 존재하는 구간이 1개일경우 삭제할 수 없다.")
    @Test
    void notRemoveSectionIfSectionOnce() {

        final String 신분당선 = "신분당선";
        final String 강남역 = "강남역";
        final String 시청역 = "시청역";
        final String 구로디지털단지역 = "구로디지털단지역";
        final String color = "red";
        final int distance = 10;

        //given
        ExtractableResponse<Response> 강남역_응답 = StationAcceptanceTest.지하철역_생성_요청_응답(강남역);
        long 강남역_아이디 = 강남역_응답.jsonPath().getLong("id");

        ExtractableResponse<Response> 시청역_응답 = StationAcceptanceTest.지하철역_생성_요청_응답(시청역);
        long 시청역_아이디 = 시청역_응답.jsonPath().getLong("id");

        ExtractableResponse<Response> 구로디지털단지역_응답 = StationAcceptanceTest.지하철역_생성_요청_응답(구로디지털단지역);
        long 구로디지털단지역_아이디 = 구로디지털단지역_응답.jsonPath().getLong("id");

        ExtractableResponse<Response> 신분당선_응답 = 지하철노선_생성_요청_응답(신분당선, 강남역_아이디, 시청역_아이디, color, distance);
        long 신분당선_아이디 = 신분당선_응답.jsonPath().getLong("id");

        //when
        ExtractableResponse<Response> 제거_응답 = delete("/lines/" + 신분당선_아이디 + "/sections?stationId=" + 시청역_아이디);

        //then
        assertThat(제거_응답.statusCode()).isEqualTo(HttpStatus.OK.value());
    }


}
