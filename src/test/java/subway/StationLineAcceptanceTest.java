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
import static subway.AcceptanceTestUtils.*;

@DisplayName("지하철 노선 관련 기능")
@AcceptanceTest
class StationLineAcceptanceTest {

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
        ValidatableResponse stationLineCratedResponse = createStationLines(lineName, color, upStationId, downStationId, distance);

        //then
        verifyResponseStatus(stationLineCratedResponse, HttpStatus.CREATED);
        verifyCreatedStationLineResponseBody(lineName, color, upStationId, upStationName, downStationId, downStationName, stationLineCratedResponse);

        ValidatableResponse foundStationLineResponse = getStationLine(LINES_RESOURCE_URL);
        verifyResponseStatus(foundStationLineResponse, HttpStatus.OK);

        foundStationLineResponse
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

        createStationLines(firstLineName, firstColor, firstUpStationId, firstDownStationId, distance);
        createStationLines(secondLineName, secondColor, secondUpStationId, secondDownStationId, distance);

        //when
        ValidatableResponse foundStationLineResponse = getStationLine(LINES_RESOURCE_URL);


        //then
        verifyResponseStatus(foundStationLineResponse, HttpStatus.OK);

        foundStationLineResponse
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

        ValidatableResponse stationLineCratedResponse = createStationLines(lineName, color, upStationId, downStationId, distance);

        //when
        ValidatableResponse foundStationLineResponse = getStationLine(getLocation(stationLineCratedResponse));


        //then
        verifyResponseStatus(foundStationLineResponse, HttpStatus.OK);

        foundStationLineResponse
                .body("name", equalTo(lineName))
                .body("color", equalTo(color))
                .body("stations", hasSize(2))
                .body("stations[0].id", equalTo(upStationId))
                .body("stations[0].name", equalTo(upStationName))
                .body("stations[1].id", equalTo(downStationId))
                .body("stations[1].name", equalTo(downStationName))
        ;
    }

    /**
     * 지하철노선 수정
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
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

        ValidatableResponse stationLineCratedResponse = createStationLines(lineName, color, upStationId, downStationId, distance);

        //when
        ValidatableResponse modifiedStationLineResponse = modifyStationLine(newLineName, newColor, getLocation(stationLineCratedResponse));

        //then
        verifyResponseStatus(modifiedStationLineResponse, HttpStatus.OK);

        ValidatableResponse foundStationLineResponse = getStationLine(getLocation(stationLineCratedResponse));
        verifyResponseStatus(foundStationLineResponse, HttpStatus.OK);

        foundStationLineResponse
                .body("name", equalTo(newLineName))
                .body("color", equalTo(newColor))
                .body("stations", hasSize(2))
                .body("stations[0].id", equalTo(upStationId))
                .body("stations[0].name", equalTo(upStationName))
                .body("stations[1].id", equalTo(downStationId))
                .body("stations[1].name", equalTo(downStationName))
        ;

    }

    /**
     * 지하철노선 삭제
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */

    private ValidatableResponse verifyCreatedStationLineResponseBody(String lineName, String color, int upStationId, String upStationName, int downStationId, String downStationName, ValidatableResponse stationLineCratedResponse) {
        return stationLineCratedResponse
                .body("name", equalTo(lineName))
                .body("color", equalTo(color))
                .body("stations", hasSize(2))
                .body("stations[0].id", equalTo(upStationId))
                .body("stations[0].name", equalTo(upStationName))
                .body("stations[1].id", equalTo(downStationId))
                .body("stations[1].name", equalTo(downStationName));
    }

    private ValidatableResponse createStationLines(String lineName, String color, long upStationId, long downStationId, long distance) {
        Map<String, Object> params = new HashMap<>();
        params.put("lineName", lineName);
        params.put("color", color);
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", distance);

        return RestAssured
                .given().log().all()
                .body(convertToJsonString(params)).contentType(ContentType.JSON)
                .when()
                .post(LINES_RESOURCE_URL)
                .then().log().all();
    }

    private ValidatableResponse createStation(String stationName) {
        Map<String, String> params = new HashMap<>();
        params.put("name", stationName);

        return RestAssured
                .given().log().all()
                .body(AcceptanceTestUtils.convertToJsonString(params)).contentType(ContentType.JSON)
                .when()
                .post(STATIONS_RESOURCE_URL)
                .then().log().all();
    }


    private ValidatableResponse getStationLine(String url) {
        return RestAssured
                .given().log().all()
                .when()
                .get(url)
                .then().log().all();
    }

    private ValidatableResponse modifyStationLine(String lineName, String color, String url) {
        Map<String, String> params = new HashMap<>();
        params.put("name", lineName);
        params.put("color", color);

        return RestAssured
                .given().log().all()
                .body(AcceptanceTestUtils.convertToJsonString(params)).contentType(ContentType.JSON)
                .when()
                .put(url)
                .then().log().all();
    }
}