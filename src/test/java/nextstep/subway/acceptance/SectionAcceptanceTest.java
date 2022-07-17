package nextstep.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

public class SectionAcceptanceTest extends BaseAcceptance {

    @BeforeEach
    void setUp() {
        RestAssured.port = super.port;
        dataBaseCleaner.afterPropertiesSet();
        dataBaseCleaner.tableClear();
    }

    @Test
    @DisplayName("지하철 노선 구간 등록")
    void createSection() {
        //given
        long lineId = 지하철_노선_생성("강남역", "양재역", "신분당선", "red");

        ExtractableResponse<Response> newDownStationResponse = 지하철_역_생성("대림역");
        long newDownStationId = newDownStationResponse.jsonPath().getLong("id");

        //when
        ExtractableResponse<Response> extract = 새로운_구간_등록(lineId, newDownStationId, 2);

        //then
        long actualLineId = extract.jsonPath().getLong("lineId");
        String upStationName = extract.jsonPath().getString("stations[0].name");
        String downStationName = extract.jsonPath().getString("stations[1].name");

        assertAll(
            () -> assertThat(actualLineId).isEqualTo(lineId),
            () -> assertThat(upStationName).isEqualTo("양재역"),
            () -> assertThat(downStationName).isEqualTo("대림역")
        );
    }

    @Test
    @DisplayName("지하철 노선 구간 제거")
    void deleteSection() {
        //given
        long lineId = 지하철_노선_생성("강남역", "양재역", "신분당선", "red");

        ExtractableResponse<Response> 마지막_구간_등록_응답 = 두개의_구간_등록(lineId);

        long 하행_종점역_id = 마지막_구간_등록_응답.jsonPath().getLong("stations[1].id");

        //when
        지하철_구간_삭제(lineId, 하행_종점역_id);

        ExtractableResponse<Response> getLinesResponse = 전체_노선_조회();

        //then
        final List<String> name = getLinesResponse.jsonPath().getList("name", String.class);
        final List<List> stations = getLinesResponse.jsonPath().getList("stations", List.class);
        assertAll(
            () -> assertThat(name).hasSize(1)
                .containsExactly("신분당선"),

            () -> assertThat(stations.get(0)).containsAnyOf(
                Map.of("id", 1, "name", "강남역"),
                Map.of("id", 2, "name", "양재역"),
                Map.of("id", 3, "name", "판교역")
            )
        );
    }

    private ExtractableResponse<Response> 두개의_구간_등록(final long lineId) {
        ExtractableResponse<Response> newDownStationResponse = 지하철_역_생성("판교역");
        long newDownStationId = newDownStationResponse.jsonPath().getLong("id");

        ExtractableResponse<Response> newDownStationResponse2 = 지하철_역_생성("광교역");
        long newDownStationId2 = newDownStationResponse2.jsonPath().getLong("id");

        ExtractableResponse<Response> 첫_구간_등록_응답 = 새로운_구간_등록(lineId, newDownStationId,
            2);

        long 첫_새로운_구간등록_후_id = 첫_구간_등록_응답.jsonPath().getLong("stations[1].id");

        return 새로운_구간_등록(lineId, newDownStationId2, 첫_새로운_구간등록_후_id);
    }

    private ExtractableResponse<Response> 지하철_구간_삭제(long lineId,
        long removeStationId) {
        return RestAssured.given().log().all()
            .param("stationId", removeStationId)
            .when().delete("/lines/" + lineId + "/sections")
            .then().log().all()
            .extract();
    }

    private ExtractableResponse<Response> 새로운_구간_등록(long lineId,
        long newDownStationId, final long upStationId) {
        Map<String, Object> param = Map.of(
            "downStationId", newDownStationId,
            "upStationId", upStationId,
            "distance", 10
        );

        return RestAssured.given().log().all()
            .body(param).contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/lines/" + lineId + "/sections")
            .then().log().all()
            .extract();
    }

}
