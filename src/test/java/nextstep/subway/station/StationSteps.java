package nextstep.subway.station;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.station.dto.StationRequest;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class StationSteps {

    public static ExtractableResponse<Response> 지하철_역_생성_요청(String name) {
        return RestAssured
                .given().log().all()
                .body(new StationRequest(name))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract();
    }

    public static void 지하철_역_생성_됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        생선된_지하철_역_생성_리소스_생성_됨(response);
    }

    public static void 생선된_지하철_역_생성_리소스_생성_됨(ExtractableResponse<Response> response) {
        assertThat(생성된_지하철_역_URI_경로_확인(response)).isNotBlank();
    }

    public static String 생성된_지하철_역_URI_경로_확인(ExtractableResponse<Response> response) {
        return response.header("Location");
    }

    public static void 지하철_역_생성_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static ExtractableResponse<Response> 지하철_역_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .when()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .get("/stations")
                .then().log().all()
                .extract();
    }

    public static void 지하철_역_목록_조회_응답_됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 지하철_역_목록_결과에_포함되는_역_확인(ExtractableResponse<Response> createResponse1, ExtractableResponse<Response> createResponse2, ExtractableResponse<Response> response) {
        List<Long> expectedLineIds = 지하철_역_예상_ID(createResponse1, createResponse2);
        List<Long> resultLineIds = 지하철_역_목록_조회_결과_ID(response);
        assertThat(resultLineIds).containsAll(expectedLineIds);
    }

    public static List<Long> 지하철_역_예상_ID(ExtractableResponse<Response> createResponse1, ExtractableResponse<Response> createResponse2) {
        return Arrays.asList(createResponse1, createResponse2)
                .stream()
                .map(it -> Long.parseLong(생성된_지하철_역_URI_경로_확인(it).split("/")[2]))
                .collect(Collectors.toList());
    }

    public static List<Long> 지하철_역_목록_조회_결과_ID(ExtractableResponse<Response> response) {
        return response.jsonPath().getList(".", StationResponse.class)
                .stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());
    }

    public static ExtractableResponse<Response> 지하철_역_삭제_요청(String uri) {
        return RestAssured
                .given().log().all()
                .when()
                .delete(uri)
                .then().log().all()
                .extract();
    }

    public static void 지하철_역_삭제_됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
