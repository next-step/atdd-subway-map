package nextstep.subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.*;
import java.util.stream.Collectors;


import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {
    /**
     * 테스트 프로그램 가이드
     * 1. Four Phase Test((http://xunitpatterns.com/Four%20Phase%20Test.html))
     *    Test Reader 에게 혼란 제거 하도록 작성 e.g) TestSuite의 Convention을 그대로 사용, 함수별 독립성
     * 2. GivenWhenThen(https://martinfowler.com/bliki/GivenWhenThen.html)
     *    Ex) Feature: Subway Line Management at Subway Map
     *         Scenario: User register a new created subway line
     *          Given I have a new created line information
     *
     *          When I register a new line information at subway map system
     *
     *          Then I should get the id of new line
     */

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // given
        // 지하철 노선
        final Map newLine = createLineMapHelper("KangNam Line","green");
        final Map upStation = StationAcceptanceTest.createStationInputHelper("강남역");
        final Map downStation = StationAcceptanceTest.createStationInputHelper("서초역");
        final int distance = 10;

        // when
        // 지하철_노선_생성_요청
        ExtractableResponse<Response> response = registerLineWithStationsHelper(newLine, upStation, downStation, distance);

        // then
        // 지하철_노선_생성됨
        assertCreateNewLineSuccess(response);
    }

    @DisplayName("구간을 추가한다.")
    @Test
    void createSection() {
        // given
        // 지하철 노선
        LineResponse createdLineResponse  =
                registerLineWithStationsHelper("KangName Line", "green", "강남역","서초역", 10)
                .as(LineResponse.class);

        final Map newDownStation = StationAcceptanceTest.createStationInputHelper("방배역");
        StationResponse newDownStationResponse = StationAcceptanceTest.createStationHelper(newDownStation).as(StationResponse.class);

        // when
        Map<String, String> newSection = new HashMap<>();
        newSection.put("upStationId", createdLineResponse.getDownStationId().toString());
        newSection.put("downStationId",newDownStationResponse.getId().toString());
        newSection.put("distance", "5");
        ExtractableResponse<Response> newSectionResponse = registerSectionHelper(createdLineResponse.getId(), newSection);

        // then
        assertAppendNewSectioSuccess(newSectionResponse);

    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLines() {
        // given
        // 지하철_노선_등록되어_있음
        // 지하철_노선_등록되어_있음
        LineResponse kangNamLineResponse  =
                registerLineWithStationsHelper("KangName Line", "green", "강남역","서초역", 10)
                        .as(LineResponse.class);
        LineResponse bunDangLineResponse  =
                registerLineWithStationsHelper("Bundang Line", "yellow", "양재역","분당역", 20)
                        .as(LineResponse.class);

        // when역
        // 지하철_노선_목록_조회_요청
        ExtractableResponse<Response> response = getLinesHelper();

        // then
        // 지하철_노선_목록_응답됨
        // 지하철_노선_목록_포함됨
        this.assertGetLinesSuccess(response);
        this.assertGetLinesContainIn(response, Arrays.asList(kangNamLineResponse, bunDangLineResponse));
    }

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLine() {
        // given
        // 지하철_노선_등록되어_있음
        LineResponse registeredLine  =
                registerLineWithStationsHelper("KangName Line", "green", "강남역","서초역", 10)
                        .as(LineResponse.class);

        // when
        // 지하철_노선_조회_요청
        ExtractableResponse<Response> response = findLineHelper(registeredLine);

        // then
        // 지하철_노선_응답됨
        assertGetLineDetailSuccess(response);
    }

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        // 지하철_노선_등록되어_있음
        LineResponse registeredLine  =
                registerLineWithStationsHelper("KangName Line", "green", "강남역","서초역", 10)
                        .as(LineResponse.class);

        // when
        // 지하철_노선_생성_요청
        Map<String, String> updateRequestMap = createLineAndStationMapHelper(
                registeredLine.getName(),
                registeredLine.getColor(),
                registeredLine.getUpStationId(),
                registeredLine.getDownStationId(),
                registeredLine.getDistance());
        updateRequestMap.put("color", "yellogreen");
        ExtractableResponse<Response> response  = updateLineHelper(registeredLine.getId(), updateRequestMap);

        // then
        // 지하철_노선_수정됨
        assertUpdateLineSuccess(response);
    }

    @DisplayName("지하철 노선을 제거한다.")
    @Test
    void deleteLine() {
        // given
        // 지하철_노선_등록되어_있음
        LineResponse registeredLine  =
                registerLineWithStationsHelper("KangName Line", "green", "강남역","서초역", 10)
                        .as(LineResponse.class);

        // when
        // 지하철_노선_제거_요청
        ExtractableResponse<Response> response  = deleteLineHelper(registeredLine.getId());

        // then
        // 지하철_노선_삭제됨
        this.assertDeleteLineSuccess(response);
    }

    private void assertAppendNewSectioSuccess(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private void assertCreateNewLineSuccess(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.as(LineResponse.class)).isNotNull();
    }

    private void assertCreateDuplicatedLineFail(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private void assertGetLinesSuccess(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        // NOTE: 어떤 값을 비교하는게 좋을까?
    }

    private void assertGetLineDetailSuccess(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.as(LineResponse.class)).isNotNull();
    }

    private void assertUpdateLineSuccess(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void assertDeleteLineSuccess(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void assertGetLinesContainIn(ExtractableResponse<Response> response, final List<LineResponse> expectedLines) {
        // NOTE: https://stackoverflow.com/questions/15531767/rest-assured-generic-list-deserialization
        // List<LineResponse> responseLines = response.as(LineResponse[].class); <- Not Working
        List<Tuple> resultLines = response.jsonPath().
                getList(".", LineResponse.class).
                stream().
                map(line -> new Tuple(line.getName(), line.getColor())).
                collect(Collectors.toList());
        List<Tuple> inputLines = expectedLines.stream().
                map(line -> new Tuple(line.getName(), line.getColor())).
                collect(Collectors.toList());
        assertThat(resultLines).isEqualTo(inputLines);

    }

    public static  ExtractableResponse<Response> getLinesHelper() {
        return RestAssured.
                given().
                    log().all().
                    contentType(MediaType.APPLICATION_JSON_VALUE).
                when().
                    get("/lines").
                then().
                    log().all().
                    extract();
    }

    public static ExtractableResponse<Response> registerLineHelper(final Map<String, String> line) {
        return RestAssured.
                given().
                    log().all().
                    body(line).
                    contentType(MediaType.APPLICATION_JSON_VALUE).
                when().
                    post("/lines").
                then().
                    log().all().
                    extract();
    }

    public static ExtractableResponse<Response> registerLineWithStationsHelper(
            final Map<String, String> newLine,
            final Map<String, String> upStation,
            final Map<String, String> downStation,
            final int distance) {
        StationResponse upStationResponse = StationAcceptanceTest.createStationHelper(upStation).as(StationResponse.class);
        StationResponse downStationResponse = StationAcceptanceTest.createStationHelper(downStation).as(StationResponse.class);
        newLine.put("upStationId", upStationResponse.getId().toString());
        newLine.put("downStationId", downStationResponse.getId().toString());
        newLine.put("distance", String.valueOf(distance));
        return registerLineHelper(newLine);
    }


    public static ExtractableResponse<Response> registerLineWithStationsHelper(
            final String lineName,
            final String lineColor,
            final String upStationName,
            final String downStationName,
            final int distance) {
        final Map newLine = createLineMapHelper(lineName,lineColor);
        final Map upStation = StationAcceptanceTest.createStationInputHelper(upStationName);
        final Map downStation = StationAcceptanceTest.createStationInputHelper(downStationName);
        return registerLineWithStationsHelper(newLine, upStation, downStation, distance);
    }

    public static  ExtractableResponse<Response> findLineHelper(final LineResponse inputLine) {
        return RestAssured.
                given().
                    log().all().
                    contentType(MediaType.APPLICATION_JSON_VALUE).
                when().
                    pathParam("lineId", inputLine.getId()).
                    get("/lines/{lineId}").
                then().
                    log().all().
                    extract();
    }

    public static ExtractableResponse<Response> updateLineHelper(final Long lineId, final Map<String, String> line) {
        return RestAssured.
                given().
                    log().all().
                    contentType(MediaType.APPLICATION_JSON_VALUE).
                    body(line).
                when().
                    pathParam("lineId", lineId).
                    patch("/lines/{lineId}").
                then().
                    log().all().
                    extract();
    }

    public static ExtractableResponse<Response> deleteLineHelper(final Long lineId) {
        return RestAssured.
                given().
                    log().all().
                    contentType(MediaType.APPLICATION_JSON_VALUE).
                when().
                    pathParam("lineId", lineId).
                    delete("/lines/{lineId}").
                then().
                    log().all().
                    extract();
    }


    public static ExtractableResponse<Response> registerSectionHelper(final Long lineId, final Map newSection) {
        return RestAssured.given().log().all().
                    contentType(MediaType.APPLICATION_JSON_VALUE).
                when().
                    body(newSection).
                    pathParam("lineId", lineId).
                    post("/lines/{lineId}/sections").
                then().
                    log().all().
                    extract();
    }

    public static Map createLineAndStationMapHelper(
            final String lineName,
            final String lineColor,
            final Long upStationId,
            final Long downStationId,
            final int distance){
        return new HashMap<String, String>() {
            {
                put("name", lineName);
                put("color", lineColor);
                put("upStationId", String.valueOf(upStationId));
                put("downStationId", String.valueOf(downStationId));
                put("distance", String.valueOf(distance));
            }
        };
    }
    public static Map createLineMapHelper(
            final String lineName,
            final String lineColor){
        return new HashMap<String, String>() {
            {
                put("name", lineName);
                put("color", lineColor);
            }
        };
    }
}
