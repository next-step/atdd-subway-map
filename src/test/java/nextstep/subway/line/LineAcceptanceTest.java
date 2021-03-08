package nextstep.subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.line.dto.SectionResponse;
import org.assertj.core.groups.Tuple;
import org.graalvm.compiler.replacements.nodes.ArrayEqualsNode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;

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
        LineRequest request = new LineRequest("KANGNAM","green", 201L, 202L, 10);

        // when
        // 지하철_노선_생성_요청
        LineResponse response = this.registerLineHelper(request);

        // then
        // 지하철_노선_생성됨
        this.assertCreateNewLineSuccess(request, response);
    }

    @DisplayName("구간을 추가한다.")
    @Test
    void createSection() {
        // given
        // 지하철 노선
        LineRequest newLine = new LineRequest("KANGNAM","green", 201L, 202L, 10);

        LineResponse createdLine = this.registerLineHelper(newLine);

        // when
        SectionRequest newSection = new SectionRequest(202L, 203L, 5);
        this.registerSectionHelper(createdLine.getId(), newSection);
        // then
    }

    @DisplayName("기존에 존재하는 지하철역 이름으로 지하철역을 생성한다.")
    @Test
    void createStationWithDuplicateName() {
        // given
        // 지하철_노선_등록되어_있음
        final String SAME_LINE_NAME = "KANGNAM";
        final Map newLine = new HashMap<String, String>() {
            {
                put("name", SAME_LINE_NAME);
                put("color", "green");
            }
        };
        this.assertCreateNewLineSuccess(this.registerLineHelper(newLine));

        // when
        final Map DuplicateNameLine = new HashMap<String, String>() {
            {
                put("name", SAME_LINE_NAME);
                put("color", "green");
            }
        };
        ExtractableResponse<Response> response = this.registerLineHelper(DuplicateNameLine);

        // then
        this.assertCreateDuplicatedLineFail(response);
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLines() {
        // given
        // 지하철_노선_등록되어_있음
        // 지하철_노선_등록되어_있음
        final Map KangNamLine = new HashMap<String, String>() {
            {
                put("name", "KANGNAM");
                put("color", "green");
            }
        };
        this.assertCreateNewLineSuccess(this.registerLineHelper(KangNamLine));
        final Map BunDangLine = new HashMap<String, String>() {
            {
                put("name", "BUNDANG");
                put("color", "yellow");
            }
        };
        this.assertCreateNewLineSuccess(this.registerLineHelper(BunDangLine));

        // when
        // 지하철_노선_목록_조회_요청
        ExtractableResponse<Response> response = this.getLinesHelper();

        // then
        // 지하철_노선_목록_응답됨
        // 지하철_노선_목록_포함됨
        this.assertGetLinesSuccess(response);
        this.assertGetLinesContainIn(response, Arrays.asList(KangNamLine, BunDangLine));
    }

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLine() {
        // given
        // 지하철_노선_등록되어_있음
        final Map KangNamLine = new HashMap<String, String>() {
            {
                put("name", "KANGNAM");
                put("color", "green");
            }
        };
        ExtractableResponse<Response> registerLineResponse = this.registerLineHelper(KangNamLine);
        this.assertCreateNewLineSuccess(registerLineResponse);
        LineResponse registeredLine  = registerLineResponse.as(LineResponse.class);

        // when
        // 지하철_노선_조회_요청
        ExtractableResponse<Response> response = this.findLineHelper(registeredLine);

        // then
        // 지하철_노선_응답됨
        this.assertGetLineDetailSuccess(response);
    }

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        // 지하철_노선_등록되어_있음
        final Map KangNamLine = new HashMap<String, String>() {
            {
                put("name", "KANGNAM");
                put("color", "green");
            }
        };
        ExtractableResponse<Response> registerLineResponse = this.registerLineHelper(KangNamLine);
        this.assertCreateNewLineSuccess(registerLineResponse);
        LineResponse registeredLine  = registerLineResponse.as(LineResponse.class);

        // when
        // 지하철_노선_수정_요청
        KangNamLine.put("color", "gold");
        ExtractableResponse<Response> response  = this.updateLineHelper(registeredLine.getId(), KangNamLine);

        // then
        // 지하철_노선_수정됨
        this.assertUpdateLineSuccess(response);
    }

    @DisplayName("지하철 노선을 제거한다.")
    @Test
    void deleteLine() {
        // given
        // 지하철_노선_등록되어_있음
        final Map KangNamLine = new HashMap<String, String>() {
            {
                put("name", "KANGNAM");
                put("color", "green");
            }
        };
        ExtractableResponse<Response> registerLineResponse = this.registerLineHelper(KangNamLine);
        LineResponse registeredLine  = registerLineResponse.as(LineResponse.class);

        // when
        // 지하철_노선_제거_요청
        ExtractableResponse<Response> response  = this.deleteLineHelper(registeredLine.getId());

        // then
        // 지하철_노선_삭제됨
        this.assertDeleteLineSuccess(response);
    }

    private void assertCreateNewLineSuccess(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.as(LineResponse.class)).isNotNull();
    }

    private void assertCreateNewLineSuccess(LineRequest request, LineResponse response) {
        assertThat(response).isNotNull();
        assertThat(request.getColor()).isEqualTo(response.getColor());
        assertThat(request.getName()).isEqualTo(response.getName());
        assertThat(request.getDistance()).isEqualTo(response.getDistance());
        assertThat(request.getDownStationId()).isEqualTo(response.getDownStationId());
        assertThat(request.getUpStationId()).isEqualTo(response.getUpStationId());
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

    private void assertGetLinesContainIn(ExtractableResponse<Response> response, final List<Map> expectedLines) {
        // NOTE: https://stackoverflow.com/questions/15531767/rest-assured-generic-list-deserialization
        // List<LineResponse> responseLines = response.as(LineResponse[].class); <- Not Working
        List<Tuple> resultLines = response.jsonPath().
                getList(".", LineResponse.class).
                stream().
                map(line -> new Tuple(line.getName(), line.getColor())).
                collect(Collectors.toList());
        List<Tuple> inputLines = expectedLines.stream().
                map(line -> new Tuple(line.get("name"), line.get("color"))).
                collect(Collectors.toList());
        assertThat(resultLines).isEqualTo(inputLines);

    }

    private ExtractableResponse<Response> getLinesHelper() {
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

    private ExtractableResponse<Response> registerLineHelper(final Map<String, String> line) {
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

    private LineResponse registerLineHelper(final LineRequest request) {
        return RestAssured.given().log().all().
                    body(request).
                    contentType(MediaType.APPLICATION_JSON_VALUE).
                when().
                    post("/lines").
                then().log().all().
                    extract().
                    as(LineResponse.class);
    }

    private  ExtractableResponse<Response> findLineHelper(final LineResponse InputLine) {
        return RestAssured.
                given().
                    log().all().
                    contentType(MediaType.APPLICATION_JSON_VALUE).
                when().
                    pathParam("lineId", InputLine.getId()).
                    get("/lines/{lineId}").
                then().
                    log().all().
                    extract();
    }

    private  ExtractableResponse<Response> updateLineHelper(final Long lineId, final Map<String, String> line) {
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

    private  ExtractableResponse<Response> deleteLineHelper(final Long lineId) {
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

    private SectionResponse registerSectionHelper(final Long lineId, final SectionRequest newSection) {
        return RestAssured.given().log().all().
                    contentType(MediaType.APPLICATION_JSON_VALUE).
                when().
                    pathParam("lineId", lineId).
                    post("/lines/{lineId}/sections").
                then().
                    log().all().
                    extract().
                    as(SectionResponse.class);
    }

}
