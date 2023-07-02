package subway;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static subway.AcceptanceTestUtils.getLocation;
import static subway.AcceptanceTestUtils.verifyResponse;

@DisplayName("지하철역 관련 기능")
@AcceptanceTest
class StationAcceptanceTest {

    private static final String RESOURCE_URL = "/stations";


    @Test
    void 지하철역을_생성한다() {
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

    @Test
    void 지하철역을_전체_조회한다() {
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

    @Test
    void 지하철역을_삭제한다() {
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

}