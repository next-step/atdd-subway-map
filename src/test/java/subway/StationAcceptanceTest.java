package subway;

import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static subway.AcceptanceTestUtils.*;

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
        verifyResponseStatus(stationCreatedResponse, HttpStatus.CREATED);

        ValidatableResponse stationFoundResponse = getResource(getLocation(stationCreatedResponse));
        verifyResponseStatus(stationFoundResponse, HttpStatus.OK);

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
        ValidatableResponse foundStation = getResource(RESOURCE_URL);

        // then
        verifyResponseStatus(foundStation, HttpStatus.OK);

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
        verifyResponseStatus(stationCreatedResponse, HttpStatus.CREATED);
        String createdResourceLocation = getLocation(stationCreatedResponse);

        // when
        ValidatableResponse stationDeletedResponse = deleteResource(createdResourceLocation);

        // then
        verifyResponseStatus(stationDeletedResponse, HttpStatus.NO_CONTENT);

        ValidatableResponse foundStation = getResource(createdResourceLocation);
        verifyResponseStatus(foundStation, HttpStatus.NOT_FOUND);
    }

    private ValidatableResponse createStation(String stationName) {
        Map<String, String> params = new HashMap<>();
        params.put("name", stationName);

        return createResource(RESOURCE_URL, params);
    }
}