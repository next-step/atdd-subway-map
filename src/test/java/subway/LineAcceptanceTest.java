package subway;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("지하철 노선 관련 기능")
class LineAcceptanceTest extends AcceptanceTest {

    /**
     * When 지하철 노선을 생성하면 <br>
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("노선 생성")
    @Test
    void 노선_생성() {
        // when
        final String 신분당선 = "신분당선";
        노선을_생성한다(신분당선, "bg-red-600", "강남역", "잠실역", 10);

        // then
        노선_목록에서_조회_가능하다(신분당선);
    }

    /**
     * Given 2개의 지하철 노선을 생성하고 <br>
     * When 지하철 노선 목록을 조회하면 <br>
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("노선 목록 조회")
    @Test
    void 노선_목록_조회() {
        // given
        final String 신분당선 = "신분당선";
        final String 이호선 = "2호선";
        노선을_생성한다(신분당선, "bg-red-600", "강남역", "잠실역", 10);
        노선을_생성한다(이호선, "green", "서울역", "명동역", 20);

        // when then
        노선_목록에서_조회_가능하다(신분당선, 이호선);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("노선 조회")
    @Test
    void 노선_조회() {
        // given
        final String 신분당선 = "신분당선";
        final String color = "bg-red-600";
        final long lineId = 노선을_생성한다(신분당선, color, "강남역", "잠실역", 10)
            .body().jsonPath().getLong("id");

        // when then
        노선을_확인한다(lineId, 신분당선, color);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("노선 수정")
    @Test
    void 노선_수정() {
        // given
        final long lineId = 노선을_생성한다("신분당선", "bg-red-600", "강남역", "잠실역", 10)
            .body().jsonPath().getLong("id");

        // when
        final String 다른분당선 = "다른분당선";
        final String green = "bg-green-600";
        노선을_수정한다(lineId, 다른분당선, green);

        // then
        노선을_확인한다(lineId, 다른분당선, green);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("노선 삭제")
    @Test
    void 노선_삭제() {
        // given
        final String 신분당선 = "신분당선";
        final long lineId = 노선을_생성한다(신분당선, "bg-red-600", "강남역", "잠실역", 10)
            .body().jsonPath().getLong("id");

        // when
        노선을_삭제한다(lineId);

        // then
        노선_목록에서_조회_불가능하다(신분당선);
    }

    private void 노선_목록에서_조회_가능하다(String... lineNames) {
        final List<String> createdLineNames = 노선_목록_이름들을_조회한다();

        assertThat(createdLineNames).contains(lineNames);
    }

    private void 노선_목록에서_조회_불가능하다(String... lineNames) {
        final List<String> createdLineNames = 노선_목록_이름들을_조회한다();

        assertThat(createdLineNames).doesNotContain(lineNames);
    }

    private List<String> 노선_목록_이름들을_조회한다() {
        final ExtractableResponse<Response> lineResponse = 노선_목록을_조회한다();
        return lineResponse.body().jsonPath().getList("name", String.class);
    }

    private void 노선을_확인한다(long lineId, String name, String color) {
        final ExtractableResponse<Response> lineResponse = 노선을_조회한다(lineId);
        final String lineName = lineResponse.body().jsonPath().get("name");
        final String lineColor = lineResponse.body().jsonPath().get("color");

        assertAll(
            () -> assertThat(lineName).isEqualTo(name),
            () -> assertThat(lineColor).isEqualTo(color)
        );
    }
}
