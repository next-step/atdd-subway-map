package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("구간 관련 기능")
public class SectionAcceptanceTest extends BaseAcceptanceTest {

    Long 상행역_ID;
    Long 하행역_ID;
    Long _2호선_ID;

    @BeforeEach
    void dataSetUp() {
        상행역_ID = 지하철역_생성("상행역");
        하행역_ID = 지하철역_생성("하행역");
        _2호선_ID = 노선_생성("2호선", "bg-green-600", 상행역_ID, 하행역_ID, 10);
    }

    /**
     * `Given`  지하철 역을 생성하고
     * `When`   구간(`상행역` : 하행 종점역이 아닌 역, `하행역` : 새로 생성한 역)을 등록하면
     * `Then`   400 에러 코드를 응답받고 등록한 구간을 찾을 수 없다.
     */
    @Test
    @DisplayName("구간 등록 예외 - 잘못된 상행역")
    void createSection_invalid() {
        // given
        Long 신규역_ID = 지하철역_생성("신규역");

        // when
        ExtractableResponse<Response> 구간_생성_요청_결과 =
                구간_등록_요청(_2호선_ID, 상행역_ID, 신규역_ID, 5, HttpStatus.BAD_REQUEST);

        // then
        List<Long> _2호선_지하철_목록 = 노선_지하철_목록_조회(_2호선_ID);
        assertThat(_2호선_지하철_목록).doesNotContain(신규역_ID);
    }

    /**
     * `Given` 지하철 역을 생성하고
     * `When` 구간(`상행역` : 하행 종점역, `하행역` : 노선에 등록되어 있는 역)을 등록하면
     * `Then` 400 에러 코드를 응답받고 등록한 구간을 찾을 수 없다.
     */
    @Test
    @DisplayName("구간 등록 예외 - 잘못된 하행역")
    void createSection_invalid_wrong_downStation() {
        // given
        Long 신규역_ID = 지하철역_생성("신규역");

        // when
        ExtractableResponse<Response> 구간_생성_요청_결과 =
                구간_등록_요청(_2호선_ID, 하행역_ID, 상행역_ID, 5, HttpStatus.BAD_REQUEST);

        // then
        List<Long> _2호선_지하철_목록 = 노선_지하철_목록_조회(_2호선_ID);
        assertThat(_2호선_지하철_목록).doesNotContain(신규역_ID);
    }

    /**
     * `Given` 지하철 역을 생성하고
     * `When` 구간(`상행역` : 하행 종점역, `하행역` : 새로 생성한 역)을 등록하면
     * `Then` 해당 지하철 노선을 조회 시 등록한 구간을 찾을 수 있다.
     */
    @Test
    @DisplayName("구간 등록")
    void createSection() {
        // given
        Long 신규역_ID = 지하철역_생성("신규역");

        // when
        구간_등록_요청(_2호선_ID, 하행역_ID, 신규역_ID, 5, HttpStatus.CREATED);

        // then
        List<Long> _2호선_지하철_목록 = 노선_지하철_목록_조회(_2호선_ID);
        assertThat(_2호선_지하철_목록).contains(신규역_ID);
    }

    private ExtractableResponse<Response> 구간_등록_요청(long id, long upStationId, long downStationId, int distance, HttpStatus httpStatus) {
        final Map<String, Object> params = new HashMap<>();
        params.put("downStationId", downStationId);
        params.put("upStationId", upStationId);
        params.put("distance", distance);

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines/{id}/sections", id)
                .then().log().all()
                .extract();
        assertThat(response.statusCode()).isEqualTo(httpStatus.value());
        return response;
    }

    private Long 지하철역_생성(String name) {
        return StationAcceptanceTest.지하철역_등록_요청(name)
                .jsonPath()
                .getLong("id");
    }

    private Long 노선_생성(String name, String color, Long upStationId, Long downStationId, int distance) {
        return LineAcceptanceTest.노선_생성_요청(name, color, upStationId, downStationId, distance)
                .jsonPath()
                .getLong("id");
    }

    private List<Long> 노선_지하철_목록_조회(Long id) {
        return LineAcceptanceTest.노선_조회_요청(id)
                .jsonPath()
                .getList("stations.id", Long.class);
    }
}
