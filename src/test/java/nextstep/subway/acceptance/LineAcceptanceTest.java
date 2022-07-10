package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.common.AcceptanceTest;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.LineUpdateRequest;
import nextstep.subway.applicaion.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;

import static nextstep.subway.acceptance.common.RestAssuredTemplate.*;
import static nextstep.subway.acceptance.StationAcceptanceTest.역을_만들다;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {

    private Long 모란역;
    private Long 암사역;

    @BeforeEach
    void init() {
        모란역 = 역을_만들다("모란역").as(StationResponse.class).getId();
        암사역 = 역을_만들다("암사역").as(StationResponse.class).getId();
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // when
        var _8호선 = 노선을_만들다("8호선", "bg-pink-500", 모란역, 암사역, 17L).as(LineResponse.class);

        // then
        var 노선_목록 = 노선_목록을_조회한다().jsonPath().getList(".", LineResponse.class);
        assertThat(노선_목록).containsOnlyOnce(_8호선);
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철 노선을 목록을 조회한다.")
    @Test
    void getLines() {
        // given
        var _8호선 = 노선을_만들다("8호선", "bg-pink-500", 모란역, 암사역, 17L).as(LineResponse.class);

        var 까치산역 = 역을_만들다("까치산역").as(StationResponse.class).getId();
        var 신설동역 = 역을_만들다("신설동역").as(StationResponse.class).getId();
        var _2호선 = 노선을_만들다("2호선", "bg-lime-400", 까치산역, 신설동역, 23L).as(LineResponse.class);

        // when
        var 노선_목록 = 노선_목록을_조회한다().jsonPath().getList(".", LineResponse.class);

        // then
        assertThat(노선_목록).containsExactly(_8호선, _2호선);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLine() {
        // given
        var _8호선 = 노선을_만들다("8호선", "bg-pink-500", 모란역, 암사역, 17L).as(LineResponse.class);

        // when
        var 조회한_8호선 = 노선을_조회한다(_8호선.getId()).as(LineResponse.class);

        // then
        assertAll(() -> {
            assertThat(조회한_8호선.getName()).isEqualTo("8호선");
            assertThat(조회한_8호선.getColor()).isEqualTo("bg-pink-500");
            assertThat(조회한_8호선.getStationResponses()).containsExactly(
                    new StationResponse(1L, "모란역"),
                    new StationResponse(2L, "암사역")
            );
        });
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        var _8호선 = 노선을_만들다("8호선", "bg-pink-500", 모란역, 암사역, 17L).as(LineResponse.class);

        // when
        노선_목록을_수정한다(_8호선.getId(), "2호선", "bg-lime-300");

        // when
        var _2호선 = 노선을_조회한다(_8호선.getId()).as(LineResponse.class);
        assertAll(() -> {
            assertThat(_2호선.getName()).isEqualTo("2호선");
            assertThat(_2호선.getColor()).isEqualTo("bg-lime-300");
            assertThat(_2호선.getStationResponses()).containsExactly(
                    new StationResponse(1L, "모란역"),
                    new StationResponse(2L, "암사역")
            );
        });
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("지하철 노선을 삭제한다.")
    @Test
    void deleteLine() {
        // given
        var _8호선 = 노선을_만들다("8호선", "bg-pink-500", 모란역, 암사역, 17L).as(LineResponse.class);

        // when && then
        노선을_삭제한다(_8호선.getId());
    }

    public static ExtractableResponse<Response> 노선을_만들다(String name, String color, Long upStationId, Long downStationId, Long distance) {
        var lineRequest = new LineRequest(name, color, upStationId, downStationId, distance);
        var response = postRequestWithRequestBody("/lines", lineRequest);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        return response;
    }

    private ExtractableResponse<Response> 노선을_조회한다(Long id) {
        var response = getRequestWithParameter("/lines/{id}", id);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        return response;
    }

    private ExtractableResponse<Response> 노선_목록을_조회한다() {
        var response = getRequest("/lines");
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        return response;
    }

    private void 노선_목록을_수정한다(Long id, String name, String color) {
        var lineUpdateRequest = new LineUpdateRequest(name, color);
        var response = putRequestWithParameterAndRequestBody("/lines/{id}", id, lineUpdateRequest);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 노선을_삭제한다(Long id) {
        var response = deleteRequestWithParameter("/lines/{id}", id);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
