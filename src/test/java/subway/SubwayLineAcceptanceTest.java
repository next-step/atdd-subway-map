package subway;

import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import subway.controller.resonse.StationResponse;
import subway.controller.resonse.SubwayLineResponse;
import subway.marker.AcceptanceTest;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static subway.utils.AcceptanceTestUtils.*;

@DisplayName("지하철 노선 관련 기능")
@AcceptanceTest
class SubwayLineAcceptanceTest {

    private static final String STATIONS_RESOURCE_URL = "/stations";
    private static final String LINES_RESOURCE_URL = "/lines";
    private static final String SECTION_RESOURCE_URL = "/sections";

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
        ValidatableResponse subwayLineCratedResponse = createSubwayLines(lineName, color, upStationId, downStationId, distance);

        //then
        verifyResponseStatus(subwayLineCratedResponse, HttpStatus.CREATED);

        subwayLineCratedResponse
                .body("name", equalTo(lineName))
                .body("color", equalTo(color))
                .body("stations", hasSize(2))
                .body("stations[0].id", equalTo(upStationId))
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

    /**
     * 지하철노선 목록 조회
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
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

    /**
     * 지하철노선 조회
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
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

    /**
     * 지하철노선 수정
     * Given 지하철 노선을 생성하고
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

    /**
     * 지하철노선 삭제
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
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


    /**
     * 지하철노선 구간 등록
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선에 추가로 구간을 등록할때
     * 새로운 노선의 상행역이 기존 노선의 하행역이 아니면
     * Then InvalidSectionUpStationException 이 발생한다
     */
    @Test
    void 신규_구간_상행역_불일치_등록_실패() {
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
        long createdLineId = subwayLineCratedResponse.extract().as(SubwayLineResponse.class).getId();

        int sectionDownStationId = 3;
        String sectionDownStationName = "길음역";
        createStation(sectionDownStationName);

        //when
        ValidatableResponse subwayLineSectionCreatedResponse = createSubwayLineSection(createdLineId, sectionDownStationId, downStationId, distance);

        //then
        verifyResponseStatus(subwayLineSectionCreatedResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * 지하철노선 구간 등록
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선에 추가로 구간을 등록할때
     * 새로운 노선의 하행역이 기존 노선에 등록되어 있는 역이면
     * Then InvalidSectionDownStationException 이 발생한다
     */
    @Test
    void 신규_구간_하행역_기등록_실패() {
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
        long createdLineId = subwayLineCratedResponse.extract().as(SubwayLineResponse.class).getId();

        //when
        ValidatableResponse subwayLineSectionCreatedResponse = createSubwayLineSection(createdLineId, downStationId, upStationId, distance);

        //then
        verifyResponseStatus(subwayLineSectionCreatedResponse, HttpStatus.BAD_REQUEST);
    }


    /**
     * /**
     * 지하철노선 구간 등록
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선에 추가로 구간을 등록할때
     * 새로운 노선의 상행역이 기존 노선의 하행역이라면
     * Then 새로운 구간이 노선에 추가되고 조회 시 하행역이 변경되고 거리가 추가 된다.
     */
    @Test
    void 신규_구간_등록_성공() {
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
        long createdLineId = subwayLineCratedResponse.extract().as(SubwayLineResponse.class).getId();

        int sectionDownStationId = 3;
        String sectionDownStationName = "길음역";
        createStation(sectionDownStationName);
        int sectionDistance = 3;

        //when
        ValidatableResponse subwayLineSectionCreatedResponse = createSubwayLineSection(createdLineId, downStationId, sectionDownStationId, sectionDistance);

        //then
        verifyResponseStatus(subwayLineSectionCreatedResponse, HttpStatus.CREATED);

        ValidatableResponse createdSubwayLineSectionResponse = getResource(getLocation(subwayLineCratedResponse));
        verifyResponseStatus(createdSubwayLineSectionResponse, HttpStatus.OK);

        createdSubwayLineSectionResponse
                .body("name", equalTo(lineName))
                .body("color", equalTo(color))
                .body("stations", hasSize(2))
                .body("stations[0].id", equalTo(upStationId))
                .body("stations[0].name", equalTo(upStationName))
                .body("stations[1].id", equalTo(sectionDownStationId))
                .body("stations[1].name", equalTo(sectionDownStationName))
                .body("distance", equalTo(distance + sectionDistance));
    }

    /**
     * /**
     * 지하철노선 구간 제거
     * Given 지하철 노선을 생성하고 생성한 지하철 노선에 추가로 구간을 등록한뒤
     * When 중간에 있는 역을 제거하려 하면
     * Then 예외가 발생하고 실패한다.
     */
    @Test
    void 지하철노선_구간_제거시_하행_종점역이_아니면_실패() {
        //given
        String lineName = "신분당선";
        String color = "bg-red-600";

        int upStationId = 1;
        String upStationName = "강남역";
        createStation(upStationName);

        int downStationId = 2;
        String downStationName = "언주역";
        ValidatableResponse stationCreatedResponse = createStation(downStationName);
        StationResponse stationResponse = stationCreatedResponse.extract().as(StationResponse.class);

        int distance = 10;

        ValidatableResponse subwayLineCratedResponse = createSubwayLines(lineName, color, upStationId, downStationId, distance);
        long createdLineId = subwayLineCratedResponse.extract().as(SubwayLineResponse.class).getId();

        int sectionDownStationId = 3;
        String sectionDownStationName = "길음역";
        createStation(sectionDownStationName);
        int sectionDistance = 3;

        createSubwayLineSection(createdLineId, downStationId, sectionDownStationId, sectionDistance);

        //when
        ValidatableResponse subwayLineSectionDeletedResponse = deleteResource(getLocation(subwayLineCratedResponse) + SECTION_RESOURCE_URL + "?stationId=" + stationResponse.getId());

        //then
        verifyResponseStatus(subwayLineSectionDeletedResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * /**
     * 지하철노선 구간 제거
     * Given 지하철 노선을 생성하고
     * When 하행역을 제거하려 하면
     * Then 예외가 발생하고 실패한다.
     */
    @Test
    void 지하철노선_구간_제거시_구간이_한개인_경우_실패() {
        //given
        String lineName = "신분당선";
        String color = "bg-red-600";

        int upStationId = 1;
        String upStationName = "강남역";
        createStation(upStationName);

        int downStationId = 2;
        String downStationName = "언주역";
        ValidatableResponse stationCreatedResponse = createStation(downStationName);
        StationResponse stationResponse = stationCreatedResponse.extract().as(StationResponse.class);

        int distance = 10;

        ValidatableResponse subwayLineCratedResponse = createSubwayLines(lineName, color, upStationId, downStationId, distance);

        //when
        ValidatableResponse subwayLineSectionDeletedResponse = deleteResource(getLocation(subwayLineCratedResponse) + SECTION_RESOURCE_URL + "?stationId=" + stationResponse.getId());

        //then
        verifyResponseStatus(subwayLineSectionDeletedResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * /**
     * 지하철노선 구간 제거
     * Given 지하철 노선을 생성하고 생성한 지하철 노선에 추가로 구간을 등록한뒤
     * When 하행 종점역을 제거한뒤
     * Then 다시 조회하면 제거된 역을 제외한 상행역과 하행역이 조회되고 거리도 줄어든다
     */
    @Test
    void 지하철노선_구간_제거_성공() {
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
        long createdLineId = subwayLineCratedResponse.extract().as(SubwayLineResponse.class).getId();

        int sectionDownStationId = 3;
        String sectionDownStationName = "길음역";
        ValidatableResponse stationCreatedResponse = createStation(sectionDownStationName);
        StationResponse stationResponse = stationCreatedResponse.extract().as(StationResponse.class);
        int sectionDistance = 3;

        createSubwayLineSection(createdLineId, downStationId, sectionDownStationId, sectionDistance);

        //when
        ValidatableResponse subwayLineSectionDeletedResponse = deleteResource(getLocation(subwayLineCratedResponse) + SECTION_RESOURCE_URL + "?stationId=" + stationResponse.getId());

        //then
        verifyResponseStatus(subwayLineSectionDeletedResponse, HttpStatus.NO_CONTENT);

        ValidatableResponse foundSubwayLineResponse = getResource(LINES_RESOURCE_URL + "/" + createdLineId);
        verifyResponseStatus(foundSubwayLineResponse, HttpStatus.OK);

        foundSubwayLineResponse
                .body("name", equalTo(lineName))
                .body("color", equalTo(color))
                .body("stations", hasSize(2))
                .body("stations[0].id", equalTo(upStationId))
                .body("stations[0].name", equalTo(upStationName))
                .body("stations[1].id", equalTo(downStationId))
                .body("stations[1].name", equalTo(downStationName))
                .body("distance", equalTo(distance));

    }

    private ValidatableResponse createSubwayLineSection(Long lineId, long upStationId, long downStationId, long distance) {
        Map<String, Object> params = new HashMap<>();
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", distance);

        return createResource(String.format("%s/%d%s", LINES_RESOURCE_URL, lineId, SECTION_RESOURCE_URL), params);
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