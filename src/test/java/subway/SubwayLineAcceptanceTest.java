package subway;

import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import subway.marker.AcceptanceTest;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static subway.utils.AcceptanceTestUtils.*;

@DisplayName("지하철 노선 관련 기능")
@AcceptanceTest
class SubwayLineAcceptanceTest {

    private static final String STATIONS_RESOURCE_URL = "/stations";
    private static final String LINES_RESOURCE_URL = "/lines";

    @Test
    void 지하철노선을_생성한다() {
        //given
        String lineName = "신분당선";
        String color = "bg-red-600";

        int upStationId = 1;
        String upStationName = "강남역";
        createStation(upStationName);

        int downStationId = 2;
        String downStationName = "언주역";
        createStation(downStationName);

        int distance = 10;

        //when
        ValidatableResponse subwayLineCratedResponse = createSubwayLines(lineName, color, upStationId, downStationId, distance);

        //then
        verifyResponseStatus(subwayLineCratedResponse, HttpStatus.CREATED);

        subwayLineCratedResponse
                .body("name", equalTo(lineName))
                .body("color", equalTo(color))
                .body("stations", hasSize(2))
                .body("stations[0].id", contains(upStationId, downStationId))
                .body("stations[0].name", equalTo(upStationName))
                .body("stations[1].id", equalTo(downStationId))
                .body("stations[1].name", equalTo(downStationName));

        ValidatableResponse foundSubwayLineResponse = getResource(LINES_RESOURCE_URL);
        verifyResponseStatus(foundSubwayLineResponse, HttpStatus.OK);

        foundSubwayLineResponse
                .body("", hasSize(1))
                .body("[0].name", equalTo(lineName))
                .body("[0].color", equalTo(color))
                .body("[0].stations", hasSize(2))
                .body("[0].stations[0].id", equalTo(upStationId))
                .body("[0].stations[0].name", equalTo(upStationName))
                .body("[0].stations[1].id", equalTo(downStationId))
                .body("[0].stations[1].name", equalTo(downStationName));
    }

    @Test
    void 지하철노선_목록을_조회한다() {
        //given
        String firstLineName = "신분당선";
        String firstColor = "bg-red-600";

        int firstUpStationId = 1;
        String firstUpStationName = "강남역";
        createStation(firstUpStationName);

        int firstDownStationId = 2;
        String firstDownStationName = "언주역";
        createStation(firstDownStationName);

        String secondLineName = "수인분당선";
        String secondColor = "bg-green-600";

        int secondUpStationId = 3;
        String secondUpStationName = "수원역";
        createStation(secondUpStationName);

        int secondDownStationId = 4;
        String secondDownStationName = "분당역";
        createStation(secondDownStationName);

        int distance = 10;

        createSubwayLines(firstLineName, firstColor, firstUpStationId, firstDownStationId, distance);
        createSubwayLines(secondLineName, secondColor, secondUpStationId, secondDownStationId, distance);

        //when
        ValidatableResponse foundSubwayLineResponse = getResource(LINES_RESOURCE_URL);

        //then
        verifyResponseStatus(foundSubwayLineResponse, HttpStatus.OK);

        foundSubwayLineResponse
                .body("", hasSize(2))
                .body("[0].name", equalTo(firstLineName))
                .body("[0].color", equalTo(firstColor))
                .body("[0].stations", hasSize(2))
                .body("[0].stations[0].id", equalTo(firstUpStationId))
                .body("[0].stations[0].name", equalTo(firstUpStationName))
                .body("[0].stations[1].id", equalTo(firstDownStationId))
                .body("[0].stations[1].name", equalTo(firstDownStationName))
                .body("[1].name", equalTo(secondLineName))
                .body("[1].color", equalTo(secondColor))
                .body("[1].stations", hasSize(2))
                .body("[1].stations[0].id", equalTo(secondUpStationId))
                .body("[1].stations[0].name", equalTo(secondUpStationName))
                .body("[1].stations[1].id", equalTo(secondDownStationId))
                .body("[1].stations[1].name", equalTo(secondDownStationName))
        ;
    }

    @Test
    void 지하철노선을_조회한다() {
        //given
        String lineName = "신분당선";
        String color = "bg-red-600";

        int upStationId = 1;
        String upStationName = "강남역";
        createStation(upStationName);

        int downStationId = 2;
        String downStationName = "언주역";
        createStation(downStationName);

        int distance = 10;

        ValidatableResponse subwayLineCratedResponse = createSubwayLines(lineName, color, upStationId, downStationId, distance);

        //when
        ValidatableResponse foundSubwayLineResponse = getResource(getLocation(subwayLineCratedResponse));

        //then
        verifyResponseStatus(foundSubwayLineResponse, HttpStatus.OK);

        foundSubwayLineResponse
                .body("name", equalTo(lineName))
                .body("color", equalTo(color))
                .body("stations", hasSize(2))
                .body("stations[0].id", equalTo(upStationId))
                .body("stations[0].name", equalTo(upStationName))
                .body("stations[1].id", equalTo(downStationId))
                .body("stations[1].name", equalTo(downStationName))
        ;
    }

    @Test
    void 지하철_노선을_수정한다() {
        //given
        String lineName = "신분당선";
        String color = "bg-red-600";

        int upStationId = 1;
        String upStationName = "강남역";
        createStation(upStationName);

        int downStationId = 2;
        String downStationName = "언주역";
        createStation(downStationName);

        int distance = 10;

        String newLineName = "바뀐 분당선";
        String newColor = "bg-green-600";

        ValidatableResponse subwayLineCratedResponse = createSubwayLines(lineName, color, upStationId, downStationId, distance);

        //when
        ValidatableResponse modifiedSubwayLineResponse = modifySubwayLine(newLineName, newColor, getLocation(subwayLineCratedResponse));

        //then
        verifyResponseStatus(modifiedSubwayLineResponse, HttpStatus.OK);

        ValidatableResponse foundSubwayLineResponse = getResource(getLocation(subwayLineCratedResponse));
        verifyResponseStatus(foundSubwayLineResponse, HttpStatus.OK);

        foundSubwayLineResponse
                .body("name", equalTo(newLineName))
                .body("color", equalTo(newColor))
                .body("stations", hasSize(2))
                .body("stations[0].id", equalTo(upStationId))
                .body("stations[0].name", equalTo(upStationName))
                .body("stations[1].id", equalTo(downStationId))
                .body("stations[1].name", equalTo(downStationName))
        ;
    }

    @Test
    void 지하철_노선을_제거한다() {
        //given
        String lineName = "신분당선";
        String color = "bg-red-600";

        int upStationId = 1;
        String upStationName = "강남역";
        createStation(upStationName);

        int downStationId = 2;
        String downStationName = "언주역";
        createStation(downStationName);

        int distance = 10;

        ValidatableResponse subwayLineCratedResponse = createSubwayLines(lineName, color, upStationId, downStationId, distance);

        //when
        ValidatableResponse deletedSubwayLineResponse = deleteResource(getLocation(subwayLineCratedResponse));

        //then
        verifyResponseStatus(deletedSubwayLineResponse, HttpStatus.NO_CONTENT);

        ValidatableResponse foundSubwayLineResponse = getResource(getLocation(subwayLineCratedResponse));
        verifyResponseStatus(foundSubwayLineResponse, HttpStatus.NOT_FOUND);
    }

    private ValidatableResponse createSubwayLines(String lineName, String color, long upStationId, long downStationId, long distance) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", lineName);
        params.put("color", color);
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", distance);

        return createResource(LINES_RESOURCE_URL, params);
    }

    private ValidatableResponse createStation(String stationName) {
        Map<String, String> params = new HashMap<>();
        params.put("name", stationName);

        return createResource(STATIONS_RESOURCE_URL, params);
    }

    private ValidatableResponse modifySubwayLine(String lineName, String color, String url) {
        Map<String, String> params = new HashMap<>();
        params.put("name", lineName);
        params.put("color", color);

        return modifyResource(url, params);
    }
}