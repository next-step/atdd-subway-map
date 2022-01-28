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

    @DisplayName("지하철 노선 생성")
    @Test
    void createLine() {
        //given
        //when
        int statusCode = 노선_생성_요청_응답_HttpStatusCode(일호선);

        //then
        httpStatusCode_검증(statusCode, CREATED.value());

    }

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

    @DisplayName("지하철 노선 목록 조회")
    @Test
    void getLines() {
        //given
        LineSteps.createLine(일호선);
        LineSteps.createLine(이호선);

        //when
        ExtractableResponse<Response> 노선_목록_조회_응답 = LineSteps.findLines();

        int statusCode = 노선_목록_조회_응답.statusCode();
        List<String> namesOfLines = 노선_목록_조회_응답.jsonPath().getList("name");

        //then
        assertAll(
                () -> httpStatusCode_검증(statusCode, OK.value()),
                () -> assertThat(namesOfLines).containsExactly(일호선_이름, 이호선_이름)
        );

    }

    @DisplayName("지하철 노선 조회")
    @Test
    void getLine() {
        //given
        ExtractableResponse<Response> 노선_생성_응답 = LineSteps.createLine(일호선);
        String uri = 노선_생성_응답.header("Location");

        //when
        ExtractableResponse<Response> 노선_조회_응답 = LineSteps.findLines(uri);

        int statusCode = 노선_조회_응답.statusCode();
        String findLineName = 노선_조회_응답.jsonPath().get("name");
        List<String> namesOfStations = 노선_조회_응답.jsonPath().getList("stations.name");

        //then
        assertAll(
                () -> httpStatusCode_검증(statusCode, OK.value()),
                () -> assertThat(findLineName).isEqualTo(일호선_이름),
                () -> assertThat(namesOfStations).containsExactly(StationFixture.시청역, StationFixture.서울역)
        );
    }

    @DisplayName("지하철 노선 수정")
    @Test
    void updateLine() {
        //given
        ExtractableResponse<Response> 노선_생성_응답 = LineSteps.createLine(일호선);
        String uri = 노선_생성_응답.header("Location");

        //when
        ExtractableResponse<Response> 노선_수정_응답 = LineSteps.updateLine(uri, 이호선);

        int statusCode = 노선_수정_응답.statusCode();
        String updatedName = 노선_수정_응답.jsonPath().get("name");

        //then
        assertAll(
                () -> httpStatusCode_검증(statusCode, OK.value()),
                () -> assertThat(updatedName).isEqualTo(이호선_이름)
        );
    }

    @DisplayName("지하철 노선 삭제")
    @Test
    void deleteLine() {
        //given
        ExtractableResponse<Response> 노선_생성_응답 = LineSteps.createLine(일호선);
        String uri = 노선_생성_응답.header("Location");

        //when
        ExtractableResponse<Response> 노선_삭제_응답 = LineSteps.deleteLine(uri);

        int statusCode = 노선_삭제_응답.statusCode();

        //then
        httpStatusCode_검증(statusCode, NO_CONTENT.value());
    }

}
