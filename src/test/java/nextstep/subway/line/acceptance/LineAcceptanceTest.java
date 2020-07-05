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

    ExtractableResponse<Response> 새로운_신분당선_노선을_생성한다() {
        // when
        // 지하철_노선_생성_요청
        Map<String, String> params = new HashMap<>();
        params.put("name", "신분당선");
        params.put("color", "bg-red-600");
        params.put("startTime", LocalTime.of(05, 30).format(DateTimeFormatter.ISO_TIME));
        params.put("endTime", LocalTime.of(23, 30).format(DateTimeFormatter.ISO_TIME));
        params.put("intervalTime", "5");

        ExtractableResponse<Response> response = RestAssured.given().log().all().
            contentType(MediaType.APPLICATION_JSON_VALUE).
            body(params).
            when().
            post("/lines").
            then().
            log().all().
            extract();

        return response;
    }

    ExtractableResponse<Response> 새로운_김포골드라인_노선을_생성한다() {
        // when
        // 지하철_노선_생성_요청
        Map<String, String> params = new HashMap<>();
        params.put("name", "김포골드라인");
        params.put("color", "bg-gold-600");
        params.put("startTime", LocalTime.of(05, 30).format(DateTimeFormatter.ISO_TIME));
        params.put("endTime", LocalTime.of(23, 30).format(DateTimeFormatter.ISO_TIME));
        params.put("intervalTime", "5");

        ExtractableResponse<Response> response = RestAssured.given().log().all().
            contentType(MediaType.APPLICATION_JSON_VALUE).
            body(params).
            when().
            post("/lines").
            then().
            log().all().
            extract();

        return response;
    }

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // when
        // 지하철_노선_생성_요청
        ExtractableResponse<Response> response = 새로운_신분당선_노선을_생성한다();

        // then
        // 지하철_노선_생성됨
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    @DisplayName("기존에 존재하는 지하철 노선 이름으로 지하철 노선을 생성한다.")
    @Test
    void createLine2() {
        // given
        새로운_신분당선_노선을_생성한다();

        // when
        // 지하철_노선_생성_요청
        final ExtractableResponse<Response> response = 새로운_신분당선_노선을_생성한다();

        // then
        // 지하철_노선_생성_실패됨
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLines() {
        // given
        새로운_신분당선_노선을_생성한다();
        새로운_김포골드라인_노선을_생성한다();

        // when
        final ExtractableResponse<Response> response = RestAssured.given()
            .log()
            .all()
            .when()
            .get("/lines")
            .then()
            .log()
            .all()
            .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.body()).isNotNull();
        assertThat(response.body().jsonPath().get().toString()).contains("신분당선", "김포골드라인");
    }

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLine() {
        // given
        // 지하철_노선_등록되어_있음
        Map<String, String> params = new HashMap<>();
        params.put("name", "신분당선");
        params.put("color", "bg-red-600");
        params.put("startTime", LocalTime.of(05, 30).format(DateTimeFormatter.ISO_TIME));
        params.put("endTime", LocalTime.of(23, 30).format(DateTimeFormatter.ISO_TIME));
        params.put("intervalTime", "5");

        ExtractableResponse<Response> createResponse = RestAssured.given().log().all().
                contentType(MediaType.APPLICATION_JSON_VALUE).
                body(params).
                when().
                post("/lines").
                then().
                log().all().
                extract();

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
        ExtractableResponse<Response> 신분당선_생성_응답 = 새로운_신분당선_노선을_생성한다();

        // when
        String url = 신분당선_생성_응답.header("Location");
        Map<String, String> params = new HashMap<>();
        params.put("name", "신분당선 용산행");
        params.put("color", "bg-red-604");
        params.put("startTime", LocalTime.of(05, 30).format(DateTimeFormatter.ISO_TIME));
        params.put("endTime", LocalTime.of(02, 30).format(DateTimeFormatter.ISO_TIME));
        params.put("intervalTime", "5");

        ExtractableResponse<Response> updateRequest = RestAssured.given()
            .log()
            .all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(params)
            .when()
            .put(url)
            .then()
            .log()
            .all()
            .extract();

        assertThat(updateRequest.statusCode()).isEqualTo(HttpStatus.OK.value());

        //then
        ExtractableResponse<Response> getResponse = RestAssured.given()
            .log()
            .all().
            accept(MediaType.APPLICATION_JSON_VALUE).
            when().
            get(url).
            then().
            log().all().
            extract();

        assertThat(getResponse.body()).isNotNull();
        assertThat(getResponse.body().jsonPath().get().toString()).contains("신분당선 용산행");
    }

    @DisplayName("지하철 노선을 제거한다.")
    @Test
    void deleteLine() {
        // given
        // 지하철_노선_등록되어_있음
        ExtractableResponse<Response> enrollRequest = 새로운_신분당선_노선을_생성한다();

        // when
        // 지하철_노선_제거_요청
        String url = enrollRequest.header("Location");
        ExtractableResponse<Response> deleteRequest = RestAssured.given()
            .log()
            .all()
            .when()
            .delete(url)
            .then()
            .log()
            .all()
            .extract();

        // then
        // 지하철_노선_삭제됨
        assertThat(deleteRequest.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
