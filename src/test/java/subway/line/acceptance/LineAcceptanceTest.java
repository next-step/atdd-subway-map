package subway.line.acceptance;

import core.AcceptanceTestExtension;
import core.RestAssuredHelper;
import core.TestConfig;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import subway.common.LineApiHelper;
import subway.common.StationApiHelper;
import subway.line.service.dto.LineResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = TestConfig.class)
@ExtendWith(AcceptanceTestExtension.class)
public class LineAcceptanceTest {
    private Long 지하철역_Id;
    private Long 새로운지하철역_Id;
    private Long 또다른지하철역_Id;
    private final String 신분당선 = "신분당선";
    private final String 신분당선_color = "bg-red-600";
    private final int 신분당선_distance = 10;
    private final String 분당선 = "분당선";
    private final String 분당선_color = "bg-green-600";
    private final int 분당선_distance = 15;

    @BeforeEach
    void setUp() {
        지하철역_Id = RestAssuredHelper.getIdFrom(StationApiHelper.createStation("지하철역"));
        새로운지하철역_Id = RestAssuredHelper.getIdFrom(StationApiHelper.createStation("새로운지하철역"));
        또다른지하철역_Id = RestAssuredHelper.getIdFrom(StationApiHelper.createStation("또다른지하철역"));
    }

    /**
     * When 노선을 생성하면
     * Then 노선이 생성된다
     * Then 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("노선을 생성한다.")
    @Test
    void createLineTest() {
        // when
        final ExtractableResponse<Response> response = LineApiHelper.createLine(신분당선, 신분당선_color, 지하철역_Id, 새로운지하철역_Id, 신분당선_distance);

        // then
        assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
            assertLineResponse(response.as(LineResponse.class), 신분당선, 신분당선_color, 지하철역_Id, 새로운지하철역_Id);
        });

        // then
        final List<String> lineNames = getLineNames(LineApiHelper.fetchLines());
        assertThat(lineNames).containsAnyOf(신분당선);
    }

    /**
     * Given 2개의 노선을 생성하고
     * When 노선 목록을 조회하면
     * Then 2개의 노선을 응답 받는다
     */
    @DisplayName("노선 목록을 조회한다.")
    @Test
    void fetchLinesTest() {
        // given
        final ExtractableResponse<Response> 신분당선_response = LineApiHelper.createLine(신분당선, 신분당선_color, 지하철역_Id, 새로운지하철역_Id, 신분당선_distance);
        final ExtractableResponse<Response> 분당선_response = LineApiHelper.createLine(분당선, 분당선_color, 지하철역_Id, 또다른지하철역_Id, 분당선_distance);

        // when
        final ExtractableResponse<Response> response = LineApiHelper.fetchLines();

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())
                , () -> assertResponseData(response, RestAssuredHelper.getIdFrom(신분당선_response), 신분당선, 신분당선_color, 지하철역_Id, 새로운지하철역_Id)
                , () -> assertResponseData(response, RestAssuredHelper.getIdFrom(분당선_response), 분당선, 분당선_color, 지하철역_Id, 또다른지하철역_Id)
        );
    }

    /**
     * Given 노선을 생성하고
     * When 생성한 노선을 조회하면
     * Then 생성한 노선을 응답 받는다
     */
    @DisplayName("특정 노선을 조회한다.")
    @Test
    void fetchLineByIdTest() {
        // given
        final ExtractableResponse<Response> 신분당선_response = LineApiHelper.createLine(신분당선, 신분당선_color, 지하철역_Id, 새로운지하철역_Id, 신분당선_distance);
        final Long 신분당선_id = RestAssuredHelper.getIdFrom(신분당선_response);

        // when
        final ExtractableResponse<Response> response = LineApiHelper.fetchLineById(신분당선_id);

        // then
        assertSoftly(softly -> {
            assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            assertLineResponse(response.as(LineResponse.class), 신분당선, 신분당선_color, 지하철역_Id, 새로운지하철역_Id);
        });
    }

    /**
     * Given 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("특정 노선을 수정한다.")
    @Test
    void updateLineByIdTest() {
        // given
        final ExtractableResponse<Response> 신분당선_response = LineApiHelper.createLine(신분당선, 신분당선_color, 지하철역_Id, 새로운지하철역_Id, 신분당선_distance);
        final Long 신분당선_id = RestAssuredHelper.getIdFrom(신분당선_response);

        // when
        final String 다른분당선 = "다른분당선";
        final ExtractableResponse<Response> response = LineApiHelper.modifyLine(신분당선_id, 다른분당선, 신분당선_color);

        // then
        assertSoftly(softly -> {
            assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            final LineResponse lineResponse = LineApiHelper.fetchLineById(신분당선_id).as(LineResponse.class);
            assertLineResponse(lineResponse, 다른분당선, 신분당선_color, 지하철역_Id, 새로운지하철역_Id);
        });
    }

    /**
     * Given 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("특정 노선을 삭제한다.")
    @Test
    void deleteLineByIdTest() {
        // given
        final ExtractableResponse<Response> 신분당선_response = LineApiHelper.createLine(신분당선, 신분당선_color, 지하철역_Id, 새로운지하철역_Id, 신분당선_distance);
        final ExtractableResponse<Response> 분당선_response = LineApiHelper.createLine(분당선, 분당선_color, 지하철역_Id, 또다른지하철역_Id, 분당선_distance);
        final Long 신분당선_id = RestAssuredHelper.getIdFrom(신분당선_response);

        // when
        final ExtractableResponse<Response> response = LineApiHelper.removeLine(신분당선_id);

        // then
        assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
            final List<Long> lineIds = getIds(LineApiHelper.fetchLines());
            softly.assertThat(lineIds).containsExactly(RestAssuredHelper.getIdFrom(분당선_response));
        });
    }

    private List<Long> getIds(final ExtractableResponse<Response> response) {
        return response.jsonPath().getList("id", Long.class);
    }

    private List<String> getLineNames(final ExtractableResponse<Response> response) {
        return response.jsonPath().getList("name", String.class);
    }

    private void assertResponseData(final ExtractableResponse<Response> response, final Long id, final String name, final String color, final Long upStationId, final Long downStationId) {
        final LineResponse lineResponse = RestAssuredHelper.findObjectFrom(response, id, LineResponse.class);
        assertLineResponse(lineResponse, name, color, upStationId, downStationId);
    }

    private void assertLineResponse(final LineResponse lineResponse, final String name, final String color, final Long upStationId, final Long downStationId) {
        assertSoftly(softly -> {
            softly.assertThat(lineResponse.getName()).isEqualTo(name);
            softly.assertThat(lineResponse.getColor()).isEqualTo(color);
            softly.assertThat(lineResponse.getStations())
                    .extracting("id").containsExactly(upStationId, downStationId);
        });
    }

}
