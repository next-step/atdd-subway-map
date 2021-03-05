package nextstep.subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.utils.DatabaseCleanup;
import org.aspectj.lang.annotation.Before;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {
    @Autowired
    private DatabaseCleanup databaseCleanup;

    private final Map KANGNAM = new HashMap<String, String>() {
        {
            put("name", "KANGNAM");
            put("color", "green");
        }
    };
    private final Map BUNDANG = new HashMap<String, String>() {
        {
            put("name", "BUNDANG");
            put("color", "yellow");
        }
    };

    @BeforeEach
    void cleanupDatabase() {
        databaseCleanup.execute();
    }

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // when
        // 지하철_노선_생성_요청
        ExtractableResponse<Response> response = this.registerLineHelper(this.KANGNAM);
        // then
        // 지하철_노선_생성됨
        this.assertStatusCode(response, HttpStatus.CREATED);
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLines() {
        // given
        // 지하철_노선_등록되어_있음
        // 지하철_노선_등록되어_있음
        this.registerLineHelper(this.KANGNAM);
        this.registerLineHelper(this.BUNDANG);

        // when
        // 지하철_노선_목록_조회_요청
        ExtractableResponse<Response> response = this.getLinesHelper();

        // then
        // 지하철_노선_목록_응답됨
        // 지하철_노선_목록_포함됨
        this.assertStatusCode(response, HttpStatus.OK);
        this.assertContainIn(response, this.KANGNAM);
        this.assertContainIn(response, this.BUNDANG);
    }

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLine() {
        // given
        // 지하철_노선_등록되어_있음
        ExtractableResponse<Response> registerLineResponse = this.registerLineHelper(this.KANGNAM);
        LineResponse registeredLine  = registerLineResponse.jsonPath().getObject("", LineResponse.class);

        // when
        // 지하철_노선_조회_요청
        ExtractableResponse<Response> response = this.findLineHelper(registeredLine);

        // then
        // 지하철_노선_응답됨
        this.assertStatusCode(response, HttpStatus.OK);
        this.assertEqualTo(response, this.KANGNAM);
    }

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        // 지하철_노선_등록되어_있음
        ExtractableResponse<Response> registerLineResponse = this.registerLineHelper(this.KANGNAM);
        LineResponse registeredLine  = registerLineResponse.jsonPath().getObject("", LineResponse.class);

        // when
        // 지하철_노선_수정_요청
        KANGNAM.put("color", "gold");
        ExtractableResponse<Response> response  = this.updateLineHelper(registeredLine.getId(), this.KANGNAM);

        // then
        // 지하철_노선_수정됨
        this.assertStatusCode(response, HttpStatus.OK);
    }

    @DisplayName("지하철 노선을 제거한다.")
    @Test
    void deleteLine() {
        // given
        // 지하철_노선_등록되어_있음
        ExtractableResponse<Response> registerLineResponse = this.registerLineHelper(this.KANGNAM);
        LineResponse registeredLine  = registerLineResponse.jsonPath().getObject("", LineResponse.class);

        // when
        // 지하철_노선_제거_요청
        ExtractableResponse<Response> response  = this.deleteLineHelper(registeredLine.getId());

        // then
        // 지하철_노선_삭제됨
        this.assertStatusCode(response, HttpStatus.NO_CONTENT);
    }

    private ExtractableResponse<Response> registerLineHelper(final Map<String, String> line) {
        return RestAssured.given().log().all().
                body(line).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                when().
                post("/lines").
                then().
                log().all().
                extract();
    }

    private void assertStatusCode(ExtractableResponse<Response> response, final HttpStatus expectedStatus) {
        assertThat(response.statusCode()).isEqualTo(expectedStatus.value());
    }

    private ExtractableResponse<Response> getLinesHelper() {
        return RestAssured.given().log().all().
                contentType(MediaType.APPLICATION_JSON_VALUE).
                when().
                get("/lines").
                then().
                log().all().
                extract();
    }

    private void assertContainIn(ExtractableResponse<Response> response, final Map expectedLine) {
        List<LineResponse> responseLines = response.jsonPath().getList("", LineResponse.class);
        assertThat(
                responseLines.stream().
                        anyMatch(line -> line.getName().equals(expectedLine.get("name"))
                                && line.getColor().equals(expectedLine.get("color")))

        ).isTrue();
    }

    private void assertEqualTo(ExtractableResponse<Response> response, final Map expectedLine) {
        LineResponse responseLines = response.jsonPath().getObject("", LineResponse.class);
        assertThat(responseLines.getName()).isEqualTo(expectedLine.get("name"));
        assertThat(responseLines.getColor()).isEqualTo(expectedLine.get("color"));
    }

    private  ExtractableResponse<Response> findLineHelper(final LineResponse InputLine) {
        return RestAssured.given().log().all().
                contentType(MediaType.APPLICATION_JSON_VALUE).
                when().
                get("/lines/"+InputLine.getId()).
                then().
                log().all().
                extract();
    }

    private  ExtractableResponse<Response> updateLineHelper(final Long lineId, final Map<String, String> line) {
        return RestAssured.given().log().all().
                contentType(MediaType.APPLICATION_JSON_VALUE).
                body(line).
                when().
                patch("/lines/"+lineId).
                then().
                log().all().
                extract();
    }

    private  ExtractableResponse<Response> deleteLineHelper(final Long lineId) {
        return RestAssured.given().log().all().
                contentType(MediaType.APPLICATION_JSON_VALUE).
                when().
                delete("/lines/"+lineId).
                then().
                log().all().
                extract();
    }
}
