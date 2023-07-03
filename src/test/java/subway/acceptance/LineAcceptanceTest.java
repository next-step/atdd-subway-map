package subway.acceptance;

import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.acceptance.LineRequestFixture.*;
import static subway.acceptance.StationRequestFixture.지하철_역_생성;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {

    /**
     * Given 2개의 지하철 역을 생성하고
     * When (해당 지하철 역을 포함하는) 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선을 생성 한다.")
    @Test
    void createLine() {
        // given
        Long upStationId = 지하철_역_생성("강남역").jsonPath().getLong("id");
        Long downStationId = 지하철_역_생성("양재역").jsonPath().getLong("id");

        // when
        ExtractableResponse<Response> response = 지하철_노선_생성("신분당선", "bg-red-600", upStationId, downStationId, 10L);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        List<String> lineNames = 지하철_노선_목록_조회().jsonPath().getList("name", String.class);
        assertThat(lineNames).containsAnyOf("신분당선");
    }



    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철 노선을 목록을 조회 한다.")
    @Test
    void getLines() {
        // given
        Long station1 = 지하철_역_생성("강남역").jsonPath().getLong("id");
        Long station2 = 지하철_역_생성("양재역").jsonPath().getLong("id");
        Long station3 = 지하철_역_생성("역삼역").jsonPath().getLong("id");
        Long station4 = 지하철_역_생성("선릉역").jsonPath().getLong("id");

        지하철_노선_생성("신분당선", "bg-red-600", station1, station2, 10L);
        지하철_노선_생성("2호선", "bg-green-600", station3, station4, 10L);

        List<String> lineNames = 지하철_노선_목록_조회()
                .jsonPath()
                .getList("name", String.class);
        assertThat(lineNames).containsAnyOf("신분당선", "2호선");
    }



    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철 노선을 조회 한다.")
    @Test
    void getLine() {
        // given
        Long upStationId = 지하철_역_생성("강남역").jsonPath().getLong("id");
        Long downStationId = 지하철_역_생성("양재역").jsonPath().getLong("id");

        Long id = 지하철_노선_생성("신분당선", "bg-red-600", upStationId, downStationId, 10L).jsonPath().getLong("id");

        // when
        ExtractableResponse<Response> response = 지하철_노선_조회(id);

        assertThat(response.jsonPath().getLong("id")).isEqualTo(id);
        assertThat(response.jsonPath().getString("name")).isEqualTo("신분당선");
        assertThat(response.jsonPath().getString("color")).isEqualTo("bg-red-600");
        assertThat(response.jsonPath().getList("stations.id", Long.class))
                .containsAnyOf(upStationId, downStationId);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("지하철 노선을 수정 한다.")
    @Test
    void updateLine() {
        // given
        Long upStationId = 지하철_역_생성("강남역").jsonPath().getLong("id");
        Long downStationId = 지하철_역_생성("역삼역").jsonPath().getLong("id");

        Long id = 지하철_노선_생성("신분당선", "bg-red-600", upStationId, downStationId, 10L).jsonPath().getLong("id");

        // when
        Map<String, String> params = new HashMap<>();
        params.put("name", "2호선");
        params.put("color", "bg-green-600");

        ExtractableResponse<Response> response = 지하철_노선_수정(id, params);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        // then
        JsonPath lineResponse = 지하철_노선_조회(id).jsonPath();

        assertThat(lineResponse.getString("name")).isEqualTo("2호선");
        assertThat(lineResponse.getString("color")).isEqualTo("bg-green-600");
    }



    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("지하철 노선을 삭제 한다.")
    @Test
    void deleteLIne() {
        // given
        Long upStationId = 지하철_역_생성("강남역").jsonPath().getLong("id");
        Long downStationId = 지하철_역_생성("양재역").jsonPath().getLong("id");

        Long id = 지하철_노선_생성("신분당선", "bg-red-600", upStationId, downStationId, 10L).jsonPath().getLong("id");

        // when
        ExtractableResponse<Response> response = 지하철_노선_삭제(id);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        // then
        List<String> lineNames = 지하철_노선_목록_조회().jsonPath().getList("name", String.class);

        assertThat(lineNames).doesNotContain("신분당선");
    }
}
