package subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.RestTestUtils;
import subway.line.web.dto.LineRequest;
import subway.line.web.dto.SectionRequest;
import subway.station.web.StationResponse;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 구간 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SectionAcceptanceTest {

    private List<LineRequest> requests;

    @PostConstruct
    void init() {
        createStations();
        this.requests = getRequests();
    }

    /**
     * Given : 지하철 노선을 등록하고
     * When : 해당 노선에 새로운 구간을 추가하면
     *   - 새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이다.
     *   - 새로운 구간의 하행역은 해당 노선에 등록되어있지 않은 역이다.
     * Then : 해당 노선에 새로운 구간이 추가된다
     */
    @DisplayName("지하철 구간 등록 - 정상 케이스")
    @Test
    public void createSection() {
        // given
        LineRequest request = requests.get(0);
        Long lineId = RestTestUtils.getLongFromResponse(createLine(request), "id");
        List<StationResponse> beforeStations = RestTestUtils.getListFromResponse(getLine(lineId), "stations", StationResponse.class);

        // when
        Long upStationId = request.getDownStationId();
        Long downStationId = 3L;
        createSection(lineId, new SectionRequest(downStationId, upStationId, 10));

        // then
        List<StationResponse> afterStations = RestTestUtils.getListFromResponse(getLine(lineId), "stations", StationResponse.class);
        assertThat(afterStations.size()).isGreaterThan(beforeStations.size());
    }

    /**
     * Given : 지하철 노선을 등록하고
     * When : 해당 노선에 새로운 구간을 추가하면
     *   - 새로운 구간의 상행역은 해당 노선에 등록된 하행 종점역이 아니다.
     * Then : 새로운 구간이 추가되지 않는다.
     */
    @DisplayName("지하철 구간 등록 - 예외 케이스 : 새로운 구간의 상행역이 해당 노선에 등록되어있는 하행 종점역이 아닌 경우")
    @Test
    public void createSection_InvalidCase1() {

    }

    /**
     * Given : 지하철 노선을 등록하고
     * When : 해당 노선에 새로운 구간을 추가하면
     *   - 새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이어야 한다.
     *   - 새로운 구간의 하행역은 해당 노선에 등록되어있는 역일 수 없다.
     * Then : 해당 노선에 새로운 구간이 추가된다
     *   - 새로운 구간 등록시 위 조건에 부합하지 않는 경우 에러 처리한다.
     */
    @DisplayName("지하철 구간 등록 - 예외 케이스 : 새로운 구간의 하행역이 해당 노선에 등록되어있는 역인 경우")
    @Test
    public void createSection_InvalidCase2() {

    }

    /**
     * Given : 구간이 두 개 이상인 지하철 노선을 등록하고
     * When : 하행 종점역을 제거하면
     * Then : 해당 종점역을 포함한 구간이 제거된다
     */
    @DisplayName("지하철 구간 제거 - 정상 케이스")
    @Test
    public void deleteSection() {
        // given
        LineRequest request = requests.get(0);
        Long lineId = RestTestUtils.getLongFromResponse(createLine(request), "id");
        Long upStationId = request.getDownStationId();
        Long downStationId = 3L;
        createSection(lineId, new SectionRequest(downStationId, upStationId, 10));
        List<StationResponse> beforeStations = RestTestUtils.getListFromResponse(getLine(lineId), "stations", StationResponse.class);
        Long beforeLastSectionId = RestTestUtils.getLongFromResponse(getSections(lineId), "lastSectionId");

        // when
        deleteSection(lineId, downStationId);

        // then
        Long afterLastSectionId = RestTestUtils.getLongFromResponse(getSections(lineId), "lastSectionId");
        assertThat(afterLastSectionId).isNotEqualTo(beforeLastSectionId);

        List<StationResponse> afterStations = RestTestUtils.getListFromResponse(getLine(lineId), "stations", StationResponse.class);
        assertThat(beforeStations.size()).isGreaterThan(afterStations.size());
        assertThat(afterStations.stream().map(StationResponse::getId).collect(Collectors.toList())).doesNotContain(downStationId);


    }

    /**
     * Given : 구간이 한 개인 지하철 노선을 등록하고
     * When : 하행 종점역을 제거하면
     * Then : 해당 구간이 제거되지 않는다.
     */
    @DisplayName("지하철 구간 제거 - 예외 케이스 : 구간이 한 개인 노선의 하행 종점역을 제거하려는 경우")
    @Test
    public void deleteSection_InvalidCase1() {
    }

    /**
     * Given : 구간이 두 개 이상인 지하철 노선을 등록하고
     * When : 하행 종점역이 아닌 역을 제거하면
     * Then : 해당 구간이 제거되지 않는다.
     */
    @DisplayName("지하철 구간 제거 - 예외 케이스 : 하행 종점역이 아닌 역을 제거하려는 경우")
    @Test
    public void deleteSection_InvalidCase2() {
    }

    private void createStations() {
        List<String> names = List.of("강남역", "양재역", "판교역");
        for (String name : names) {
            RestAssured.given().log().all()
                    .body(Map.of("name", name))
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when().post("/stations")
                    .then().log().all()
                    .statusCode(HttpStatus.CREATED.value());
        }
    }

    private List<LineRequest> getRequests() {
        LineRequest request1 = LineRequest.builder()
                .name("신분당선")
                .color("bg-red-600")
                .upStationId(1L)
                .downStationId(2L)
                .distance(10)
                .build();

        LineRequest request2 = LineRequest.builder()
                .name("분당선")
                .color("bg-yellow-600")
                .upStationId(1L)
                .downStationId(3L)
                .distance(10)
                .build();

        return List.of(request1, request2);
    }

    private ExtractableResponse<Response> createLine(LineRequest request) {
        return RestAssured.given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();
    }

    private ExtractableResponse<Response> getLine(Long id) {
        return RestAssured.given().log().all()
                .when().get("/lines/"+id)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> getSections(Long id) {
        return RestAssured.given().log().all()
                .when().get("/lines/"+id+"/sections")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> createSection(Long lineId, SectionRequest request) {
        return RestAssured.given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines/"+lineId+"/sections")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }

    private ExtractableResponse<Response> deleteSection(Long lineId, Long stationId) {
        return RestAssured.given().log().all()
                .when().delete("/lines/"+lineId+"/sections?stationId="+stationId)
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .extract();
    }


}
