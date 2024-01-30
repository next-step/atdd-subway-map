package subway.section;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import subway.line.LineResponse;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Sql("classpath:db/teardown.sql")
@DisplayName("구간 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SectionAcceptanceTest {

    /**
     * When 노선이 생성되어 있다.
     *      노선의 하행종점역이 아닌 상행역의 구간을 등록한다.
     * Then 예외가 발생한다.
     */
    @DisplayName("노선에 구간을 등록 할 때, 구간의 상행역이 노선의 하행좀정역이 아니면 오류가 발생한다.")
    @Test
    public void 새로운_구간의_상행역이_노선의_하행종점역이_아닐_떄() {
        final Long lineId = 노선이_생성되어_있다("신분당선", "bg-red-600", 1L, 2L);

        final ExtractableResponse<Response> response = 구간을_등록한다(lineId, 3L, 2L, 10);

        예외가_발생한다(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * When 노선이 생성되어 있다.
     *      노선에 이미 등록되어 있는 역을 등록한다.
     * Then 예외가 발생한다.
     */
    @DisplayName("노선에 구간을 등록 할 때, 노선에 이미 등록되어 있는 역을 등록하면 오류가 발생한다.")
    @Test
    public void 이미_등록되어_있는_지하철역_일_때() {
        final Long lineId = 노선이_생성되어_있다("신분당선", "bg-red-600", 1L, 2L);

        final ExtractableResponse<Response> response = 구간을_등록한다(lineId, 2L, 1L, 10);

        예외가_발생한다(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * When 노선이 생성되어 있다.
     *      구간을 등록한다.
     * Then 정상 응답 처리된다.
     */
    @DisplayName("노선에 구간을 등록한다.")
    @Test
    public void 구간등록_정상처리() {
        final Long lineId = 노선이_생성되어_있다("신분당선", "bg-red-600", 1L, 2L);

        final ExtractableResponse<Response> response = 구간을_등록한다(lineId, 2L, 3L, 10);

        구간이_정상_등록한다(response, HttpStatus.CREATED);
    }

    /**
     * When 노선이 생성되어 있다.
     *      구간이 등록되어 있다.
     *      마지막 구간이 아닌 구간을 제거한다.
     * Then 예외가 발생한다.
     */
    @DisplayName("노선의 구간을 제거할 때 마지막구간이 아니면 오류가 발생한다.")
    @Test
    public void 구간제거_마지막구간이_아닐때() {
        final Long lineId = 노선이_생성되어_있다("신분당선", "bg-red-600", 1L, 2L);
        구간을_등록한다(lineId, 2L, 3L, 10);
    }

    private void 구간이_정상_등록한다(final ExtractableResponse<Response> response, final HttpStatus httpStatus) {
        final SectionResponse sectionResponse = response.as(SectionResponse.class);
        assertThat(response.statusCode()).isEqualTo(httpStatus.value());
        assertThat(sectionResponse.getDistance()).isEqualTo(10);
    }

    private void 예외가_발생한다(final ExtractableResponse<Response> response, HttpStatus httpStatus) {
        assertThat(response.statusCode()).isEqualTo(httpStatus.value());
    }

    private ExtractableResponse<Response> 구간이_등록되어_있다(final Long lineId, final Long upStationId,
                                                   final Long downStationId, final int distance) {
        final Map<String, String> params = registerSectionRequestPixture(upStationId, downStationId, distance);
        final ExtractableResponse<Response> response = apiRegisteSection(lineId, params);
        return response;
    }
    private ExtractableResponse<Response> 구간을_등록한다(final Long lineId, final Long upStationId,
                                                                      final Long downStationId, final int distance) {
        final Map<String, String> params = registerSectionRequestPixture(upStationId, downStationId, distance);
        final ExtractableResponse<Response> response = apiRegisteSection(lineId, params);
        return response;
    }

    private ExtractableResponse<Response> apiRegisteSection(final Long lineId, final Map<String, String> params) {
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines/" + lineId + "/sections")
                .then().log().all()
                .extract();
        return response;
    }

    private Long 노선이_생성되어_있다(final String name, final String color, final Long upStationId, final Long downStationId) {
        Map<String, String> params1 = createLineRequestPixture(name, color, upStationId, downStationId);
        final LineResponse lineResponse = apiCreateLine(params1);
        return lineResponse.getId();
    }

    private LineResponse apiCreateLine(final Map<String, String> params) {
        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract().as(LineResponse.class);
    }

    private Map<String, String> createLineRequestPixture(final String name, final String color, final Long upStationId, final Long downStationId) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        params.put("upStationId", String.valueOf(upStationId));
        params.put("downStationId", String.valueOf(downStationId));
        return params;
    }

    private Map<String, String> registerSectionRequestPixture(final Long upStationId, final Long downStationId, final int distance) {
        Map<String, String> params = new HashMap<>();
        params.put("upStationId", String.valueOf(upStationId));
        params.put("downStationId", String.valueOf(downStationId));
        params.put("distance", String.valueOf(distance));
        return params;
    }
}
