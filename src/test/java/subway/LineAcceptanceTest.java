package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import subway.controller.dto.LineCreateRequest;
import subway.controller.dto.LineUpdateRequest;

import java.util.List;

import static helper.JsonPathUtils.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관련 기능")
@SqlGroup({
    @Sql(value = "/sql/setup-station-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
    @Sql(value = "/sql/truncate-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LineAcceptanceTest {

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선 생성")
    @Nested
    class 지하철_노선_생성_테스트 {
        // TODO: 유효하지 않은 값에 대한 검증 추가
        @DisplayName("지하철 노선을 생성하면, 생성한 노선을 찾을 수 있다")
        @Test
        void createSubwayLine() {
            // given
            LineCreateRequest 수인분당선 = new LineCreateRequest(
                "수인분당선", "bg-yellow-600", 1L, 2L, 10
            );

            // when
            ExtractableResponse<Response> response = LineAcceptanceTest.createSubwayLine(수인분당선);

            assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
            assertThat(getStringPath(response.body(), "name")).isEqualTo(수인분당선.getName());

            long createdSubwayLineId = getSubwayLineId(response);
            assertThat(createdSubwayLineId).isNotNull();

            // then
            ExtractableResponse<Response> getSubwayLinesResponse = getSubwayLine(createdSubwayLineId);

            assertThat(getSubwayLinesResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
            assertThat(getSubwayLineId(getSubwayLinesResponse)).isEqualTo(createdSubwayLineId);
        }
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철 노선 목록 조회")
    @Nested
    class 지하철_노선_목록_조회_테스트 {

        @DisplayName("생성한 지하철 노선을 모두 조회할 수 있다")
        @Test
        void will_return_created_subway_lines() {
            // TODO: Request DTO 사용으로 변경?
            // given
            LineCreateRequest 수인분당선 = new LineCreateRequest(
                "수인분당선", "bg-yellow-600", 1L, 2L, 10
            );
            LineCreateRequest 신분당선 = new LineCreateRequest(
                "신분당선", "bg-red-600", 11L, 12L, 5
            );

            createSubwayLine(수인분당선);
            createSubwayLine(신분당선);

            // when
            ExtractableResponse<Response> getSubwayLinesResponse = getSubwayLines();

            assertThat(getSubwayLinesResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
            assertThat(getListPath(getSubwayLinesResponse.body(), "name", String.class))
                .containsExactly(수인분당선.getName(), 신분당선.getName());
        }
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철 노선 조회")
    @Nested
    class 지하철_노선_조회_테스트 {
        // TODO: 찾을 수 없는 노선 ID가 주어지는 경우 검증
        @DisplayName("생성한 지하철 노선을 조회하면 해당 노선의 정보를 응답받는다")
        @Test
        void will_return_subway_line() {
            // given
            LineCreateRequest 수인분당선 = new LineCreateRequest(
                "수인분당선", "bg-yellow-600", 1L, 2L, 10
            );

            ExtractableResponse<Response> createSubwayResponse = createSubwayLine(수인분당선);
            long createdSubwayLineId = getSubwayLineId(createSubwayResponse);

            // when
            ExtractableResponse<Response> getSubwayLineResponse = getSubwayLine(createdSubwayLineId);

            // then
            assertThat(getSubwayLineResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
            assertThat(getSubwayLineId(getSubwayLineResponse)).isEqualTo(createdSubwayLineId);
        }
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("지하철 노선 수정")
    @Nested
    class 지하철_노선_수정_테스트 {
        // TODO: 유효하지 않은 수정 데이터 검증
        @DisplayName("생성한 지하철 노선을 수정하면 해당 지하철 노선 정보는 수정된다")
        @Test
        void will_return_updated_subway_line_data() {
            // given
            LineCreateRequest 수인분당선 = new LineCreateRequest(
                "수인분당선", "bg-yellow-600", 1L, 2L, 10
            );

            ExtractableResponse<Response> createSubwayResponse = createSubwayLine(수인분당선);
            long createdSubwayLineId = getSubwayLineId(createSubwayResponse);

            // when
            LineUpdateRequest updateRequest = new LineUpdateRequest("수인분당당선", "bg-brown-600");

            ExtractableResponse<Response> updateResponse = RestAssured.given().log().all()
                .body(updateRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("lineId", createdSubwayLineId)
                .when().put("/lines/{lineId}")
                .then().log().all()
                .extract();

            assertThat(updateResponse.statusCode()).isEqualTo(HttpStatus.OK.value());

            // when
            ExtractableResponse<Response> getSubwayLineResponse = getSubwayLine(createdSubwayLineId);

            // then
            assertThat(getSubwayLineResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
            assertThat(getStringPath(getSubwayLineResponse.body(), "name"))
                .isEqualTo(updateRequest.getName());
            assertThat(getStringPath(getSubwayLineResponse.body(), "color"))
                .isEqualTo(updateRequest.getColor());
        }
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("지하철 노선 삭제")
    @Nested
    class 지하철_노선_삭제_테스트 {

        @DisplayName("생성한 지하철 노선을 삭제하면 해당 지하철 노선 정보는 삭제된다")
        @Test
        void will_delete_subway_line() {
            // given
            LineCreateRequest 수인분당선 = new LineCreateRequest(
                "수인분당선", "bg-yellow-600", 1L, 2L, 10
            );

            ExtractableResponse<Response> createSubwayResponse = createSubwayLine(수인분당선);
            long createdSubwayLineId = getSubwayLineId(createSubwayResponse);

            // when
            ExtractableResponse<Response> deleteResponse = RestAssured.given().log().all()
                .pathParam("lineId", createdSubwayLineId)
                .when().delete("/lines/{lineId}")
                .then().log().all()
                .extract();

            assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

            // then
            ExtractableResponse<Response> getSubwayLinesResponse = getSubwayLines();

            assertThat(getSubwayLinesResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
            assertThat(getSubwayLineIds(getSubwayLinesResponse)).noneMatch(id -> id.equals(createdSubwayLineId));
        }
    }

    /** 주어진 지하철 노선 정보를 통해 지하철 노선 생성 요청 후 응답값을 반환합니다 */
    private static ExtractableResponse<Response> createSubwayLine(
        final LineCreateRequest request
    ) {
        return RestAssured.given().log().all()
            .body(request)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/lines")
            .then().log().all()
            .extract();
    }

    /** 주어진 응답값에서 식별자를 추출해 반환합니다 */
    private long getSubwayLineId(final ExtractableResponse<Response> response) {
        return getLongPath(response.body(), "id");
    }

    /** 주어진 응답값에서 식별자목록을 추출해 반환합니다 */
    private List<Long> getSubwayLineIds(final ExtractableResponse<Response> response) {
        return getListPath(response.body(), "id", Long.class);
    }

    /** 모든 지하철 노선을 조회하는 요청 후 응답값을 반환합니다 */
    private ExtractableResponse<Response> getSubwayLines() {
        return RestAssured.given().log().all()
            .when().get("/lines")
            .then().log().all()
            .extract();
    }

    /** 주어진 지하철 노선 식별자를 통해 지하철 노선 조회 후 응답값을 반환합니다 */
    private ExtractableResponse<Response> getSubwayLine(final long subwayLineId) {
        return RestAssured.given().log().all()
            .pathParam("lineId", subwayLineId)
            .when().get("/lines/{lineId}")
            .then().log().all()
            .extract();
    }
}
