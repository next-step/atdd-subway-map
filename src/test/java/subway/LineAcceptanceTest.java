package subway;


import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import subway.line.LineResponse;

import javax.transaction.Transactional;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class LineAcceptanceTest {

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @ParameterizedTest
    @ValueSource(strings = {"신분당선", "분당선"})
    @DisplayName("지하철 노선을 생성하면 생성된 지하철 노선이 조회된다.")
    void createLine(String name) {
        // when
        TestHelper.createLine(name);

        // then
        assertThat(TestHelper.selectAllLines())
                .extracting(LineResponse::getName)
                .contains(name);
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다
     */
    @Test
    @DisplayName("지하철노선 목록을 조회하면 생성한 지하철 노선이 조회된다.")
    void selectLines() {
        // given
        TestHelper.createLine("신분당선");
        TestHelper.createLine("분당선");

        // when
        List<LineResponse> lineResponses = TestHelper.selectAllLines();

        // then
        assertThat(lineResponses).hasSize(2)
                .map(LineResponse::getName)
                .containsExactly("신분당선", "분당선");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @Test
    @DisplayName("지하철 노선을 조회하면 생성한 지하철 노선의 정보를 응답받을 수 있다.")
    void selectLine() {
        // given
        LineResponse line = TestHelper.createLine("신분당선");

        // when
        LineResponse lineResponse = TestHelper.selectLine(line.getId());

        // then
        assertThat(lineResponse.getName()).isEqualTo("신분당선");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @Test
    @DisplayName("지하철 노선을 수정하면 해당 지하철 노선 정보는 수정되어 조회된다.")
    void updateLine() {
        // given
        LineResponse line = TestHelper.createLine("신분당선");

        // when
        RestAssured
            .given()
                .body(Map.of("name", "1호선", "color", "bg-red-600"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
                .put("/lines/{id}", line.getId())
            .then()
                .statusCode(HttpStatus.OK.value())
                .extract();

        // then
        LineResponse lineResponse = TestHelper.selectLine(line.getId());

        assertThat(lineResponse)
                .extracting(LineResponse::getName)
                    .isEqualTo("1호선");

        assertThat(lineResponse)
                .extracting(LineResponse::getColor)
                .isEqualTo("bg-red-600");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @Test
    @DisplayName("지하철 노선을 삭제하면 해당 지하철 노선 정보는 삭제된다.")
    void deleteLine() {
        // given
        LineResponse line = TestHelper.createLine("신분당선");

        // when
        ExtractableResponse<Response> response = RestAssured
                .given()
                .when()
                    .delete("/lines/{id}", line.getId())
                .then()
                    .statusCode(HttpStatus.NO_CONTENT.value())
                    .extract();

        // then
        List<LineResponse> lineResponses = TestHelper.selectAllLines();

        assertThat(lineResponses)
                .extracting(LineResponse::getName)
                .doesNotContain("신분당선");
    }


}
