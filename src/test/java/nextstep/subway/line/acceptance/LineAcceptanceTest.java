package nextstep.subway.line.acceptance;

import io.restassured.RestAssured;
import io.restassured.internal.RestAssuredResponseImpl;
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
        Map<String, String> params = 신분당선_추가();
        ExtractableResponse<Response> response = 지하철노선_조회(new RestAssuredResponseImpl(), params);

        // then
        // 지하철_노선_생성됨
        System.out.println("========== 지하철_노선_생성됨 ==========");
        assertThat(response.statusCode()).isEqualTo(201);
        System.out.println("========== isNotBlank ==========");
        assertThat(response.header("Location")).isNotBlank();
    }

    @DisplayName("기존에 존재하는 지하철 노선 이름으로 지하철 노선을 생성한다.")
    @Test
    void createLine2() {
        // given
        // 지하철_노선_등록되어_있음
        Map<String, String> params = 신분당선_추가();
        ExtractableResponse<Response> response = 지하철노선_조회(new RestAssuredResponseImpl(), params);

        // when
        // 지하철_노선_생성_요청
        params = 신분당선_추가();
        response = 지하철노선_조회(new RestAssuredResponseImpl(), params);

        // then
        // 지하철_노선_생성_실패됨
        System.out.println("========== 지하철_노선_생성_실패됨 ==========");
        assertThat(response.statusCode()).isEqualTo(400);
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLines() {
        // given
        // 지하철_노선_등록되어_있음
        // 지하철_노선_등록되어_있음
        Map<String, String> params = 신분당선_추가();

        // when
        // 지하철_노선_목록_조회_요청
        ExtractableResponse<Response> response = 지하철노선_목록조회(new RestAssuredResponseImpl(), params);

        // then
        // 지하철_노선_목록_응답됨
        System.out.println("========== 지하철_노선_목록_응답됨 ==========");
        assertThat(response.statusCode()).isEqualTo(200);
        // 지하철_노선_목록_포함됨
        System.out.println("========== 지하철_노선_목록_포함됨 ==========");
        assertThat(response.body()).isNotNull();
    }

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLine() {
        // given
        // 지하철_노선_등록되어_있음
        Map<String, String> params = 신분당선_추가();
        ExtractableResponse<Response> createResponse = 지하철노선_조회(new RestAssuredResponseImpl(), params);

        // when
        // 지하철_노선_조회_요청
        System.out.println("========== 지하철_노선_조회_요청 ==========");
        String uri = createResponse.header("Location");
        ExtractableResponse<Response> response = uri로_지하철노선_조회(new RestAssuredResponseImpl(), params, uri);

        // then
        // 지하철_노선_응답됨
        System.out.println("========== 지하철_노선_응답됨 ==========");
        assertThat(response.statusCode()).isEqualTo(200);
        System.out.println("========== isNotNull ==========");
        assertThat(response.as(LineResponse.class)).isNotNull();
    }

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        // 지하철_노선_등록되어_있음
        Map<String, String> params = 신분당선_추가();
        ExtractableResponse<Response> response = 지하철노선_조회(new RestAssuredResponseImpl(), params);

        // when
        // 지하철_노선_수정_요청
        System.out.println("========== 지하철_노선_수정_요청 ==========");
        params = 신분당선_IntervalTime수정( params, "999" );
        response = 지하철노선_조회(new RestAssuredResponseImpl(), params);

        // then
        // 지하철_노선_수정됨
        String uri = response.header("Location");
        response = uri로_지하철노선_조회(new RestAssuredResponseImpl(), params, uri);

        System.out.println("========== 지하철_노선_수정됨 ==========");
        assertThat(response.statusCode()).isEqualTo(200);
        System.out.println("========== isNotNull ==========");
        assertThat(response.as(LineResponse.class)).isNotNull();
    }

    @DisplayName("지하철 노선을 제거한다.")
    @Test
    void deleteLine() {
        // given
        // 지하철_노선_등록되어_있음
        Map<String, String> params = 신분당선_추가();
        ExtractableResponse<Response> response = 지하철노선_조회(new RestAssuredResponseImpl(), params);

        // when
        // 지하철_노선_제거_요청
        System.out.println("========== 지하철_노선_제거_요청 ==========");
        params = 지하철노선_제거( params );
        response = 지하철노선_조회(new RestAssuredResponseImpl(), params);

        // then
        // 지하철_노선_삭제됨
        System.out.println("========== 지하철_노선_삭제됨 ==========");
        assertThat(response.statusCode()).isEqualTo(500);
    }

    /**
     *  세부기능
     *  입력 성공 : response 201
     *  조회 성공 : response 200
     *  실패 : response 400
     *  수정 :
     */
    private Map<String, String> 신분당선_추가() {
        System.out.println("========== 신분당선_추가 START ==========");
        final Map<String, String> params = new HashMap<String, String>();
        params.put("name", "신분당선");
        params.put("color", "bg-red-600");
        params.put("startTime", LocalTime.of(05, 30).format(DateTimeFormatter.ISO_TIME));
        params.put("endTime", LocalTime.of(23, 30).format(DateTimeFormatter.ISO_TIME));
        params.put("intervalTime", "5");
        System.out.println("========== 신분당선_추가 END ==========");
        return params;
    }

    private ExtractableResponse<Response> 지하철노선_조회(ExtractableResponse<Response> response, Map<String, String> params) {
        System.out.println("========== 지하철노선_조회 START ==========");
        response = RestAssured.given().log().all().
                contentType(MediaType.APPLICATION_JSON_VALUE).
                body(params).
                when().
                post("/lines").
                then().
                log().all().
                extract();
        System.out.println("========== 지하철노선_조회 END ==========");
        return response;    // 201 Created
    }

    private ExtractableResponse<Response> 지하철노선_목록조회(ExtractableResponse<Response> response, Map<String, String> params) {
        System.out.println("========== 지하철노선_목록조회 START ==========");
        response = RestAssured.given().log().all().
                contentType(MediaType.APPLICATION_JSON_VALUE).
                body(params).
                when().
                get("/lines").
                then().
                log().all().
                extract();
        System.out.println("========== 지하철노선_목록조회 END ==========");
        return response;    // 200 Created
    }

    private ExtractableResponse<Response> uri로_지하철노선_조회(ExtractableResponse<Response> response, Map<String, String> params, String uri) {
        System.out.println("========== uri로_지하철노선_조회 START ==========");
        response = RestAssured.given().log().all().
                accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                get(uri).
                then().
                log().all().
                extract();
        System.out.println("========== uri로_지하철노선_조회 END ==========");
        return response;    // 200 Created
    }

    private Map<String, String> 신분당선_IntervalTime수정(Map<String, String> params, String newIntervalTime) {
        System.out.println("========== 신분당선_IntervalTime수정 START ==========");
        params.put("name", "수정된_신분당선");
        params.put("color", "bg-red-600");
        params.put("startTime", LocalTime.of(05, 30).format(DateTimeFormatter.ISO_TIME));
        params.put("endTime", LocalTime.of(23, 30).format(DateTimeFormatter.ISO_TIME));
        params.put("intervalTime", newIntervalTime);
        System.out.println("========== 신분당선_IntervalTime수정 END ==========");
        return params;
    }

    private ExtractableResponse<Response> uri로_지하철노선_수정(ExtractableResponse<Response> response, Map<String, String> params, String uri) {
        System.out.println("========== uri로_지하철노선_수정 START ==========");
        response = RestAssured.given().log().all().
                accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                get(uri).
                then().
                log().all().
                extract();
        System.out.println("========== uri로_지하철노선_수정 END ==========");
        return response;    // 200 Created
    }

    private Map<String, String> 지하철노선_제거(Map<String, String> params) {
        System.out.println("========== 지하철노선_제거 START ==========");
        params.clear();
        System.out.println("========== 지하철노선_제거 END ==========");
        return params;
    }
}
