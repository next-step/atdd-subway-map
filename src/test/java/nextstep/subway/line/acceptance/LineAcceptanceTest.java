package nextstep.subway.line.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // when
        // 지하철_노선_생성_요청
        ExtractableResponse<Response> response = this.requestCreateLine("신분당선", "bg-red-600",
                LocalTime.of(05, 30), LocalTime.of(23, 30), 5);

        // then
        // 지하철_노선_생성됨
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    @DisplayName("기존에 존재하는 지하철 노선 이름으로 지하철 노선을 생성한다.")
    @Test
    void createLine2() {
        // given
        // 지하철_노선_등록되어_있음
        String name = "신분당선";
        this.requestCreateLine(name, "bg-red-600",
                LocalTime.of(05, 30), LocalTime.of(23, 30), 5);

        // when
        // 지하철_노선_생성_요청
        ExtractableResponse<Response> response = this.requestCreateLine(name, "bg-red-600",
                LocalTime.of(05, 30), LocalTime.of(23, 30), 5);

        // then
        // 지하철_노선_생성_실패됨
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CONFLICT.value()); // FIXME it returns 400
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLines() {
        // given
        // 지하철_노선_등록되어_있음
        this.requestCreateLine("신분당선", "bg-red-600",
                LocalTime.of(05, 30), LocalTime.of(23, 30), 5);
        // 지하철_노선_등록되어_있음
        this.requestCreateLine("1호선", "bg-blue-600",
                LocalTime.of(05, 30), LocalTime.of(23, 30), 5);

        // when
        // 지하철_노선_목록_조회_요청
        ExtractableResponse<Response> response = RestAssured.given().log().all().
                accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                get("/lines").
                then().
                log().all().
                extract();

        // then
        // 지하철_노선_목록_응답됨
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        // 지하철_노선_목록_포함됨
        assertThat(response.body().asString()).contains("신분당선").contains("1호선");
    }

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLine() {
        // given
        // 지하철_노선_등록되어_있음
        ExtractableResponse<Response> createResponse = this.requestCreateLine("신분당선", "bg-red-600",
                LocalTime.of(05, 30), LocalTime.of(23, 30), 5);

        // when
        // 지하철_노선_조회_요청
        String uri = createResponse.header("Location");

        ExtractableResponse<Response> response = RestAssured.given().log().all().
                accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                get(uri).
                then().
                log().all().
                extract();

        // then
        // 지하철_노선_응답됨
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.as(LineResponse.class)).isNotNull();
    }

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        // 지하철_노선_등록되어_있음
        ExtractableResponse<Response> createResponse = this.requestCreateLine("신분당선", "bg-red-600",
                LocalTime.of(05, 30), LocalTime.of(23, 30), 5);

        // when
        // 지하철_노선_수정_요청
        String uri = createResponse.header("Location");
        Long id = Long.parseLong(uri.split("/lines/")[1]);
        ExtractableResponse<Response> response = this.requestUpdateLine(id, "1호선", "bg-blue-600",
                LocalTime.of(05, 30), LocalTime.of(23, 30), 5);

        // then
        // 지하철_노선_수정됨
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("지하철 노선을 제거한다.")
    @Test
    void deleteLine() {
        // given
        // 지하철_노선_등록되어_있음
        ExtractableResponse<Response> createResponse = this.requestCreateLine("신분당선", "bg-red-600",
                LocalTime.of(05, 30), LocalTime.of(23, 30), 5);

        // when
        // 지하철_노선_제거_요청
        String uri = createResponse.header("Location");
        Long id = Long.parseLong(uri.split("/lines/")[1]);
        ExtractableResponse<Response> response = this.requestDeleteLine(id);

        // then
        // 지하철_노선_삭제됨
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private ExtractableResponse<Response> requestCreateLine(String name, String color,
                                                            LocalTime startTime, LocalTime endTime, int intervalTime) {
        Map<String, String> params = this.lineRequestParams(name, color, startTime, endTime, intervalTime);

        return RestAssured.given().log().all().
                contentType(MediaType.APPLICATION_JSON_VALUE).
                body(params).
                when().
                post("/lines").
                then().
                log().all().
                extract();
    }

    private ExtractableResponse<Response> requestUpdateLine(Long id, String name, String color,
                                                            LocalTime startTime, LocalTime endTime, int intervalTime) {
        Map<String, String> params = this.lineRequestParams(name, color, startTime, endTime, intervalTime);

        return RestAssured.given().log().all().
                contentType(MediaType.APPLICATION_JSON_VALUE).
                body(params).
                when().
                put("/lines/" + id).
                then().
                log().all().
                extract();
    }

    private ExtractableResponse<Response> requestDeleteLine(Long id) {
        return RestAssured.given().log().all().
                when().
                delete("/lines/" + id).
                then().
                log().all().
                extract();
    }

    private Map<String, String> lineRequestParams(String name, String color,
                                                  LocalTime startTime, LocalTime endTime, int intervalTime) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        params.put("startTime", startTime.format(DateTimeFormatter.ISO_TIME));
        params.put("endTime", endTime.format(DateTimeFormatter.ISO_TIME));
        params.put("intervalTime", String.valueOf(intervalTime));
        return params;
    }
}
