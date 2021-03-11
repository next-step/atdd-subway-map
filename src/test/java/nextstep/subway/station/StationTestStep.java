package nextstep.subway.station;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class StationTestStep {
    public static ExtractableResponse<Response> 지하철_역_생성_요청(String name) {
        return RestAssured.given().log().all()
                .body(new Station(name))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract();
    }

    public static void 지하철_역_생성_확인(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    public static StationResponse 지하철_역_등록되어_있음(String name) {
        return 지하철_역_생성_요청(name).jsonPath().getObject(".", StationResponse.class);
    }

    public static ExtractableResponse<Response> 지하철_역_목록_조회_요청() {
        return RestAssured.given().log().all()
                .when()
                .get("/stations")
                .then().log().all()
                .extract();
    }

    public static List<StationResponse> 지하철_역_목록_등록되어_있음() {
        return Arrays.asList(지하철_역_등록되어_있음("강남역"), 지하철_역_등록되어_있음("역삼역"));
    }

    public static void 지하철_역_목록_조회_확인(List<StationResponse> addedStationList, ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<StationResponse> stationResponseList = response.jsonPath().getList(".", StationResponse.class);
        assertThat(stationResponseList).containsAll(addedStationList);
    }

    public static ExtractableResponse<Response> 지하철_역_삭제_요청(Long id) {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when()
                .delete("/stations/" + id)
                .then().log().all()
                .extract();
        return response;
    }

    public static void 지하철_역_생성_실패_확인(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void 지하철_역_제거_확인(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
