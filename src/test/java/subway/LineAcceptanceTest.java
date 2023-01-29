package subway;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import subway.dto.LineRequest;

@DisplayName("지하철 노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql("/init.sql")
public class LineAcceptanceTest {
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // when
        ExtractableResponse<Response> response = createLine(new LineRequest("신분당선", "bg-red-600", 1L, 2L, 10L));

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.jsonPath().getString("name")).contains("신분당선");
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void findLines() {
        // given
        createLine(new LineRequest("신분당선", "bg-red-600", 1L, 2L, 10L));
        createLine(new LineRequest("분당선", "bg-red-600", 2L, 3L, 10L));
        createLine(new LineRequest("신분당선", "bg-red-600", 1L, 3L, 10L));

        // when
        ExtractableResponse<Response> response = findAllLines();
        List<String> lineNames = response.jsonPath().getList("name", String.class);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(lineNames).hasSize(3);
        assertThat(lineNames).containsExactly("신분당선", "분당선", "신분당선");
    }

    @DisplayName("특정 지하철 노선을 조회한다.")
    @Test
    void findLineById() {
        // given
        var line = createLine(new LineRequest("신분당선", "bg-red-600", 1L, 2L, 10L));
        long lineId = line.jsonPath().getLong("id");

        // when
        ExtractableResponse<Response> response = findLineById(lineId);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getLong("id")).isEqualTo(lineId);
    }

    @DisplayName("특정 지하철 노선의 정보를 갱신한다.")
    @Test
    void updateLine() {
        // given
        var line = createLine(new LineRequest("신분당선", "bg-red-600", 1L, 2L, 10L));
        long lineId = line.jsonPath().getLong("id");

        // when
        ExtractableResponse<Response> response = updateLine(lineId, new LineRequest("분당선", "bg-yellow-600", 2L, 3L, 100L));

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getLong("id")).isEqualTo(lineId);
        assertThat(response.jsonPath().getString("name")).isEqualTo("분당선");
    }

    @DisplayName("특정 지하철 노선을 삭제한다.")
    @Test
    void deleteLine() {
        // given
        var line = createLine(new LineRequest("신분당선", "bg-red-600", 1L, 2L, 10L));
        long lineId = line.jsonPath().getLong("id");

        // when
        ExtractableResponse<Response> deleteResponse = deleteLine(lineId);
        ExtractableResponse<Response> findResponse = findLineById(lineId);

        // then
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        assertThat(findResponse.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    private ExtractableResponse<Response> createLine(LineRequest lineRequest) {
        return RestAssured.given().log().all()
            .body(lineRequest)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/lines")
            .then().log().all()
            .extract();
    }

    private ExtractableResponse<Response> findAllLines() {
        return RestAssured.given().log().all()
            .when().get("/lines")
            .then().log().all()
            .extract();
    }

    private ExtractableResponse<Response> findLineById(Long lineId) {
        return RestAssured.given().log().all()
            .when().get("/lines/{id}", lineId)
            .then().log().all()
            .extract();
    }

    private ExtractableResponse<Response> updateLine(Long lineId, LineRequest lineRequest) {
        return RestAssured.given().log().all()
            .body(lineRequest)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().put("/lines/{id}", lineId)
            .then().log().all()
            .extract();
    }

    private ExtractableResponse<Response> deleteLine(Long lineId) {
        return RestAssured.given().log().all()
            .when().delete("/lines/{id}", lineId)
            .then().log().all()
            .extract();
    }
}
