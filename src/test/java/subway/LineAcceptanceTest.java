package subway;

import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import subway.line.LineResponse;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@DisplayName("지하철 노선 관련 기능")
@Sql("/insert-station.sql")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class LineAcceptanceTest extends LineAcceptConstants {

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createStationTest() {
        // given
        createLine(이호선);

        // when
        List<String> lineNames = getLinesNames();

        // then
        assertThat(lineNames.size()).isEqualTo(1);
        assertThat(lineNames).containsOnly((String) 이호선.get(LINE_NAME));
    }


    private void createLine(final Map<String, Object> line) {
        RestAssured
                .given()
                    .contentType(APPLICATION_JSON_VALUE)
                    .accept(APPLICATION_JSON_VALUE)
                    .body(line)
                .when()
                    .post("/lines")
                .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .body("name", Matchers.equalTo(line.get(LINE_NAME)));
    }

    private List<LineResponse> getLines() {
        return RestAssured
                .given()
                    .accept(APPLICATION_JSON_VALUE)
                .when()
                    .get("/lines")
                .then()
                    .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath()
                .getList("", LineResponse.class);
    }

    private List<String> getLinesNames() {
        return getLines().stream()
                .map(LineResponse::getName)
                .collect(Collectors.toList());
    }


}
