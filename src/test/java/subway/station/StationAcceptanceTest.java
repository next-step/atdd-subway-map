package subway.station;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import subway.common.AssertUtil;
import subway.common.DataBaseCleanUp;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
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
    private DataBaseCleanUp dataBaseCleanUp;

    @BeforeEach
    void setUp() {
        dataBaseCleanUp.execute();
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
        ExtractableResponse<Response> response = StationStep.createSubwayStation(GANGNAM);

        // then
        AssertUtil.상태코드_CREATED(response);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        ExtractableResponse<Response> stationsResponse = StationStep.getAllStationsResponse();
        지하철역_목록_이름_검증(stationsResponse, List.of(GANGNAM));
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
        StationStep.createSubwayStation(GANGNAM);
        StationStep.createSubwayStation(YANGJAE);

        //when
        ExtractableResponse<Response> response = StationStep.getAllStationsResponse();

        // then
        AssertUtil.상태코드_OK(response);
        지하철역_목록_이름_검증(response, List.of(GANGNAM, YANGJAE));
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
        ExtractableResponse<Response> gangnamStation = StationStep.createSubwayStation(GANGNAM);
        ExtractableResponse<Response> yangjaeStation = StationStep.createSubwayStation(YANGJAE);
        Long gangnamId = extractStationId(gangnamStation);
        String notDeletedStationName = extractStationName(yangjaeStation);

        // when
        ExtractableResponse<Response> response = StationStep.deleteStationResponse(gangnamId);

        // then
        AssertUtil.상태코드_NO_CONTENT(response);
        ExtractableResponse<Response> stationsResponse = StationStep.getAllStationsResponse();
        지하철역_목록_이름_검증(stationsResponse, List.of(notDeletedStationName));
    }

    private static void 지하철역_목록_이름_검증(ExtractableResponse<Response> stationsResponse, List<String> stationNames) {
        List<String> names = extractStationNames(stationsResponse);
        assertThat(names).containsExactly(stationNames.toArray(new String[0]));
    }

    public static List<String> extractStationNames(ExtractableResponse<Response> stationsResponse) {
        return stationsResponse.jsonPath().getList(NAME_FILED, String.class);
    }

    public static String extractStationName(ExtractableResponse<Response> stationsResponse) {
        return stationsResponse.jsonPath().getString(NAME_FILED);
    }

    public static Long extractStationId(ExtractableResponse<Response> stationsResponse) {
        return stationsResponse.jsonPath().getLong(ID_FILED);
    }
}