package nextstep.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class SectionAcceptanceTest extends BaseAcceptance {

    @BeforeEach
    void setUp() {
        RestAssured.port = super.port;
        dataBaseCleaner.afterPropertiesSet();
        dataBaseCleaner.tableClear();
    }

    /*
    해당 노선의 구간을 등록
    등록하는 upStationId는 기존의 하행역과 같아야한다.
    등록하려는 하행역은 해당하는 노선에 없어야 한다.
    새로운 구간 등록시 위 조건이 아닌경우 에러처리
     */
    @Test
    @DisplayName("지하철 노선 구간 등록")
    void createSection() {
        //given
        long lineId = 지하철_노선_생성("강남역", "양재역", "신분당선", "red");

        long newDownStationId = 지하철_역_생성_후_id값_반환("대림역");

        ExtractableResponse<Response> extract = 새로운_구간_등록(lineId, newDownStationId, 2);

        long actualLineId = extract.jsonPath().getLong("lineId");
        String upStationName = extract.jsonPath().getString("stations[0].name");
        String downStationName = extract.jsonPath().getString("stations[1].name");

        assertAll(
            () -> assertThat(actualLineId).isEqualTo(lineId),
            () -> assertThat(upStationName).isEqualTo("양재역"),
            () -> assertThat(downStationName).isEqualTo("대림역")
        );
    }

    private long 지하철_역_생성_후_id값_반환(final String stationName) {
        ExtractableResponse<Response> newDownStationResponse = 지하철_역_생성(stationName);
        return newDownStationResponse.jsonPath().getLong("id");
    }

    @Test
    @DisplayName("지하철 노선 구간 제거")
    void deleteSection() {
        long lineId = 지하철_노선_생성("강남역", "양재역", "신분당선", "red");

        long newDownStationId = 지하철_역_생성_후_id값_반환("판교역");
        long newDownStationId2 = 지하철_역_생성_후_id값_반환("광교역");

        ExtractableResponse<Response> 첫_구간_등록_응답 = 새로운_구간_등록(lineId, newDownStationId,
            2);

        long 첫_새로운_구간등록_후_id = 첫_구간_등록_응답.jsonPath().getLong("stations[1].id");

        ExtractableResponse<Response> 마지막_구간_등록_응답 = 새로운_구간_등록(lineId, newDownStationId2,
            첫_새로운_구간등록_후_id);

        long 하행_종점역_id = 마지막_구간_등록_응답.jsonPath().getLong("stations[1].id");

        ExtractableResponse<Response> removeResponse = 지하철_구간_삭제(lineId, 하행_종점역_id);

        assertThat(removeResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        findAllLines();
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
