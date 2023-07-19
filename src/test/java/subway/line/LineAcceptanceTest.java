package subway.line;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.AcceptanceTest;
import subway.station.StationResponse;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static subway.station.StationAcceptanceTest.*;

@DisplayName("지하철 노선 관리 기능")
public class LineAcceptanceTest extends AcceptanceTest {

    private static final String SINBUNDANG_LINE_NAME = "신분당선";
    private static final String SINBUNDANG_LINE_COLOR = "bg-red-600";
    private static final String SINBUNDANG_UP_STATION_NAME = "신사역";
    private static final String SINBUNDANG_DOWN_STATION_NAME = "광교역";
    private static final Long SINBUNDANG_LINE_DISTANCE = 10L;

    private static final String SUINBUNDANG_LINE_NAME = "수인분당선";
    private static final String SUINBUNDANG_LINE_COLOR = "bg-yellow-600";
    private static final String SUINBUNDANG_UP_STATION_NAME = "청량리역";
    private static final String SUINBUNDANG_DOWN_STATION_NAME = "인천역";
    private static final Long SUINBUNDANG_LINE_DISTANCE = 20L;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void 지하철_노선_생성() {
        // given
        LineRequest request = 지하철역_생성_및_지하철_노선_요청_객체_생성(SINBUNDANG_LINE_NAME, SINBUNDANG_LINE_COLOR, SINBUNDANG_UP_STATION_NAME, SINBUNDANG_DOWN_STATION_NAME, SINBUNDANG_LINE_DISTANCE);

        // when
        ExtractableResponse<Response> createResponse = 지하철_노선_생성_요청(request);
        String lineId = createResponse.response().getHeaders().get("location").getValue().split("/lines/")[1];

        // then
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        ExtractableResponse<Response> showResponse = 지하철_노선_목록_조회_요청();
        List<LineResponse> responses = showResponse.jsonPath().getList(".", LineResponse.class);

        assertThat(responses.size()).isEqualTo(1);

        LineResponse response = responses.get(0);
        List<StationResponse> stationsOfResponse = List.of(new StationResponse(1L, SINBUNDANG_UP_STATION_NAME), new StationResponse(2L, SINBUNDANG_DOWN_STATION_NAME));
        assertThat(response).isEqualTo(new LineResponse(Long.valueOf(lineId), SINBUNDANG_LINE_NAME, SINBUNDANG_LINE_COLOR, stationsOfResponse, SINBUNDANG_LINE_DISTANCE));
    }

    @Test
    void 지하철_노선_목록_조회() {
        // given
        LineRequest request1 = 지하철역_생성_및_지하철_노선_요청_객체_생성(SINBUNDANG_LINE_NAME, SINBUNDANG_LINE_COLOR, SINBUNDANG_UP_STATION_NAME, SINBUNDANG_DOWN_STATION_NAME, SINBUNDANG_LINE_DISTANCE);
        LineRequest request2 = 지하철역_생성_및_지하철_노선_요청_객체_생성(SUINBUNDANG_LINE_NAME, SUINBUNDANG_LINE_COLOR, SUINBUNDANG_UP_STATION_NAME, SUINBUNDANG_DOWN_STATION_NAME, SUINBUNDANG_LINE_DISTANCE);

        Long lineId1 = 지하철_노선_생성_요쳥_및_아이디_추출(request1);
        Long lineId2 = 지하철_노선_생성_요쳥_및_아이디_추출(request2);

        // when
        ExtractableResponse<Response> showResponse = 지하철_노선_목록_조회_요청();
        List<LineResponse> responses = showResponse.jsonPath().getList(".", LineResponse.class);

        // then
        assertThat(showResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(responses.size()).isEqualTo(2);

        LineResponse response1 = responses.get(0);
        List<StationResponse> stationsOfResponse1 = List.of(new StationResponse(1L, SINBUNDANG_UP_STATION_NAME), new StationResponse(2L, SINBUNDANG_DOWN_STATION_NAME));
        assertThat(response1).isEqualTo(new LineResponse(Long.valueOf(lineId1), SINBUNDANG_LINE_NAME, SINBUNDANG_LINE_COLOR, stationsOfResponse1, SINBUNDANG_LINE_DISTANCE));

        LineResponse response2 = responses.get(1);
        List<StationResponse> stationsOfResponse2 = List.of(new StationResponse(3L, SUINBUNDANG_UP_STATION_NAME), new StationResponse(4L, SUINBUNDANG_DOWN_STATION_NAME));
        assertThat(response2).isEqualTo(new LineResponse(Long.valueOf(lineId2), SUINBUNDANG_LINE_NAME, SUINBUNDANG_LINE_COLOR, stationsOfResponse2, SUINBUNDANG_LINE_DISTANCE));
    }

    @Test
    void 지하철_노선_조회() throws JsonProcessingException {
        // given
        LineRequest request = 지하철역_생성_및_지하철_노선_요청_객체_생성(SINBUNDANG_LINE_NAME, SINBUNDANG_LINE_COLOR, SINBUNDANG_UP_STATION_NAME, SINBUNDANG_DOWN_STATION_NAME, SINBUNDANG_LINE_DISTANCE);
        Long lineId = 지하철_노선_생성_요쳥_및_아이디_추출(request);

        // when
        ExtractableResponse<Response> showResponse = 지하철_노선_요청(lineId);

        // then
        assertThat(showResponse.statusCode()).isEqualTo(HttpStatus.OK.value());

        LineResponse response = objectMapper.readValue(showResponse.response().body().asString(), LineResponse.class);
        List<StationResponse> stationsOfResponse = List.of(new StationResponse(1L, SINBUNDANG_UP_STATION_NAME), new StationResponse(2L, SINBUNDANG_DOWN_STATION_NAME));
        assertThat(response).isEqualTo(new LineResponse(Long.valueOf(lineId), SINBUNDANG_LINE_NAME, SINBUNDANG_LINE_COLOR, stationsOfResponse, SINBUNDANG_LINE_DISTANCE));
    }

    @Test
    void 지하철_노선_수정() {
        // given
        LineRequest createRequest = 지하철역_생성_및_지하철_노선_요청_객체_생성(SINBUNDANG_LINE_NAME, SINBUNDANG_LINE_COLOR, SINBUNDANG_UP_STATION_NAME, SINBUNDANG_DOWN_STATION_NAME, SINBUNDANG_LINE_DISTANCE);
        Long lineId = 지하철_노선_생성_요쳥_및_아이디_추출(createRequest);

        // when
        LineRequest updateRequest = 지하철역_생성_및_지하철_노선_요청_객체_생성(SUINBUNDANG_LINE_NAME, SUINBUNDANG_LINE_COLOR, SUINBUNDANG_UP_STATION_NAME, SUINBUNDANG_DOWN_STATION_NAME, SUINBUNDANG_LINE_DISTANCE);
        ExtractableResponse<Response> updateResponse = 지하철_노선_수정_요청(lineId, updateRequest);

        // then
        assertThat(updateResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void 지하철_노선_삭제() {
        // given
        LineRequest createRequest = 지하철역_생성_및_지하철_노선_요청_객체_생성(SINBUNDANG_LINE_NAME, SINBUNDANG_LINE_COLOR, SINBUNDANG_UP_STATION_NAME, SINBUNDANG_DOWN_STATION_NAME, SINBUNDANG_LINE_DISTANCE);
        Long lineId = 지하철_노선_생성_요쳥_및_아이디_추출(createRequest);

        // when
        ExtractableResponse<Response> deleteResponse = 지하철_노선_제거_요청(lineId);

        // then
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        ExtractableResponse<Response> showResponse = 지하철_노선_요청(lineId);
        assertThat(showResponse.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    public static LineRequest 지하철역_생성_및_지하철_노선_요청_객체_생성(String lineName, String color, String upStationName, String downStationName, Long distance) {
        Long upStationId = 지하철역_생성_요청_및_아이디_추출(upStationName);
        Long downStationId = 지하철역_생성_요청_및_아이디_추출(downStationName);
        return new LineRequest(lineName, color, upStationId, downStationId, distance);
    }

    public static Long 지하철_노선_생성_요쳥_및_아이디_추출(LineRequest request) {
        ExtractableResponse<Response> createResponse = 지하철_노선_생성_요청(request);
        String lineId = createResponse.response().getHeaders().get("location").getValue().split("/lines/")[1];
        return Long.valueOf(lineId);
    }

    public static ExtractableResponse<Response> 지하철_노선_생성_요청(LineRequest request) {
        return RestAssured
                .given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 지하철_노선_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .when()
                .get("/lines")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_요청(Long lineId) {
        return RestAssured
                .given().log().all()
                .when()
                .get("/lines/" + lineId)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 지하철_노선_수정_요청(Long lineId, LineRequest request) {
        return RestAssured
                .given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .put("/lines/" + lineId)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 지하철_노선_제거_요청(Long lineId) {
        return RestAssured
                .given().log().all()
                .when()
                .delete("/lines/" + lineId)
                .then().log().all()
                .extract();
    }
}
