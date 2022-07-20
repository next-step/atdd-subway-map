package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.api.LineApiCall;
import nextstep.subway.api.StationApiCall;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineUpdateRequest;
import nextstep.subway.applicaion.dto.StationRequest;
import nextstep.subway.util.DatabaseUitl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LineAcceptanceTest {
    @LocalServerPort
    int port;

    @Autowired
    DatabaseUitl databaseUitl;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        databaseUitl.tableClear();
    }


    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createSubwayLine() {

        final String 강남역 = "강남역";
        final String 광교역 = "광교역";

        final String 신분당선 = "신분당선";

        // 생성된 지하철역 목록
        List<ExtractableResponse<Response>> stationCreationResponses = StationApiCall.createStations(강남역, 광교역);

        Long 강남역_아이디 = getId(stationCreationResponses.get(0));
        Long 광교역_아이디 = getId(stationCreationResponses.get(1));

        // when
        ExtractableResponse<Response> lineCreationResponse = LineApiCall.createLine(new LineRequest(신분당선, "bg-red-600", 강남역_아이디, 광교역_아이디, 10));
        assertThat(lineCreationResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        final Long 신분당선_아이디 = lineCreationResponse.jsonPath().getLong("id");

        ExtractableResponse<Response> getResponse = LineApiCall.getLine(신분당선_아이디);

        String lineName = getResponse.jsonPath().getString("name");
        String lineColor = getResponse.jsonPath().getString("color");

        // then
        assertThat(lineName).isEqualTo(신분당선);
        assertThat(lineColor).isEqualTo("bg-red-600");

    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLines() {

        final String 강남역 = "강남역";
        final String 판교역 = "판교역";
        final String 부평구청역 = "부평구청역";
        final String 장암역 = "장암역";

        final String 신분당선 = "신분당선";
        final String 칠호선 = "7호선";

        List<ExtractableResponse<Response>> stationCreationResponses = StationApiCall.createStations(강남역, 판교역, 부평구청역, 장암역);

        Long 강남역_아이디 = getId(stationCreationResponses.get(0));
        Long 판교역_아이디 = getId(stationCreationResponses.get(1));
        Long 부평구청역_아이디 = getId(stationCreationResponses.get(2));
        Long 장암역_아이디 = getId(stationCreationResponses.get(3));

        // given
        LineApiCall.createLine(new LineRequest(신분당선, "bg-red-600", 강남역_아이디, 판교역_아이디, 10));
        LineApiCall.createLine(new LineRequest(칠호선, "bg-brown-600", 부평구청역_아이디, 장암역_아이디, 10));

        // when
        List<String> lineNames = LineApiCall.getLines().jsonPath().getList("name", String.class);

        // then
        assertThat(lineNames).contains("신분당선", "7호선");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLines() {

        final String 인천역 = "인천역";
        final String 소요산역 = "소요산역";

        final String 일호선 = "1호선";

        List<ExtractableResponse<Response>> stationCreationResponses = StationApiCall.createStations(인천역, 소요산역);

        Long 인천역_아이디 = getId(stationCreationResponses.get(0));
        Long 소요산역_아이디 = getId(stationCreationResponses.get(1));

        // given
        ExtractableResponse<Response> createResponse = LineApiCall.createLine(new LineRequest(일호선, "bg-blue-600", 인천역_아이디, 소요산역_아이디, 10));
        Long 일호선_아이디 = createResponse.jsonPath().getLong("id");

        String lineName = "새로운노선";
        String lineColor = "bg-deepblue-600";

        // when
        ExtractableResponse<Response> updateResponse = LineApiCall.updateLine(일호선_아이디, new LineUpdateRequest(lineName, lineColor));
        assertThat(updateResponse.statusCode()).isEqualTo(HttpStatus.OK.value());

        // then
        List<String> lineNames = LineApiCall.getLines().jsonPath().getList("name", String.class);
        assertThat(lineNames).contains("새로운노선");

    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("지하철 노선을 삭제한다.")
    @Test
    void deleteLines() {

        final String 수원역 = "수원역";
        final String 죽전역 = "죽전역";

        final String 분당선 = "분당선";

        List<ExtractableResponse<Response>> stationCreationResponses = StationApiCall.createStations(수원역, 죽전역);

        Long 수원역_아이디 = getId(stationCreationResponses.get(0));
        Long 죽전역_아이디 = getId(stationCreationResponses.get(1));

        // given
        ExtractableResponse<Response> lineCreationResponse = LineApiCall.createLine(new LineRequest(분당선, "bg-yellow-600", 수원역_아이디, 죽전역_아이디, 10));
        Long 분당선_아이디 = lineCreationResponse.jsonPath().getLong("id");

        // when
        ExtractableResponse<Response> lineDeletionResponse = LineApiCall.deleteLine(분당선_아이디);

        // then
        assertThat(lineDeletionResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

    }

    // 응답객체 id 데이터 조회
    private Long getId(ExtractableResponse<Response> response) {
        return response.jsonPath().getLong("id");
    }

}
