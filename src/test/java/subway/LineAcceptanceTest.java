package subway;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import subway.dto.LineResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.AcceptanceTestHelper.*;

@DisplayName("지하철노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext
@Sql("/truncateLine.sql")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LineAcceptanceTest {
    private Long stationId1;
    private Long stationId2;
    private Long stationId3;

    @BeforeAll
    void beforeAll() {
        stationId1 = 지하철역_등록("지하철역").body().jsonPath().getLong("id");
        stationId2 = 지하철역_등록("새로운지하철역").body().jsonPath().getLong("id");
        stationId3 = 지하철역_등록("또다른지하철역").body().jsonPath().getLong("id");
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철노선 생성")
    @Test
    void createLine() {
        // given
        final String name = "신분당선";

        // when
        final ExtractableResponse<Response> response = 지하철노선_생성(name, "bg-red-600", stationId1, stationId2, 10);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        final List<String> lineNames = 지하철노선_목록_조회().jsonPath().getList("name", String.class);
        assertThat(lineNames).containsAnyOf(name);
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철노선 목록 조회")
    @Test
    void getLineList() {
        // given
        final String name1 = "신분당선";
        final String name2 = "분당선";
        지하철노선_생성(name1, "bg-red-600", stationId1, stationId2, 10);
        지하철노선_생성(name2, "bg-green-600", stationId1, stationId3, 10);

        // when
        final ExtractableResponse<Response> response = 지하철노선_목록_조회();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        final List<String> lineNames = 지하철노선_목록_조회().jsonPath().getList("name", String.class);
        assertThat(lineNames).containsAnyOf(name1, name2);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철노선 조회")
    @Test
    void getLine() {
        // given
        final String name = "신분당선";
        final Long id = 지하철노선_생성(name, "bg-red-600", stationId1, stationId2, 10).jsonPath().getLong("id");

        // when
        final ExtractableResponse<Response> response = 지하철노선_조회(id);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.body().jsonPath().getString("name")).isEqualTo(name);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("지하철노선 수정")
    @Test
    void updateLine() {
        // given
        final Long id = 지하철노선_생성("신분당선", "bg-red-600", stationId1, stationId2, 10).jsonPath().getLong("id");
        final String newName = "다른분당선";
        final String newColor = "bg-red-600";

        // when
        final ExtractableResponse<Response> response = 지하철노선_수정(id, newName, newColor);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        final LineResponse lineResponse = 지하철노선_조회(id).jsonPath().getObject(".", LineResponse.class);
        assertThat(lineResponse.getName()).isEqualTo(newName);
        assertThat(lineResponse.getColor()).isEqualTo(newColor);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("지하철노선 삭제")
    @Test
    void removeLine() {
        // given
        final String name = "신분당선";
        final Long id = 지하철노선_생성(name, "bg-red-600", stationId1, stationId2, 10).jsonPath().getLong("id");

        // when
        final ExtractableResponse<Response> response = 지하철노선_삭제(id);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        final List<String> lineNames = 지하철노선_목록_조회().jsonPath().getList("name", String.class);
        assertThat(lineNames).doesNotContain(name);
    }
}
