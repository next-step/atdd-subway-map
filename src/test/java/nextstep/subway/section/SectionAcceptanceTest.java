package nextstep.subway.section;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.AcceptanceTest;
import org.assertj.core.api.AbstractLongAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.acceptance.ResponseParser.getIdFromResponse;
import static nextstep.subway.line.LineAcceptanceTest.*;
import static nextstep.subway.station.StationAcceptanceTest.createStationRequest;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("구간 관련 기능")
@AcceptanceTest
class SectionAcceptanceTest {

    @LocalServerPort
    int port;

    private Long 상행역;
    private Long 하행역;
    private Long 신분당선;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;

        상행역 = 역을생성함("상행역");
        하행역 = 역을생성함("하행역");
        신분당선 = 신분당선을생성함();
    }

    /**
     * Given 신규상행역이 기존 노선의 하행역이 아닌 새로운 구간을
     * When 지하철 노선에 등록하면
     * Then 에러가 발생하고 하행역이 변경되지 않는다.
     */

    @DisplayName("신규상행역이 기존 노선의 하행역이 아닌 새로운 구간은 등록에 실패한다.")
    @Test
    void createSectionFail_DisconnectedUpStationId() {
        // given
        final Long 신규상행역 = 역을생성함("신규상행역");
        final Long 신규하행역 = 역을생성함("신규하행역");

        // when
        final ExtractableResponse<Response> 등록결과 = 새로운구간을등록함(신규상행역, 신규하행역);

        // then
        신규구간등록에실패함(등록결과);
        하행역이변경되지않음(신규하행역);
    }


    /**
     * Given 신규하행역이 기존 노선에 등록되어 있는 새로운 구간을
     * When 지하철 노선에 등록하면
     * Then 에러가 발생하고 하행역이 변경되지 않는다.
     */

    @DisplayName("신규하행역이 기존 노선에 등록되어 있는 새로운 구간은 등록에 실패한다.")
    @Test
    void createSectionFail_AlreadyRegisteredUpStationId() {
        // given

        // when
        final ExtractableResponse<Response> 등록결과 = 새로운구간을등록함(하행역, 상행역);

        // then
        신규구간등록에실패함(등록결과);
        하행역이변경되지않음(상행역);
    }


    /**
     * Given 신규상행역이 기존 노선의 하행역인 새로운 구간을
     * When 지하철 노선에 등록하면
     * Then 지하철 노선의 하행역이 해당 신규상행역으로 변경되어야 한다.
     */

    @DisplayName("신규상행역이 기존 노선의 하행역인 새로운 구간은 등록에 성공한다.")
    @Test
    void createSectionSuccess() {
        // given
        final Long 신규상행역 = 역을생성함("신규상행역");

        // when
        final ExtractableResponse<Response> 등록결과 = 새로운구간을등록함(하행역, 신규상행역);

        // then
        신규구간등록에성공함(등록결과);
        하행역이변경됨(신규상행역);
    }

    /**
     * When 하행종점역이 아닌 경우에 구간을 삭제하면
     * Then 에러가 발생하고 하행역은 그대로 존재한다.
     는    */

    @DisplayName("하행종점역이 아닌 역은 삭제에 실패한다.")
    @Test
    void deleteSectionFail_NotDownStationId() {
        // given
        final Long 신규상행역 = 역을생성함("신규상행역");
        새로운구간을등록함(하행역, 신규상행역);

        // when
        final ExtractableResponse<Response> 삭제결과 = 구간을삭제함(하행역);

        // then
        구간삭제에실패함(삭제결과);
        하행역이변경되지않음(하행역);
    }


    /**
     * When 구간이 1개인 하행종점역을 삭제하면
     * Then 에러가 발생하고 하행역이 변경되지 않는다.
     */

    @DisplayName("구간이 1개인 하행종점역은 삭제에 실패한다.")
    @Test
    void deleteSectionFail_LastSection() {
        // given

        // when
        final ExtractableResponse<Response> 삭제결과 = 구간을삭제함(하행역);

        // then
        구간삭제에실패함(삭제결과);
        하행역이변경되지않음(상행역);
    }

    /**
     * When 구간이 1개가 아닌 하행종점역을 삭제하면
     * Then 지하철 노선 목록 조회 시에 하행종점역이 구간에 존재하지 않는다.
     */

    @DisplayName("구간이 1개가 아닌 하행종점역은 삭제에 성공한다.")
    @Test
    void deleteSectionSuccess() {
        // given
        final Long 신규상행역 = 역을생성함("신규상행역");
        새로운구간을등록함(하행역, 신규상행역);

        // when
        final ExtractableResponse<Response> 삭제결과 = 구간을삭제함(신규상행역);

        // then
        구간삭제에성공함(삭제결과);
        하행역이변경됨(하행역);
    }

    private Long 신분당선을생성함() {
        return getIdFromResponse(createLineRequest(createLineParams("신분당선", 상행역, 하행역)));
    }

    private Long 역을생성함(final String stationName) {
        return getIdFromResponse(createStationRequest(stationName));
    }

    private ExtractableResponse<Response> 새로운구간을등록함(final Long 신규상행역, final Long 신규하행역) {
        return RestAssured
                .given().log().all()
                .body(createSectionParams(신규상행역, 신규하행역))
                .contentType(ContentType.JSON)
                .when().post("/lines/{id}/sections", 신분당선)
                .then().log().all()
                .extract();
    }

    private void 신규구간등록에실패함(final ExtractableResponse<Response> 등록결과) {
        assertThat(등록결과.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private void 신규구간등록에성공함(final ExtractableResponse<Response> 등록결과) {
        assertThat(등록결과.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private ExtractableResponse<Response> 구간을삭제함(final Long 하행역) {
        return RestAssured
                .given().log().all()
                .param("stationId", 하행역)
                .when().delete("/lines/{id}/sections", 신분당선)
                .then().log().all()
                .extract();
    }

    private void 구간삭제에성공함(final ExtractableResponse<Response> 삭제결과) {
        assertThat(삭제결과.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private void 구간삭제에실패함(final ExtractableResponse<Response> 삭제결과) {
        assertThat(삭제결과.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private AbstractLongAssert<?> 하행역이변경되지않음(final Long 역) {
        return assertThat((Long) getLineRequest(신분당선).jsonPath().getLong("stations[1].id")).isNotEqualTo(역);
    }

    private AbstractLongAssert<?> 하행역이변경됨(final Long 역) {
        return assertThat((Long) getLineRequest(신분당선).jsonPath().getLong("stations[1].id")).isEqualTo(역);
    }

    private Map<String, Object> createSectionParams(final Long upStationId, final Long downStationId) {
        final Map<String, Object> params = new HashMap<>();
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", 20);
        return params;
    }

}
