package subway.fixture;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import subway.StationResponse;

public class StationFixture {

    public static final String BASE_URL = "/stations";

    public StationResponse 지하철역을_생성한다(final String name) {
        final var response = RestAssured.given()
                .body(Map.of("name", name))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(BASE_URL)
                .then();

        statusCodeShouldBe(response, HttpStatus.CREATED);

        return response.extract()
                .jsonPath()
                .getObject("", StationResponse.class);
    }

    public void 지하철역을_생성한다(final List<String> names) {
        names.forEach(this::지하철역을_생성한다);
    }

    public List<StationResponse> 모든_지하철역을_조회한다() {
        final var response = RestAssured.given()
                .when().get(BASE_URL)
                .then();

        statusCodeShouldBe(response, HttpStatus.OK);

        return response.extract()
                .jsonPath()
                .getList("", StationResponse.class);
    }

    public List<StationResponse> 지하철역을_조회한다(final String name) {
        return 모든_지하철역을_조회한다().stream()
                .filter(it -> it.getName().equals(name))
                .collect(Collectors.toUnmodifiableList());
    }

    public void 지하철역을_제거한다(final Long stationId) {
        final var response = RestAssured.given()
                .when().delete(BASE_URL + "/" + stationId)
                .then();

        statusCodeShouldBe(response, HttpStatus.NO_CONTENT);
    }

    private void statusCodeShouldBe(final ValidatableResponse response, final HttpStatus expected) {
        response.statusCode(expected.value());
    }
}
