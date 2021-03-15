package nextstep.subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import org.assertj.core.api.Assertions;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class LineTestStep {
    public static ExtractableResponse 지하철_노선_생성_요청(String name, String color, Long upStationId, Long downStationId, int distance) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new LineRequest(name, color, upStationId, downStationId, distance))
                .when().post("/lines")
                .then().log().all().extract();
    }

    public static void 지하철_노선_생성_확인(ExtractableResponse response) {
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }


    public static List<LineResponse> 지하철_노선_목록_등록되어_있음(List<StationResponse> stationResponseList) {
        LineResponse addedLine1 = 지하철_노선_등록되어_있음("신분당선", "red", stationResponseList.get(1).getId(), stationResponseList.get(3).getId(), 1);
        LineResponse addedLine2 = 지하철_노선_등록되어_있음("2호선", "green", stationResponseList.get(0).getId(), stationResponseList.get(2).getId(), 1);
        return Arrays.asList(addedLine1, addedLine2);
    }

    public static ExtractableResponse<Response> 지하철_노선_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .when().get("/lines")
                .then().log().all().extract();
    }

    public static void 지하철_노선_목록_확인(List<LineResponse> addedLineList, ExtractableResponse<Response> response) {
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<LineResponse> lineResponseList = response.jsonPath().getList(".", LineResponse.class);
        Assertions.assertThat(addedLineList).isEqualTo(lineResponseList);
    }

    public static LineResponse 지하철_노선_등록되어_있음(String name, String color, Long upStationId, Long downStationId, int distance) {
        return getAddedLine(지하철_노선_생성_요청(name, color, upStationId, downStationId, distance));
    }

    public static ExtractableResponse<Response> 지하철_노선_조회_요청(Long id) {
        return RestAssured
                .given().log().all()
                .when().get("/lines/" + id)
                .then().log().all().extract();
    }

    public static void 지하철_노선_확인(LineResponse addedLine, ExtractableResponse<Response> response) {
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        Assertions.assertThat(getAddedLine(response)).isEqualTo(addedLine);
    }

    public static ExtractableResponse<Response> 지하철_노선_수정_요청(String name, String color, Long id, Long upStationId, Long downStationId, int distance) {
        return RestAssured
                .given().log().all()
                .body(new LineRequest(name, color, upStationId, downStationId, distance))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/lines/" + id)
                .then().log().all().extract();
    }

    public static void 지하철_노선_수정_확인(ExtractableResponse<Response> response) {
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static ExtractableResponse<Response> 지하철_노선_삭제_요청(Long id) {
        return RestAssured
                .given().log().all()
                .when().delete("/lines/" + id)
                .then().log().all().extract();
    }

    public static void 지하철_노선_삭제_확인(ExtractableResponse<Response> response) {
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static void 지하철_노선_생성_실패_확인(ExtractableResponse<Response> response) {
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    // util
    public static LineResponse getAddedLine(ExtractableResponse<Response> responseExtractableResponse) {
        return responseExtractableResponse.jsonPath().getObject(".", LineResponse.class);
    }
}
