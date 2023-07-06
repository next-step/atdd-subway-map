package subway.acceptance.line;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import subway.global.ExceptionResponse;
import subway.line.service.request.SectionRequest;
import subway.line.service.response.LineResponse;

public class SectionFixture {

    public static final String BASE_URL = "/lines";

    public LineResponse 지하철구간을_등록한다(final Long lineId, final SectionRequest request) {
        final var response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post(BASE_URL + "/" + lineId + "/sections")
                .then();

        statusCodeShouldBe(response, HttpStatus.OK);

        return response.extract()
                .jsonPath()
                .getObject("", LineResponse.class);
    }

    public ExceptionResponse 지하철구간_등록에_실패한다(final Long lineId, final SectionRequest request) {
        final var response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post(BASE_URL + "/" + lineId + "/sections")
                .then();

        statusCodeShouldBe(response, HttpStatus.BAD_REQUEST);

        return response.extract()
                .jsonPath()
                .getObject("", ExceptionResponse.class);
    }

    public void 지하철구간을_제거한다(final Long lineId, final Long stationId) {
        final var response = RestAssured.given()
                .param("stationId", stationId)
                .when().delete(BASE_URL + "/" + lineId + "/sections")
                .then();

        statusCodeShouldBe(response, HttpStatus.NO_CONTENT);
    }

    public void 지하철구간_제거에_실패한다(final Long lineId, final Long stationId) {
        final var response = RestAssured.given()
                .param("stationId", stationId)
                .when().delete(BASE_URL + "/" + lineId + "/sections")
                .then();

        statusCodeShouldBe(response, HttpStatus.BAD_REQUEST);
    }

    private void statusCodeShouldBe(final ValidatableResponse response, final HttpStatus expected) {
        response.statusCode(expected.value());
    }
}
