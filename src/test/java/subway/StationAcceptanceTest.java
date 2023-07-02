package subway;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

@DisplayName("지하철역 관련 기능")
@AcceptanceTest
class StationAcceptanceTest {

    private static final String RESOURCE_URL = "/stations";


    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // given
        String stationName = "강남역";

        // when
        ValidatableResponse stationCreatedResponse = createStation(stationName);

        // then
        verifyResponse(stationCreatedResponse, HttpStatus.CREATED);

        ValidatableResponse stationFoundResponse = getStations(getLocation(stationCreatedResponse));
        verifyResponse(stationFoundResponse, HttpStatus.OK);

        stationFoundResponse
                .body("name", equalTo(stationName));
    }

    @DisplayName("지하철역을 전체 조회한다.")
    @Test
    void showStations() {
        // given
        String firstStationName = "언주역";
        createStation(firstStationName);

        String secondStationName = "삼성역";
        createStation(secondStationName);

        // when
        ValidatableResponse foundStation = getStations(RESOURCE_URL);

        // then
        verifyResponse(foundStation, HttpStatus.OK);

        foundStation
                .body("", hasSize(2))
                .body("[0].name", equalTo(firstStationName))
                .body("[1].name", equalTo(secondStationName));
    }

    @DisplayName("지하철역을 삭제한다.")
    @Test
    void deleteStation() {
        // given
        String stationName = "청담역";
        ValidatableResponse stationCreatedResponse = createStation(stationName);
        verifyResponse(stationCreatedResponse, HttpStatus.CREATED);
        String createdResourceLocation = getLocation(stationCreatedResponse);

        // when
        ValidatableResponse stationDeletedResponse = deleteStation(createdResourceLocation);

        // then
        verifyResponse(stationDeletedResponse, HttpStatus.NO_CONTENT);

        ValidatableResponse foundStation = getStations(createdResourceLocation);
        verifyResponse(foundStation, HttpStatus.NOT_FOUND);
    }

    private void verifyResponse(ValidatableResponse stationFoundResponse, HttpStatus status) {
        stationFoundResponse.assertThat().statusCode(status.value());
    }

    private ValidatableResponse createStation(String stationName) {
        Map<String, String> params = new HashMap<>();
        params.put("name", stationName);

        return RestAssured
                .given().log().all()
                .body(AcceptanceTestUtils.convertToJsonString(params)).contentType(ContentType.JSON)
                .when()
                .post(RESOURCE_URL)
                .then().log().all();
    }

    private ValidatableResponse getStations(String url) {
        return RestAssured
                .given().log().all()
                .when()
                .get(url)
                .then().log().all();
    }

    private ValidatableResponse deleteStation(String url) {
        return RestAssured
                .given()
                .given().log().all()
                .when()
                .delete(url)
                .then().log().all();
    }

    private String getLocation(ValidatableResponse response) {
        return response.extract().header(HttpHeaders.LOCATION);
    }
}