package subway;

import io.restassured.common.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import subway.controller.dto.LineCreateRequest;
import subway.controller.dto.LineResponse;
import subway.controller.dto.LineUpdateRequest;
import subway.controller.dto.StationResponse;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {

    private static final String LINE_NAME_SHINBUNDANG = "신분당선";
    private static final String LINE_NAME_BUNDANG = "분당선";
    private static final String LINE_COLOR_RED = "bg-red-600";
    private static final String LINE_COLOR_GREEN = "bg-green-600";
    private static final Long DISTANCE = 10L;

    private static Long GANGNAM_STATION_ID;
    private static Long SEOLLEUNG_STATION_ID;
    private static Long YANGJAE_STATION_ID;

    @BeforeEach
    void setFixture() {
        GANGNAM_STATION_ID = post("/stations", Map.of("name", "강남역"))
                .as(StationResponse.class).getId();

        SEOLLEUNG_STATION_ID = post("/stations", Map.of("name", "선릉역"))
                .as(StationResponse.class).getId();

        YANGJAE_STATION_ID = post("/stations", Map.of("name", "양재역"))
                .as(StationResponse.class).getId();
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // given
        LineCreateRequest request = new LineCreateRequest(
                LINE_NAME_SHINBUNDANG,
                LINE_COLOR_RED,
                GANGNAM_STATION_ID,
                SEOLLEUNG_STATION_ID,
                DISTANCE
        );

        // when
        ExtractableResponse<Response> createResponse = createLine(request);

        // then
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        LineResponse lineResponse = createResponse.as(LineResponse.class);

        ExtractableResponse<Response> findResponse = findLines();
        List<Long> lineIds = findResponse.jsonPath().getList("id", Long.class);
        assertThat(lineIds).containsAnyOf(lineResponse.getId());
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void selectLines() {
        // given
        LineCreateRequest shinbundangRequest = new LineCreateRequest(
                LINE_NAME_SHINBUNDANG,
                LINE_COLOR_RED,
                GANGNAM_STATION_ID,
                SEOLLEUNG_STATION_ID,
                DISTANCE
        );
        ExtractableResponse<Response> shinbundangCreateResponse = createLine(shinbundangRequest);
        assertThat(shinbundangCreateResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        LineCreateRequest bundangRequest = new LineCreateRequest(
                LINE_NAME_BUNDANG,
                LINE_COLOR_GREEN,
                GANGNAM_STATION_ID,
                YANGJAE_STATION_ID,
                10L
        );
        ExtractableResponse<Response> bundangCreateResponse = createLine(bundangRequest);
        assertThat(bundangCreateResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        //when
        ExtractableResponse<Response> findResponse = findLines();
        List<String> linesNames = findResponse.jsonPath().getList("name", String.class);

        // then
        assertThat(linesNames).hasSize(2)
                .containsExactly("신분당선", "분당선");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void selectLine() {
        // given
        LineCreateRequest shinbundangRequest = new LineCreateRequest(
                LINE_NAME_SHINBUNDANG,
                LINE_COLOR_RED,
                GANGNAM_STATION_ID,
                SEOLLEUNG_STATION_ID,
                DISTANCE
        );
        ExtractableResponse<Response> shinbundangCreateResponse = createLine(shinbundangRequest);
        assertThat(shinbundangCreateResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        LineResponse createLineResponse = shinbundangCreateResponse.as(LineResponse.class);

        // when
        LineResponse findLineResponse = findLine(createLineResponse.getId()).as(LineResponse.class);

        // then
        assertAll(
                () -> assertThat(findLineResponse.getId()).isEqualTo(1L),
                () -> assertThat(findLineResponse.getName()).isEqualTo(LINE_NAME_SHINBUNDANG),
                () -> assertThat(findLineResponse.getColor()).isEqualTo(LINE_COLOR_RED),
                () -> assertThat(findLineResponse.getStations()).hasSize(2)
                        .extracting("id", "name")
                        .containsExactly(
                                tuple(1L, "강남역"),
                                tuple(2L, "선릉역")
                        )
        );
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다.
     */
    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        LineCreateRequest shinbundangRequest = new LineCreateRequest(
                LINE_NAME_SHINBUNDANG,
                LINE_COLOR_RED,
                GANGNAM_STATION_ID,
                SEOLLEUNG_STATION_ID,
                DISTANCE
        );
        ExtractableResponse<Response> shinbundangCreateResponse = createLine(shinbundangRequest);
        assertThat(shinbundangCreateResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        LineResponse createLineResponse = shinbundangCreateResponse.as(LineResponse.class);

        // when
        ExtractableResponse<Response> shinbundangUpdateResponse = updateLine(createLineResponse.getId(), new LineUpdateRequest("다른분당선", "bg-blue-600"));
        assertThat(shinbundangUpdateResponse.statusCode()).isEqualTo(HttpStatus.OK.value());

        // then
        LineResponse findLineResponse = findLine(createLineResponse.getId()).as(LineResponse.class);

        assertAll(
                () -> assertThat(findLineResponse.getId()).isEqualTo(1L),
                () -> assertThat(findLineResponse.getName()).isEqualTo("다른분당선"),
                () -> assertThat(findLineResponse.getColor()).isEqualTo("bg-blue-600"),
                () -> assertThat(findLineResponse.getStations()).hasSize(2)
                        .extracting("id", "name")
                        .containsExactly(
                                tuple(1L, "강남역"),
                                tuple(2L, "선릉역")
                        )
        );
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
        LineCreateRequest shinbundangRequest = new LineCreateRequest(
                LINE_NAME_SHINBUNDANG,
                LINE_COLOR_RED,
                GANGNAM_STATION_ID,
                SEOLLEUNG_STATION_ID,
                DISTANCE
        );
        ExtractableResponse<Response> shinbundangCreateResponse = createLine(shinbundangRequest);
        assertThat(shinbundangCreateResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        LineResponse createLineResponse = shinbundangCreateResponse.as(LineResponse.class);

        // when
        ExtractableResponse<Response> shinbundangDeleteResponse = deleteLine(createLineResponse.getId());
        assertThat(shinbundangDeleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        // then
        List<LineResponse> findAllResponse = findLines().as(new TypeRef<>() {
        });
        assertThat(findAllResponse).isEmpty();
    }

    private ExtractableResponse<Response> createLine(LineCreateRequest request) {
        return post("/lines", request);
    }

    private ExtractableResponse<Response> findLines() {
        return get("/lines");
    }

    private ExtractableResponse<Response> findLine(Long id) {
        return get("/lines/{id}", id);
    }

    private ExtractableResponse<Response> updateLine(Long id, LineUpdateRequest request) {
        return put("/lines/{id}", request, id);
    }

    private ExtractableResponse<Response> deleteLine(Long id) {
        return delete("/lines/{id}", id);
    }

}
