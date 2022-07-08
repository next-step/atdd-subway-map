package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;

import static nextstep.subway.acceptance.RestAssuredTemplate.*;
import static nextstep.subway.acceptance.StationAcceptanceTest.역을_만들다;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // when
        Long 모란역 = 역을_만들다("모란역").as(StationResponse.class).getId();
        Long 암사역 = 역을_만들다("암사역").as(StationResponse.class).getId();
        LineResponse newLine = 노선을_만들다("8호선", "bg-pink-500", 모란역, 암사역, 17L).as(LineResponse.class);

        // then
        List<LineResponse> lineResponse = 노선_목록을_조회한다().jsonPath().getList(".", LineResponse.class);
        assertThat(lineResponse).containsOnlyOnce(newLine);
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
        Long 모란역 = 역을_만들다("모란역").as(StationResponse.class).getId();
        Long 암사역 = 역을_만들다("암사역").as(StationResponse.class).getId();
        LineResponse newLine1 = 노선을_만들다("8호선", "bg-pink-500", 모란역, 암사역, 17L).as(LineResponse.class);

        Long 까치산역 = 역을_만들다("까치산역").as(StationResponse.class).getId();
        Long 신설동역 = 역을_만들다("신설동역").as(StationResponse.class).getId();
        LineResponse newLine2 = 노선을_만들다("2호선", "bg-lime-400", 까치산역, 신설동역, 23L).as(LineResponse.class);

        // when
        List<LineResponse> lineResponse = 노선_목록을_조회한다().jsonPath().getList(".", LineResponse.class);

        // then
        assertThat(lineResponse).containsExactly(newLine1, newLine2);
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
        Long 모란역 = 역을_만들다("모란역").as(StationResponse.class).getId();
        Long 암사역 = 역을_만들다("암사역").as(StationResponse.class).getId();
        LineResponse newLine = 노선을_만들다("8호선", "bg-pink-500", 모란역, 암사역, 17L).as(LineResponse.class);

        // when
        LineResponse lineResponse = 노선을_조회한다(newLine.getId()).as(LineResponse.class);

        // then
        assertAll(() -> {
            assertThat(lineResponse.getName()).isEqualTo("8호선");
            assertThat(lineResponse.getColor()).isEqualTo("bg-pink-500");
            assertThat(lineResponse.getStationResponses()).containsExactly(
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

    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("지하철 노선을 삭제한다.")
    @Test
    void deleteLine() {

    }

    public static ExtractableResponse<Response> 노선을_만들다(String name, String color, Long upStationId, Long downStationId, Long distance) {
        LineRequest lineRequest = new LineRequest(name, color, upStationId, downStationId, distance);
        ExtractableResponse<Response> response = postRequestWithRequestBody("/lines", lineRequest);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        return response;
    }

    private ExtractableResponse<Response> 노선을_조회한다(Long id) {
        ExtractableResponse<Response> response = getRequestWithParameter("/lines/{id}", id);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        return response;
    }

    private ExtractableResponse<Response> 노선_목록을_조회한다() {
        ExtractableResponse<Response> response = getRequest("/lines");
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        return response;
    }
}
