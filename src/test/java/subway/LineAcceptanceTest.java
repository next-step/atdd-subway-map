package subway;


import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static helper.LineTestHelper.*;
import static helper.StationTestHelper.지하철역을_생성한다;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
@DisplayName("지하철노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class LineAcceptanceTest {

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선 생성")
    @Test
    void createLine() {
        // when
        지하철역을_생성한다("지하철역");
        지하철역을_생성한다("새로운지하철역");

        String lineId = 지하철노선을_생성한다("신분당선", "bg-red-600", 1L, 2L, 10L)
                .jsonPath()
                .get("id")
                .toString();

        // then
        List<String> lines = 지하철노선_목록을_조회한다();
        assertThat(lines).contains(lineId);
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철 노선 목록 조회")
    @Test
    void showLines() {
        // given
        지하철역을_생성한다("지하철역");
        지하철역을_생성한다("새로운지하철역");
        지하철역을_생성한다("또다른지하철역");

        지하철노선을_생성한다("신분당선", "bg-red-600", 1L, 2L, 10L);
        지하철노선을_생성한다("분당선", "bg-red-600", 1L, 3L, 10L);

        // when
        List<String> lines = 지하철노선_목록을_조회한다();

        // then
        assertThat(lines).hasSize(2);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void searchLine() {
        // given
        지하철역을_생성한다("지하철역");
        지하철역을_생성한다("새로운지하철역");

        String lineId = 지하철노선을_생성한다("신분당선", "bg-red-600", 1L, 2L, 10L)
                .jsonPath()
                .get("id")
                .toString();

        // when
        ExtractableResponse<Response> response = 지하철노선을_조회한다(lineId);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
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
        지하철역을_생성한다("지하철역");
        지하철역을_생성한다("새로운지하철역");

        String lineId = 지하철노선을_생성한다("신분당선", "bg-red-600", 1L, 2L, 10L)
                .jsonPath()
                .get("id")
                .toString();

        // when
        ExtractableResponse<Response> response = 지하철노선을_수정한다(lineId, "신분당선", "bg-red-600");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
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
        지하철역을_생성한다("지하철역");
        지하철역을_생성한다("새로운지하철역");

        String lineId = 지하철노선을_생성한다("신분당선", "bg-red-600", 1L, 2L, 10L)
                .jsonPath()
                .get("id")
                .toString();

        // when
        ExtractableResponse<Response> response = 지하철노선을_삭제한다(lineId);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
