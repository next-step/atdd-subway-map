package nextstep.subway.acceptance.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.AcceptanceTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Map;

import static nextstep.subway.acceptance.TestSetupUtils.지하철_노선_생성;
import static nextstep.subway.acceptance.TestSetupUtils.지하철_역_생성;
import static org.assertj.core.api.Assertions.assertThat;

class SectionAcceptanceTest extends AcceptanceTest {

    /**
     * Given: 노선이 주어졌을 때
     * When: 새로 추가하려는 구간의 하행역이 노선에 이미 포함되어있으면
     * Then: 구간 등록에 실패한다
     */
    @Test
    void 구간_등록_하행역이_이미_등록되어있으면_예외_인수테스트() {
        // given
        long 교대역 = 지하철_역_생성("교대역");
        long 역삼역 = 지하철_역_생성("역삼역");
        long 신분당선 = 지하철_노선_생성("신분당선", "red", 교대역, 역삼역, 10);

        // when
        var 구간_등록 = 구간을_등록한다(신분당선, 역삼역, 교대역, 10);

        // then
        assertThat(구간_등록.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given: 노선이 주어졌을 때
     * When: 새로 추가하려는 구간의 상행역이 노선의 종점이 아니면
     * Then: 구간 등록에 실패한다
     */
    @Test
    void 구간_등록_상행역이_등록된_종점이_아니면_예외_인수테스트() {
        // given
        long 교대역 = 지하철_역_생성("교대역");
        long 역삼역 = 지하철_역_생성("역삼역");
        long 신분당선 = 지하철_노선_생성("신분당선", "red", 교대역, 역삼역, 10);

        // when
        long 강남역 = 지하철_역_생성("강남역");
        var 구간_등록 = 구간을_등록한다(신분당선, 교대역, 강남역, 10);

        // then
        assertThat(구간_등록.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given: 노선이 주어졌을 때
     * When: 새로 추가하려는 구간의 상행역이 노선의 종점이고 하행역이 노선에 포함되어있지 않으면
     * Then: 구간 등록에 성공한다
     */
    @Test
    void 구간_등록_성공_인수테스트() {
        // given
        long 교대역 = 지하철_역_생성("교대역");
        long 역삼역 = 지하철_역_생성("역삼역");
        long 신분당선 = 지하철_노선_생성("신분당선", "red", 교대역, 역삼역, 10);

        // when
        long 강남역 = 지하철_역_생성("강남역");
        var 구간_등록 = 구간을_등록한다(신분당선, 역삼역, 강남역, 10);

        // then
        assertThat(구간_등록.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    /**
     * Given: 노선이 주어졌을 때
     * When: 삭제하려는 구간이 노선의 마지막 역이 아니면
     * Then: 구간 삭제에 실패한다
     */
    @Test
    void 구간_삭제_마지막_역이_아니면_예외_인수테스트() {
        // given
        long 교대역 = 지하철_역_생성("교대역");
        long 역삼역 = 지하철_역_생성("역삼역");
        long 강남역 = 지하철_역_생성("강남역");

        long 신분당선 = 지하철_노선_생성("신분당선", "red", 교대역, 역삼역, 10);
        구간을_등록한다(신분당선, 역삼역, 강남역, 10);

        // when
        var 구간_삭제 = 구간을_삭제한다(신분당선, 교대역);

        // then
        assertThat(구간_삭제.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given: 노선이 주어졌을 때
     * When: 삭제하려는 구간이 노선의 유일한 구간이면
     * Then: 구간 삭제에 실패한다
     */
    @Test
    void 구간_삭제_구간이_유일한_구간이면_예외_인수테스트() {
        // given
        long 교대역 = 지하철_역_생성("교대역");
        long 역삼역 = 지하철_역_생성("역삼역");

        long 신분당선 = 지하철_노선_생성("신분당선", "red", 교대역, 역삼역, 10);

        // when
        var 구간_삭제 = 구간을_삭제한다(신분당선, 역삼역);

        // then
        assertThat(구간_삭제.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given: 노선이 주어졌을 때
     * When: 삭제하려는 구간이 노선의 종점이면서 노선에 구간이 둘 이상 존재하면
     * Then: 구간 삭제에 성공한다
     */
    @Test
    void 구간_삭제_성공_인수테스트() {
        // given
        long 교대역 = 지하철_역_생성("교대역");
        long 역삼역 = 지하철_역_생성("역삼역");
        long 강남역 = 지하철_역_생성("강남역");

        long 신분당선 = 지하철_노선_생성("신분당선", "red", 교대역, 역삼역, 10);
        구간을_등록한다(신분당선, 역삼역, 강남역, 10);

        // when
        var 구간_삭제 = 구간을_삭제한다(신분당선, 강남역);

        // then
        assertThat(구간_삭제.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private ExtractableResponse<Response> 구간을_등록한다(long lineId, long upStationId, long downStationId, int distance) {
        return RestAssured.given().log().all()
                .body(Map.of("upStationId", upStationId,
                        "downStationId", downStationId,
                        "distance", distance))
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines/" + lineId + "/sections")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 구간을_삭제한다(long lineId, long stationId) {
        return RestAssured.given().log().all()
                .param("stationId", String.valueOf(stationId))
                .when().delete("/lines/" + lineId + "/sections")
                .then().log().all()
                .extract();
    }
}
