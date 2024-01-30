package subway;

import config.fixtures.subway.StationLineMockData;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import subway.dto.StationLineRequest;
import subway.entity.StationLine;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관련 기능")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class StationLineAcceptanceTest {

    public static final String NAME_KEY = "name";
    public static final String COLOR_KEY = "color";
    public static final String UP_STATION_ID_KEY = "upStationId";
    public static final String DOWN_STATION_ID_KEY = "downStationId";
    public static final String DISTANCE_KEY = "distance";

    /**
     * When 지하철 노선을 생성하면
     * When  지하철 노선이 생성된다
     * Then  지하철 노선 목록 조회 시 생성된 노선을 찾을 수 있다.
     */
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createStationLine() {
        // given
        StationLineRequest 신분당선 = StationLineMockData.신분당선;

        // when
        createStationLineRequest(신분당선);
        // then
        assertThat(convertStationLines(findAllStationLines())).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(List.of(신분당선));
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When  지하철 노선 목록을 조회하면
     * Then  지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void findAllStationLine() {
        // given
        StationLineRequest 신분당선 = StationLineMockData.신분당선;
        StationLineRequest 분당선 = StationLineMockData.분당선;

        createStationLineRequest(신분당선);
        createStationLineRequest(분당선);

        // when
        assertThat(convertStationLines(findAllStationLines())).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(List.of(신분당선, 분당선));
    }

    /**
     * Given 지하철 노선을 생성하고
     * When  생성한 지하철 노선을 조회하면
     * Then  생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void findStationLine() {
        // given
        StationLineRequest 신분당선 = StationLineMockData.신분당선;
        ExtractableResponse<Response> response = createStationLineRequest(신분당선);

        // when, then
        assertThat(convertStationLine(findStationLine(getCreatedLocationId(response)))).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(신분당선);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When  생성한 지하철 노선을 수정하면
     * Then  해당 지하철 노선 정보는 수정된다.
     */
    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateStationLine() {
        // given
        StationLineRequest 신분당선 = StationLineMockData.신분당선;
        StationLineRequest 수정된_신분당선 = StationLineMockData.수정된_신분당선;

        ExtractableResponse<Response> createResponse = createStationLineRequest(신분당선);

        // when
        updateStationLineRequest(수정된_신분당선, getCreatedLocationId(createResponse));

        assertThat(convertStationLine(findStationLine(getCreatedLocationId(createResponse)))).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(수정된_신분당선);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When  생성한 지하철 노선을 삭제하면
     * Then  해당 지하철 노선 정보는 삭제된다.
     */
    @DisplayName("지하철 노선을 삭제한다.")
    @Test
    void deleteStationLine() {
        // given
        StationLineRequest 신분당선 = StationLineMockData.신분당선;
        StationLineRequest 분당선 = StationLineMockData.분당선;

        ExtractableResponse<Response> createResponse = createStationLineRequest(신분당선);
        createStationLineRequest(분당선);

        // when
        deleteStationLineRequest(getCreatedLocationId(createResponse));

        // then
        assertThat(convertStationLines(findAllStationLines())).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(List.of(분당선));
    }

    /**
     * 주어진 JsonPath로 부터 StationLine 객체 목록 만들어서 반환
     *
     * @param jsonPath JSON 응답 객체
     * @return StationLine 객체 목록
     */
    private List<StationLine> convertStationLines(JsonPath jsonPath) {
        List<String> names = jsonPath.getList(NAME_KEY, String.class);
        List<String> colors = jsonPath.getList(COLOR_KEY, String.class);
        List<Integer> upStationIds = jsonPath.getList(UP_STATION_ID_KEY, Integer.class);
        List<Integer> downStationIds = jsonPath.getList(DOWN_STATION_ID_KEY, Integer.class);
        List<Integer> distances = jsonPath.getList(DISTANCE_KEY, Integer.class);

        return IntStream.range(0, names.size())
                .mapToObj(i -> new StationLine(
                        names.get(i),
                        colors.get(i),
                        upStationIds.get(i),
                        downStationIds.get(i),
                        distances.get(i)
                ))
                .collect(Collectors.toList());
    }

    /**
     * 주어진 JsonPath로 부터 StationLine 객체를 만들어서 반환
     *
     * @param jsonPath JSON 응답 객체
     * @return StationLine 객체
     */
    private StationLine convertStationLine(JsonPath jsonPath) {
        return new StationLine(
                jsonPath.get(NAME_KEY).toString(),
                jsonPath.get(COLOR_KEY).toString(),
                Integer.parseInt(jsonPath.get(UP_STATION_ID_KEY).toString()),
                Integer.parseInt(jsonPath.get(DOWN_STATION_ID_KEY).toString()),
                Integer.parseInt(jsonPath.get(DISTANCE_KEY).toString())
        );
    }

    /**
     * 지하철 노선 목록을 조회하고 jsonPath 반환
     *
     * @return 지하철 노선 목록을 나타내는 jsonPath 반환
     */
    private JsonPath findAllStationLines() {
        return given().log().all()
                .when()
                .get("/lines")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().jsonPath();
    }

    /**
     * 주어진 지하철 노선 ID에 해당하는 지하철 노선 정보 반환
     *
     * @param stationLineId 지하철 노선 ID
     * @return 지하철 노선 정보를 나타내는 jsonPath 반환
     */
    private JsonPath findStationLine(Long stationLineId) {
        return given().log().all()
                .when()
                .get("/lines/" + stationLineId)
                .then().log().all()
                .extract().jsonPath();
    }

    /**
     * 지하철 노선 생성 요청 후 Response 객체 반환
     *
     * @param request 지하철 요청 정보를 담은 객체
     * @return REST Assured 기반으로 생성된 Response 객체
     */
    private ExtractableResponse<Response> createStationLineRequest(StationLineRequest request) {
        return given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();
    }

    /**
     * 지하철 노선 수정 요청 후 Response 객체 반환
     *
     * @param request       지하철 수정 정보를 담은 객체
     * @param stationLineId 수정할 지하철 노선 ID
     */
    private void updateStationLineRequest(StationLineRequest request, Long stationLineId) {
        given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .put("/lines/" + stationLineId)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().jsonPath();
    }

    /**
     * 지하철 노선 삭제 요청
     *
     * @param stationLineId 삭제할 지하철 노선 ID
     */
    private void deleteStationLineRequest(Long stationLineId) {
        given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .delete("/lines/" + stationLineId)
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    /**
     * 주어진 응답값으로부터 추출된 Location 속성에서 ID를 반환
     *
     * @param createResponse 응답값
     * @return 추출된 Location 속성의 ID
     */
    private Long getCreatedLocationId(ExtractableResponse<Response> createResponse) {
        return Long
                .parseLong(createResponse.header(HttpHeaders.LOCATION)
                        .substring(createResponse.header(HttpHeaders.LOCATION).lastIndexOf('/') + 1));
    }
}
