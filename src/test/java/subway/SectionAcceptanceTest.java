package subway;

import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import subway.line.dto.LineRequest;
import subway.line.dto.SectionRequest;
import subway.station.entity.Station;
import subway.station.repository.StationRepository;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 구간 관련 기능")
public class SectionAcceptanceTest extends BaseTest{

    @Autowired
    private StationRepository stationRepository;

    private static final String LINE_API_PATH = "/lines";
    private static final String SECTION_API_PATH = "/lines/{id}/sections";

    private Long 강남역_ID;
    private Long 역삼역_ID;
    private Long 지하철역_ID;
    private Long 신분당선_ID;

    @Override
    @BeforeEach
    void setUp() {
        super.setUp();

        final Station 강남역 = stationRepository.save(new Station("강남역"));
        강남역_ID = 강남역.getId();

        final Station 역삼역 = stationRepository.save(new Station("역삼역"));
        역삼역_ID = 역삼역.getId();

        final Station 지하철역 = stationRepository.save(new Station("지하철역"));
        지하철역_ID = 지하철역.getId();
    }

    /**
     * Given 1개의 지하철 노선을 등록하고
     * When 지하철 노선에 구간을 등록하면
     * Then 지하철 노선 조회 시 새로운 구간의 하행역을 찾을 수 있다
     */
    @DisplayName("지하철 구간을 생성한다.")
    @Test
    void 지하철_구간_등록() {
        // given
        this.신분당선_생성();

        // when
        JsonPath createSectionResponse = this.신분당선_구간_연장_역삼역_지하철역();

        // then
        final String lineName = createSectionResponse.get("name");
        assertThat(lineName).isEqualTo("신분당선");

        final String newDownStationName = createSectionResponse.get("stations[1].name");
        assertThat(newDownStationName).isEqualTo("지하철역");
    }

    /**
     * Given 지하철 구간을 생성하고
     * When 생성한 지하철 구간을 삭제하면
     * Then 해당 지하철 구간 정보는 삭제된다
     */
    @DisplayName("지하철 구간을 삭제한다.")
    @Test
    void 지하철_구간_삭제() {
        // given
        this.신분당선_생성();
        this.신분당선_구간_연장_역삼역_지하철역();

        // when
        given().log().all()
                .param("stationId", 지하철역_ID)
                .when()
                .delete(SECTION_API_PATH, 신분당선_ID)
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .log().all()
                .extract();

        // then
        final JsonPath getLineResponse = given()
                .when()
                .get(LINE_API_PATH + "/{id}", 신분당선_ID)
                .then()
                .statusCode(HttpStatus.OK.value())
                .log().all()
                .extract()
                .jsonPath();

        final String newDownStationName = getLineResponse.get("stations[1].name");
        assertThat(newDownStationName).isNotEqualTo("지하철역");
    }

    private JsonPath 신분당선_구간_연장_역삼역_지하철역() {
        final SectionRequest sectionRequest = new SectionRequest(역삼역_ID, 지하철역_ID, 5);
        final ExtractableResponse<Response> createSectionResponse = callCreateApi(sectionRequest, SECTION_API_PATH, 신분당선_ID);
        final JsonPath jsonPath = createSectionResponse.jsonPath();

        return jsonPath;
    }

    private void 신분당선_생성() {
        final LineRequest lineRequest = new LineRequest("신분당선", "bg-red-600", 강남역_ID, 역삼역_ID, 10);
        final ExtractableResponse<Response> createLineResponse = callCreateApi(lineRequest, LINE_API_PATH);
        final String location = createLineResponse.header("Location");
        신분당선_ID = Long.parseLong(location.replaceAll(".*/(\\d+)$", "$1"));
    }

}
