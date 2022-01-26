package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.dto.LineCreateResponse;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

import static nextstep.subway.acceptance.LineSteps.*;
import static nextstep.subway.acceptance.StationSteps.*;
import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("지하철 노선 관리 기능")
class LineAcceptanceTest extends AcceptanceTest {

    @BeforeEach
    void init() {
        지하철역들_생성_요청(5);
    }
    /**
     * When 지하철 노선 생성을 요청 하면
     * Then 지하철 노선 생성이 성공한다.
     */
    @DisplayName("지하철 노선 생성")
    @Test
    void createLine() {
        // given
        String color = "bg-red-600";
        String name = "신분당선";
        String upStationId = "1";
        String downStationId = "4";
        String distance = "10";

        // when
        ExtractableResponse<Response> createResponse = 지하철_노선_생성_요청(
                name,
                color,
                upStationId,
                downStationId,
                distance);

        // then
        지하철_노선_생성_응답_검증(color, name, createResponse);
    }

    /**
     * Given 지하철 노선의 상행 또는 하행이 존재하지 않는 역이고
     * When 지하철 노선 생성을 요청 하면
     * Then 지하철 노선 생성이 실패한다.
     */
    @DisplayName("생성할 노선의 지하철 역이 존재하지 않는 지하철 역인 지하철 노선 생성")
    @CsvSource(delimiter = ':', value = {"1:6", "6:1"})
    @ParameterizedTest
    void createLineWithNotExistUpStation(String upStationId, String downStationId) {
        // when
        ExtractableResponse<Response> createResponse = 지하철_노선_생성_요청(
                "신분당선",
                "bg-red-600",
                upStationId,
                downStationId,
                "10");

        //then
        실패_응답_검증(createResponse.statusCode());
    }

    /**
     * Given 지하철역 생성을 요청 하고
     * When 같은 이름으로 지하철역 생성을 요청 하면
     * Then 지하철역 생성이 실패한다.
     */
    @DisplayName("지하철 노선 중복이름 생성")
    @Test
    void createDuplicateNameLine() {
        // given
        지하철_노선_생성_요청(
                "신분당선",
                "bg-red-600",
                "1",
                "4",
                "10");

        // when
        ExtractableResponse<Response> createResponse = 지하철_노선_생성_요청(
                "신분당선",
                "bg-red-600",
                "2",
                "5",
                "3");

        // then
        실패_응답_검증(createResponse.statusCode());
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 지하철 노선에 구간을 추가하고
     * THen 지하철 노선에 구간 추가가 성공한다.
     */
    @DisplayName("지하철 노선 구간 추가")
    @Test
    void addSection() {
        ExtractableResponse<Response> createResponse = 지하철_노선_생성_요청(
                "신분당선",
                "bg-red-600",
                "1",
                "4",
                "10");

        //when
        String uri = createResponse.header("Location");
        ExtractableResponse<Response> response = 지하철_노선_구간_추가_요청(uri, "4", "5", "1");

        //then
        성공_응답_검증(response.statusCode());
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 구간의 상행역이 현재 등록되어있는 하행 종점역이 아닌 구간을 노선에 추가하고
     * THen 지하철 노선에 구간 추가가 실패한다.
     */
    @DisplayName("지하철 노선에 구간의 상행역이 현재 등록되어있는 하행 종점역이 아닌 구간을 노선에 추가")
    @Test
    void addSectionFailedByUpStation() {
        ExtractableResponse<Response> createResponse = 지하철_노선_생성_요청(
                "신분당선",
                "bg-red-600",
                "1",
                "4",
                "10");

        //when
        String uri = createResponse.header("Location");
        ExtractableResponse<Response> response = 지하철_노선_구간_추가_요청(uri, "3", "5", "10");

        //then
        실패_응답_검증(response.statusCode());
    }



    /**
     * Given 지하철 노선을 생성하고
     * When 구간의 하행역이 현재 노선에 등록되어있는 역인 구간을 노선에 추가하고
     * THen 지하철 노선에 구간 추가가 실패한다.
     */
    @DisplayName("지하철 노선에 구간의 상행역이 현재 등록되어있는 하행 종점역이 아닌 구간을 노선에 추가")
    @Test
    void addSectionFailedByDownStation() {
        ExtractableResponse<Response> createResponse = 지하철_노선_생성_요청(
                "신분당선",
                "bg-red-600",
                "1",
                "4",
                "10");

        //when
        String uri = createResponse.header("Location");
        ExtractableResponse<Response> response =
                지하철_노선_구간_추가_요청(uri, "5", "2", "10");

        //then
        실패_응답_검증(response.statusCode());
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
        // given
        ExtractableResponse<Response> createResponse1 = 지하철_노선_생성_요청(
                "신분당선",
                "bg-red-600",
                "1",
                "3",
                "5");

        ExtractableResponse<Response> createResponse2 = 지하철_노선_생성_요청(
                "2호선",
                "bg-green-600",
                "2",
                "5",
                "5");

        //when
        LineCreateResponse firstLine = createResponse1.body().as(LineCreateResponse.class);
        LineCreateResponse secondLine = createResponse2.body().as(LineCreateResponse.class);
        String url = LineSteps.DEFAULT_PATH;
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(url);

        //then
        지하철_목록_응답_검증(firstLine, secondLine, response);
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 생성한 지하철 노선 조회를 요청 하면
     * Then 생성한 지하철 노선을 응답받는다
     */
    @DisplayName("지하철 노선 조회")
    @Test
    void getLine() {
        // given
        ExtractableResponse<Response> createResponse = 지하철_노선_생성_요청(
                "신분당선",
                "bg-red-600",
                "1",
                "4",
                "10");
        LineCreateResponse lineCreateResponse = createResponse.body().as(LineCreateResponse.class);
        String uri = createResponse.header("Location");
        지하철_노선_구간_추가_요청(uri, "4", "5", "3");

        // when
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(uri);

        // then
        List<Station> stations = Arrays.asList(new Station(1L), new Station(4L), new Station(5L));
        지하철_노선_조회_검증(lineCreateResponse, response, stations);
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 지하철 노선의 정보 수정을 요청 하면
     * Then 지하철 노선의 정보 수정은 성공한다.
     */
    @DisplayName("지하철 노선 수정")
    @Test
    void updateLine() {
        // given
        ExtractableResponse<Response> createResponse = 지하철_노선_생성_요청(
                "신분당선",
                "bg-red-600",
                "1",
                "2",
                "5");

        // when
        String uri = createResponse.header("Location");
        ExtractableResponse<Response> response = 지하철_노선_수정_요청("구분당선", "bg-blue-600", uri);

        // then
        성공_응답_검증(response.statusCode());
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 생성한 지하철 노선 삭제를 요청 하면
     * Then 생성한 지하철 노선 삭제가 성공한다.
     */
    @DisplayName("지하철 노선 삭제")
    @Test
    void deleteLine() {
        // given
        ExtractableResponse<Response> createResponse = 지하철_노선_생성_요청(
                "신분당선",
                "bg-red-600",
                "1",
                "2",
                "5");

        // when
        String uri = createResponse.header("Location");
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when()
                .delete(uri)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * GIven 노선에 구간의 추가를 요청하고
     * When 생성한 지하철 노선에 구간의 삭제를 요청 하면
     * Then 생성한 지하철 노선에 구간의 삭제가 성공한다.
     */
    @DisplayName("지하철 노선의 구간 삭제")
    @Test
    void deleteSection() {
        // given
        ExtractableResponse<Response> createResponse = 지하철_노선_생성_요청(
                "신분당선",
                "bg-red-600",
                "1",
                "4",
                "10");

        String uri = createResponse.header("Location");
        String lastDownStationId = "5";
        지하철_노선_구간_추가_요청(uri, "4", lastDownStationId, "2");

        // when
        uri = createResponse.header("Location");
        ExtractableResponse<Response> response = 지하철_노선_구간_삭제_요청(lastDownStationId, uri);

        //then
        성공_응답_검증(response.statusCode());
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 생성한 지하철 노선에 구간의 삭제를 요청 하면
     * Then 생성한 지하철 노선에 구간의 삭제가 실패한다.
     */
    @DisplayName("지하철 노선의 구간이 하나일때 삭제")
    @Test
    void deleteSectionFailed() {
        // given
        String downStationId = "4";
        ExtractableResponse<Response> createResponse = 지하철_노선_생성_요청(
                "신분당선",
                "bg-red-600",
                "1",
                downStationId,
                "10");

        // when
        String uri = createResponse.header("Location");
        ExtractableResponse<Response> response = 지하철_노선_구간_삭제_요청(downStationId, uri);

        //then
        실패_응답_검증(response.statusCode());
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * Given 노선에 구간의 추가를 요청하고
     * When 생성한 지하철 노선에 마지막 구간이 아닌 다른 구간의 지하철역을 삭제요청 하면
     * Then 생성한 지하철 노선에 구간의 삭제가 실패한다.
     */
    @DisplayName("지하철 노선의 마지막 구간의 하행이 아닌 다른 구간의 지하철역을 삭제")
    @Test
    void deleteSectionFailedByNotLastDownStation() {
        // given
        String downStationId = "4";
        ExtractableResponse<Response> createResponse = 지하철_노선_생성_요청(
                "신분당선",
                "bg-red-600",
                "1",
                downStationId,
                "10");

        String uri = createResponse.header("Location");
        지하철_노선_구간_추가_요청(uri, "4", "5", "10");

        // when
        ExtractableResponse<Response> response = 지하철_노선_구간_삭제_요청(downStationId, uri);

        //then
        실패_응답_검증(response.statusCode());
    }
}