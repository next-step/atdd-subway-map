package subway;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import subway.dto.LineRequest;

@DisplayName("지하철 노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql("/init.sql")
public class LineAcceptanceTest {
    private final SubwayRestApiClient client = new SubwayRestApiClient();

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // when
        ExtractableResponse<Response> response = client.createLine(new LineRequest("신분당선", "bg-red-600", 1L, 2L, 10L));

        // then
        validateStatusCode(response, HttpStatus.CREATED);
        validateFieldEquals(response, "name", String.class, "신분당선");
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void findLines() {
        // given
        client.createLine(new LineRequest("신분당선", "bg-red-600", 1L, 2L, 10L));
        client.createLine(new LineRequest("분당선", "bg-red-600", 2L, 3L, 10L));

        // when
        ExtractableResponse<Response> response = client.findAllLines();
        List<String> lineNames = response.jsonPath().getList("name", String.class);

        // then
        validateStatusCode(response, HttpStatus.OK);
        validateFieldBodyHasSize(lineNames, 2);
        validateFieldContainsExactly(lineNames, "신분당선", "분당선");
    }

    @DisplayName("특정 지하철 노선을 조회한다.")
    @Test
    void findLineById() {
        // given
        var line = client.createLine(new LineRequest("신분당선", "bg-red-600", 1L, 2L, 10L));
        long lineId = line.jsonPath().getLong("id");

        // when
        ExtractableResponse<Response> response = client.findLineById(lineId);

        // then
        validateStatusCode(response, HttpStatus.OK);
        validateFieldEquals(response, "id", Long.class, lineId);
    }

    @DisplayName("특정 지하철 노선의 정보를 갱신한다.")
    @Test
    void updateLine() {
        // given
        var line = client.createLine(new LineRequest("신분당선", "bg-red-600", 1L, 2L, 10L));
        long lineId = line.jsonPath().getLong("id");

        // when
        ExtractableResponse<Response> response = client.updateLine(lineId, new LineRequest("분당선", "bg-yellow-600", 2L, 3L, 100L));

        // then
        validateStatusCode(response, HttpStatus.OK);
        validateFieldEquals(response, "id", Long.class, lineId);
        validateFieldEquals(response, "name", String.class, "분당선");
    }

    @DisplayName("특정 지하철 노선을 삭제한다.")
    @Test
    void deleteLine() {
        // given
        var line = client.createLine(new LineRequest("신분당선", "bg-red-600", 1L, 2L, 10L));
        long lineId = line.jsonPath().getLong("id");

        // when
        ExtractableResponse<Response> deleteResponse = client.deleteLine(lineId);
        ExtractableResponse<Response> findResponse = client.findLineById(lineId);

        // then
        validateStatusCode(deleteResponse, HttpStatus.NO_CONTENT);
        validateStatusCode(findResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void validateStatusCode(ExtractableResponse<Response> response, HttpStatus httpStatus) {
        assertThat(response.statusCode()).isEqualTo(httpStatus.value());
    }

    private <T> T extractValue(ExtractableResponse<Response> response, String path, Class<T> clazz) {
        return response.jsonPath().getObject(path, clazz);
    }

    private <T> void validateFieldEquals(ExtractableResponse<Response> response, String path, Class<T> clazz, T expected) {
        assertThat(extractValue(response, path, clazz)).isEqualTo(expected);
    }

    private <T> void validateFieldContainsExactly(List<T> actual, T... expected) {
        assertThat(actual).containsExactly(expected);
    }

    private <T> void validateFieldBodyHasSize(List<T> actual, int expected) {
        assertThat(actual).hasSize(expected);
    }
}
