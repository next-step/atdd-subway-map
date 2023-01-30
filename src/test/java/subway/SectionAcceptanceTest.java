package subway;

import static org.assertj.core.api.Assertions.*;
import static subway.LineAcceptanceTest.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

@DisplayName("지하철 구간 관련 기능")
class SectionAcceptanceTest extends AcceptanceTest {

    private static final String URL_SECTION = "/lines/{id}/sections";
    private static final String 송파역 = "송파역";
    private static final String 가락시장역 = "가락시장역";
    private static final String 문정역 = "문정역";

    private Long id_송파역;
    private Long id_가락시장역;
    private Long id_문정역;
    private Long id_8호선;

    @BeforeEach
    void setup() {
        super.setup();

        id_송파역 = 지하철_역_생성(송파역);
        id_가락시장역 = 지하철_역_생성(가락시장역);
        id_문정역 = 지하철_역_생성(문정역);

        id_8호선 = 지하철_노선_생성("8호선", "pink", id_송파역, id_가락시장역).jsonPath().getLong("id");
    }

    /**
     * When 지하철 노선에 새로운 구간을 추가하면
     * Then 지하철 노선 조회 시 추가된 구간을 찾을 수 있다
     */
    @DisplayName("지하철 노선에 구간을 추가한다.")
    @Test
    void addSection() {
        // when
        지하철_노선에_구간_추가(id_8호선, id_가락시장역, id_문정역, 10);

        // then
        ExtractableResponse<Response> response = 지하철_노선_조회(id_8호선);

        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(id_송파역, id_가락시장역, id_문정역);
    }

    /**
     * Given 지하철 노선에 새로운 구간을 추가하고
     * When 지하철 노선의 마지막 구간을 제거하면
     * Then 지하철 노선에서 해당 구간이 제거된다
     */
    @DisplayName("지하철 노선의 구간을 제거한다.")
    @Test
    void deleteSection() {
        // given
        지하철_노선에_구간_추가(id_8호선, id_가락시장역, id_문정역, 10);

        // when
        지하철_노선에_구간_제거(id_8호선, id_문정역);

        // then
        ExtractableResponse<Response> response = 지하철_노선_조회(id_8호선);

        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(id_송파역, id_가락시장역);
    }

    private static ExtractableResponse<Response> 지하철_노선에_구간_추가(Long lineId, Long upStationId, Long downStationId, long distance) {
        Map<String, Object> sectionParam = new HashMap<>();
        sectionParam.put("upStationId", upStationId);
        sectionParam.put("downStationId", downStationId);
        sectionParam.put("distance", distance);

        return RestAssured
            .given()
                .log().all()
                .body(sectionParam)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
                .post(URL_SECTION, lineId)
            .then()
                .log().all()
            .extract();
    }

    private static ExtractableResponse<Response> 지하철_노선에_구간_제거(Long lineId, Long stationId) {
        return RestAssured
            .given()
                .log().all()
            .when()
                .delete(URL_SECTION + "?stationId={stationId}", lineId, stationId)
            .then()
                .log().all()
            .extract();
    }
}
