package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static nextstep.subway.acceptance.AssertSteps.httpStatusCode_검증;
import static nextstep.subway.acceptance.LineFixture.*;
import static nextstep.subway.acceptance.LineSteps.노선_생성_요청_응답_HttpStatusCode;
import static nextstep.subway.acceptance.StationFixture.*;
import static nextstep.subway.acceptance.StationSteps.지하철_역_생성_요청_응답;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.http.HttpStatus.*;

@DisplayName("지하철 노선 관리 기능")
class LineAcceptanceTest extends AcceptanceTest {

    private LineRequest 일호선;
    private LineRequest 이호선;
    private StationResponse 시청역;
    private StationResponse 서울역;
    private StationResponse 강남역;
    private StationResponse 역삼역;

    @BeforeEach
    public void setUp() {
        super.setUp();

        시청역 = 지하철_역_생성_요청_응답(FIXTURE_시청역);
        서울역 = 지하철_역_생성_요청_응답(FIXTURE_서울역);
        강남역 = 지하철_역_생성_요청_응답(FIXTURE_강남역);
        역삼역 = 지하철_역_생성_요청_응답(FIXTURE_역삼역);

        일호선 = LineRequest.of(일호선_이름, 일호선_색상, 시청역.getId(), 서울역.getId(), 0);
        이호선 = LineRequest.of(이호선_이름, 이호선_색상, 강남역.getId(), 역삼역.getId(), 0);
    }

    /**
     * When 지하철 노선 생성을 요청 하면
     * Then 지하철 노선 생성이 성공한다.
     */
    @DisplayName("지하철 노선 생성")
    @Test
    void createLine() {
        //given
        //when
        int statusCode = 노선_생성_요청_응답_HttpStatusCode(일호선);

        //then
        httpStatusCode_검증(statusCode, CREATED.value());

    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * when 동일한 이름의 지하철 노선 생성을 요청 하면
     * Then 지하철 노선 생성이 실패 한다.
     */
    @DisplayName("지하철 노선 중복 이름 생성 실패")
    @Test
    void createLineDuplicationNameException() {
        //given
        LineSteps.createLine(일호선);

        //when
        int statusCode = 노선_생성_요청_응답_HttpStatusCode(일호선);

        //then
        httpStatusCode_검증(statusCode, CONFLICT.value());
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * Given 새로운 지하철 노선 생성을 요청 하고
     * When 지하철 노선 목록 조회를 요청 하면
     * Then 두 노선이 포함된 지하철 노선 목록을 응답받는다
     */
    @DisplayName("지하철 노선 목록 조회")
    @Test
    void getLines() {
        //given
        LineSteps.createLine(일호선);
        LineSteps.createLine(이호선);

        //when
        ExtractableResponse<Response> response = LineSteps.findLines();

        int statusCode = response.statusCode();
        List<String> namesOfLines = response.jsonPath().getList("name");

        //then
        assertAll(
                () -> httpStatusCode_검증(statusCode, OK.value()),
                () -> assertThat(namesOfLines).containsExactly(일호선_이름, 이호선_이름)
        );

    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 생성한 지하철 노선 조회를 요청 하면
     * Then 생성한 지하철 노선을 응답받는다
     */
    @DisplayName("지하철 노선 조회")
    @Test
    void getLine() {
        //given
        ExtractableResponse<Response> lineNo1 = LineSteps.createLine(일호선);
        String uri = lineNo1.header("Location");

        //when
        ExtractableResponse<Response> response = LineSteps.findLines(uri);

        int statusCode = response.statusCode();
        String findLineName = response.jsonPath().get("name");
        List<String> namesOfStations = response.jsonPath().getList("stations.name");

        //then
        assertAll(
                () -> httpStatusCode_검증(statusCode, OK.value()),
                () -> assertThat(findLineName).isEqualTo(일호선_이름),
                () -> assertThat(namesOfStations).containsExactly(StationFixture.시청역, StationFixture.서울역)
        );
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 지하철 노선의 정보 수정을 요청 하면
     * Then 지하철 노선의 정보 수정은 성공한다.
     */
    @DisplayName("지하철 노선 수정")
    @Test
    void updateLine() {
        //given
        ExtractableResponse<Response> redLine = LineSteps.createLine(일호선);
        String uri = redLine.header("Location");

        //when
        ExtractableResponse<Response> response = LineSteps.updateLine(uri, 이호선);

        int statusCode = response.statusCode();
        String updatedName = response.jsonPath().get("name");

        //then
        assertAll(
                () -> httpStatusCode_검증(statusCode, OK.value()),
                () -> assertThat(updatedName).isEqualTo(이호선_이름)
        );
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 생성한 지하철 노선 삭제를 요청 하면
     * Then 생성한 지하철 노선 삭제가 성공한다.
     */
    @DisplayName("지하철 노선 삭제")
    @Test
    void deleteLine() {
        //given
        ExtractableResponse<Response> redLine = LineSteps.createLine(일호선);
        String uri = redLine.header("Location");

        //when
        ExtractableResponse<Response> response = LineSteps.deleteLine(uri);

        int statusCode = response.statusCode();

        //then
        httpStatusCode_검증(statusCode, NO_CONTENT.value());
    }

}
