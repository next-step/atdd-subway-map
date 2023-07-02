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
import static subway.AcceptanceTestUtils.convertToJsonString;
import static subway.AcceptanceTestUtils.verifyResponse;

@DisplayName("지하철 노선 관련 기능")
@AcceptanceTest
class StationLineAcceptanceTest {

    private static final String STATIONS_RESOURCE_URL = "/stations";
    private static final String LINES_RESOURCE_URL = "/lines";

    /**
     * 지하철노선 조회
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */

    /**
     * 지하철노선 생성
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     * <p>
     * 노선 생성 시 상행종점역과 하행종점역을 등록합니다.
     * 따라서 이번 단계에서는 지하철 노선에 역을 맵핑하는 기능은 아직 없지만
     * 노선 조회시 포함된 역 목록이 함께 응답됩니다.
     */

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
        verifyResponse(stationLineCratedResponse, HttpStatus.CREATED);
        stationLineCratedResponse
                .body("name", equalTo(lineName))
                .body("color", equalTo(color))
                .body("stations", hasSize(2))
                .body("stations[0].id", equalTo(upStationId))
                .body("stations[0].name", equalTo(upStationName))
                .body("stations[1].id", equalTo(downStationId))
                .body("stations[1].name", equalTo(downStationName))
        ;

        ValidatableResponse foundLineResponse = getStationLine(LINES_RESOURCE_URL);
        verifyResponse(foundLineResponse, HttpStatus.OK);

        foundLineResponse
                .body("", hasSize(1))
                .body("[0].name", equalTo(lineName))
                .body("[0].color", equalTo(color))
                .body("[0].stations", hasSize(2))
                .body("[0].stations[0].id", equalTo(upStationId))
                .body("[0].stations[0].name", equalTo(upStationName))
                .body("[0].stations[1].id", equalTo(downStationId))
                .body("[0].stations[1].name", equalTo(downStationName))
        ;
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




    /**
     * 지하철노선 수정
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */

    /**
     * 지하철노선 삭제
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    /**
     * 지하철노선 목록 조회
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */

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
}