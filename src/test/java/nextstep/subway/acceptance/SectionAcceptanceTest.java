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

        ExtractableResponse<Response> newDownStationResponse = 지하철_역_생성("대림역");
        long newDownStationId = newDownStationResponse.jsonPath().getLong("id");

        ExtractableResponse<Response> extract = 새로운_구간_등록(lineId, newDownStationId);

        long actualLineId = extract.jsonPath().getLong("lineId");
        String upStationName = extract.jsonPath().getString("stations[0].name");
        String downStationName = extract.jsonPath().getString("stations[1].name");

        assertAll(
            () -> assertThat(actualLineId).isEqualTo(lineId),
            () -> assertThat(upStationName).isEqualTo("양재역"),
            () -> assertThat(downStationName).isEqualTo("대림역")
        );
    }

    private ExtractableResponse<Response> 새로운_구간_등록(long lineId,
        long newDownStationId) {
        Map<String, Object> param = Map.of(
            "downStationId", newDownStationId,
            "upStationId", 2,
            "distance", 10
        );

        ExtractableResponse<Response> extract = RestAssured.given().log().all()
            .body(param).contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/lines/" + lineId + "/sections")
            .then().log().all()
            .extract();
        return extract;
    }

}
