package subway;

import io.restassured.common.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.controller.dto.LineCreateRequest;
import subway.controller.dto.LineResponse;
import subway.controller.dto.LineUpdateRequest;
import subway.controller.dto.StationResponse;

import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.http.HttpStatus.*;
import static subway.fixture.LineFixture.BUNDANG_LINE;
import static subway.fixture.LineFixture.SHINBUNDANG_LINE;
import static subway.fixture.StationFixture.*;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {
    private static Long GANGNAM_STATION_ID;
    private static Long SEOLLEUNG_STATION_ID;
    private static Long YANGJAE_STATION_ID;

    @BeforeEach
    void setFixture() {
        GANGNAM_STATION_ID = createStation(GANGNAM_STATION.toCreateRequest(), CREATED.value())
                .as(StationResponse.class).getId();

        SEOLLEUNG_STATION_ID = createStation(SEOLLEUNG_STATION.toCreateRequest(), CREATED.value())
                .as(StationResponse.class).getId();

        YANGJAE_STATION_ID = createStation(YANGJAE_STATION.toCreateRequest(), CREATED.value())
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
        LineCreateRequest request = SHINBUNDANG_LINE.toCreateRequest(GANGNAM_STATION_ID, SEOLLEUNG_STATION_ID);

        // when
        ExtractableResponse<Response> createResponse = createLine(request, CREATED.value());

        LineResponse lineResponse = createResponse.as(LineResponse.class);

        ExtractableResponse<Response> findResponse = findLines(OK.value());
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
        LineCreateRequest shinbundangRequest = SHINBUNDANG_LINE.toCreateRequest(GANGNAM_STATION_ID, SEOLLEUNG_STATION_ID);
        createLine(shinbundangRequest, CREATED.value());

        LineCreateRequest bundangRequest = BUNDANG_LINE.toCreateRequest(GANGNAM_STATION_ID, YANGJAE_STATION_ID);
        createLine(bundangRequest, CREATED.value());

        //when
        ExtractableResponse<Response> findResponse = findLines(OK.value());
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
        LineCreateRequest shinbundangRequest = SHINBUNDANG_LINE.toCreateRequest(GANGNAM_STATION_ID, SEOLLEUNG_STATION_ID);
        ExtractableResponse<Response> shinbundangCreateResponse = createLine(shinbundangRequest, CREATED.value());
        LineResponse createLineResponse = shinbundangCreateResponse.as(LineResponse.class);

        // when
        LineResponse findLineResponse = findLine(createLineResponse.getId(), OK.value()).as(LineResponse.class);

        // then
        assertAll(
                () -> assertThat(findLineResponse.getId()).isEqualTo(1L),
                () -> assertThat(findLineResponse.getName()).isEqualTo("신분당선"),
                () -> assertThat(findLineResponse.getColor()).isEqualTo("bg-red-600"),
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
        LineCreateRequest shinbundangRequest = SHINBUNDANG_LINE.toCreateRequest(GANGNAM_STATION_ID, SEOLLEUNG_STATION_ID);
        ExtractableResponse<Response> shinbundangCreateResponse = createLine(shinbundangRequest, CREATED.value());
        LineResponse createLineResponse = shinbundangCreateResponse.as(LineResponse.class);

        // when
        updateLine(createLineResponse.getId(), new LineUpdateRequest("다른분당선", "bg-blue-600"), OK.value());

        // then
        LineResponse findLineResponse = findLine(createLineResponse.getId(), OK.value()).as(LineResponse.class);

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
        LineCreateRequest shinbundangRequest = SHINBUNDANG_LINE.toCreateRequest(GANGNAM_STATION_ID, SEOLLEUNG_STATION_ID);
        ExtractableResponse<Response> shinbundangCreateResponse = createLine(shinbundangRequest, CREATED.value());
        LineResponse createLineResponse = shinbundangCreateResponse.as(LineResponse.class);

        // when
        ExtractableResponse<Response> shinbundangDeleteResponse = deleteLine(createLineResponse.getId(), NO_CONTENT.value());
        assertThat(shinbundangDeleteResponse.statusCode()).isEqualTo(NO_CONTENT.value());

        // then
        List<LineResponse> findAllResponse = findLines(OK.value()).as(new TypeRef<>() {
        });
        assertThat(findAllResponse).isEmpty();
    }

    private ExtractableResponse<Response> findLines(int statusCode) {
        return get("/lines", statusCode);
    }

    private ExtractableResponse<Response> updateLine(Long id, LineUpdateRequest request, int statusCode) {
        return put("/lines/{id}", request, statusCode, id);
    }

    private ExtractableResponse<Response> deleteLine(Long id, int statusCode) {
        return delete("/lines/{id}", statusCode, new HashMap<>(), id);
    }

}
