package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.station.StationRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
//@Sql("/truncate.sql")
public class StationAcceptanceTest {
    private static final String STATION_URL = "/stations";
    private static final String STATION_ID_URL = "/stations/{id}";

    private static final String NAME_FILED = "name";
    private static final String ID_FILED = "id";

    public static final String GANGNAM = "강남역";
    public static final String YANGJAE = "양재역";
    public static final String HAGYE = "하계역";
    public static final String JUNGGYE = "중계역";


    @Autowired
    private StationRepository stationRepository;

    @BeforeEach
    void beforeEach() {
        stationRepository.deleteAll();
    }


    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        ExtractableResponse<Response> response = createSubwayStation(GANGNAM);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        ExtractableResponse<Response> stationsResponse = getAllStationsResponse();
        List<String> stationNames = stationResponseToStationNames(stationsResponse);

        assertThat(stationNames).containsAnyOf(GANGNAM);
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @Test
    @DisplayName("지하철역 목록을 조회한다.")
    void getStations() {
        //given
        createSubwayStation(GANGNAM);
        createSubwayStation(YANGJAE);

        //when
        ExtractableResponse<Response> response = getAllStationsResponse();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        //then
        List<String> names = stationResponseToStationNames(response);
        assertThat(names).containsExactly(GANGNAM, YANGJAE);
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @Test
    @DisplayName("지하철역을 삭제한다.")
    void deleteStation() {
        // given
        ExtractableResponse<Response> gangnamStation = createSubwayStation(GANGNAM);
        ExtractableResponse<Response> yangjaeStation = createSubwayStation(YANGJAE);
        String notDeletedStationName = yangjaeStation.jsonPath().get(NAME_FILED);

        // when
        ExtractableResponse<Response> response = deleteStationResponse(gangnamStation);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        // then
        ExtractableResponse<Response> stationsResponse = getAllStationsResponse();

        List<String> names = stationResponseToStationNames(stationsResponse);
        assertThat(names).containsOnly(notDeletedStationName);
    }


    /**
     * 지하철역 생성 메서드
     *
     * @param stationName
     * @return
     */
    static ExtractableResponse<Response> createSubwayStation(String stationName) {
        Map<String, String> params = new HashMap<>();
        params.put(NAME_FILED, stationName);

        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(STATION_URL)
                .then().log().all()
                .extract();
    }

    /**
     * 전체 지하철역 조회 메서드
     *
     * @return
     */
    private static ExtractableResponse<Response> getAllStationsResponse() {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get(STATION_URL)
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> deleteStationResponse(ExtractableResponse<Response> subwayStation1) {
        Object deletedId = subwayStation1.jsonPath().get(ID_FILED);
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .pathParam(ID_FILED, deletedId)
                .delete(STATION_ID_URL)
                .then().log().all()
                .extract();
        return response;
    }

    static List<String> stationResponseToStationNames(ExtractableResponse<Response> stationsResponse) {
        return stationsResponse.jsonPath().getList(NAME_FILED, String.class);
    }

    static Long extractStationId(ExtractableResponse<Response> stationsResponse) {
        return stationsResponse.jsonPath().getLong(ID_FILED);
    }
}