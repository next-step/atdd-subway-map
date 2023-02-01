package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.LineAcceptanceTest.*;

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

        id_8호선 = 지하철_노선_생성("8호선", "pink", id_송파역, id_가락시장역, 10).jsonPath().getLong("id");
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
     * When 지하철 노선에 새로운 구간 추가 시, 하행 종점역이 아닌 역을 상행역으로 전달하면
     * Then 새로운 구간이 등록되지 않는다
     */
    @DisplayName("새로운 지하철 구간 추가 시, 구간의 상행역은 하행 종점역이어야 한다.")
    @Test
    void invalidUpStation() {        
        // when
        ExtractableResponse<Response> response = 지하철_노선에_구간_추가(id_8호선, id_송파역, id_문정역, 10);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    /**
     * When 지하철 노선에 새로운 구간 추가 시, 이미 노선에 등록된 역을 하행역으로 전달하면
     * Then 새로운 구간이 등록되지 않는다
     */
    @DisplayName("새로운 지하철 구간 추가 시, 구간의 하행역은 노선에 등록되지 않은 역이어야 한다.")
    @Test
    void invalidDownStation() {
        // when
        ExtractableResponse<Response> response = 지하철_노선에_구간_추가(id_8호선, id_가락시장역, id_송파역, 10);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
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

    /**
     * When 지하철 노선의 구간 제거 요청 시, 현재 노선에 등록된 구간이 하나라면
     * Then 지하철 구간이 제거되지 않는다.
     */
    @DisplayName("현재 지하철 노선에 등록된 구간이 하나라면 지하철 구간을 제거할 수 없다.")
    @Test
    void singleSection() {
        // when
        ExtractableResponse<Response> response = 지하철_노선에_구간_제거(id_8호선, id_가락시장역);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    /**
     * Given 지하철 노선에 새로운 구간을 추가하고
     * When 지하철 노선의 구간 제거 요청 시, 전달된 지하철역이 하행 종점역이 아닌 경우
     * Then 지하철 구간이 제거되지 않는다.
     */
    @DisplayName("지하철 노선에 등록된 마지막 구간만 제거할 수 있다.")
    @Test
    void invalidLastStation() {
        // given
        지하철_노선에_구간_추가(id_8호선, id_가락시장역, id_문정역, 10);

        // when
        ExtractableResponse<Response> response = 지하철_노선에_구간_제거(id_8호선, id_송파역);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @DisplayName("지하철 구간의 길이는 1 이상이어야 한다.")
    @ValueSource(ints = {-1, 0})
    @ParameterizedTest
    void invalidDistance(long distance) {
        // when
        ExtractableResponse<Response> response = 지하철_노선에_구간_추가(id_8호선, id_가락시장역, id_문정역, distance);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
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
                .pathParam("id", lineId)
                .queryParam("stationId", stationId)
            .when()
                .delete(URL_SECTION)
            .then()
                .log().all()
            .extract();
    }
}
