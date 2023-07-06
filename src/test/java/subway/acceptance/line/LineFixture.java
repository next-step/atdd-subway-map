package subway.acceptance.line;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import subway.line.service.request.LineCreateRequest;
import subway.line.service.request.LineUpdateRequest;
import subway.line.service.response.LineResponse;

public class LineFixture {

    public static final String BASE_URL = "/lines";

    public LineResponse 지하철노선을_생성한다(final LineCreateRequest request) {
        final var response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post(BASE_URL)
                .then();

        statusCodeShouldBe(response, HttpStatus.CREATED);

        return response.extract()
                .jsonPath()
                .getObject("", LineResponse.class);
    }

    public void 지하철노선을_생성한다(final List<LineCreateRequest> requests) {
        requests.forEach(this::지하철노선을_생성한다);
    }

    public List<LineResponse> 모든_지하철노선을_조회한다() {
        final var response = RestAssured.given()
                .when().get(BASE_URL)
                .then();

        statusCodeShouldBe(response, HttpStatus.OK);

        return response.extract()
                .jsonPath()
                .getList("", LineResponse.class);
    }

    public LineResponse 지하철노선을_조회한다(final Long id) {
        final var response = RestAssured.given()
                .when().get(BASE_URL + "/" + id)
                .then();

        statusCodeShouldBe(response, HttpStatus.OK);

        return response.extract()
                .jsonPath()
                .getObject("", LineResponse.class);
    }

    public void 지하철노선을_수정한다(final Long id, final LineUpdateRequest request) {
        final var response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().put(BASE_URL + "/" + id)
                .then();

        statusCodeShouldBe(response, HttpStatus.OK);
    }

    public void 지하철노선을_제거한다(final Long id) {
        final var response = RestAssured.given()
                .when().delete(BASE_URL + "/" + id)
                .then();

        statusCodeShouldBe(response, HttpStatus.NO_CONTENT);
    }

    private void statusCodeShouldBe(final ValidatableResponse response, final HttpStatus expected) {
        response.statusCode(expected.value());
    }
}
