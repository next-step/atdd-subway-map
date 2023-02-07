package subway.section;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import subway.dto.LineRequest;
import subway.dto.SectionRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.line.LineAcceptanceTest.*;
import static subway.station.StationAcceptanceTest.createStationByName;

@DisplayName("노선 관련 기능")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SectionAcceptanceTest {

    private static final String 신분당선_이름 = "신분당선";
    private static final String 신분당선_색 = "bg-red-600";
    private static final String 분당선_이름 = "분당선";
    private static final String 다른분당선_이름 = "다른분당선";
    private static final String 다른분당선_색 = "bg-red-600";

    private static final Long 신분당선_거리 = 10L;
    private static final Long 새로운구간_거리 = 15L;

    private static final Long 지하철역_아이디 = 1L;
    private static final Long 새로운지하철역_아이디 = 2L;
    private static final Long 또다른지하철역_아이디 = 3L;
    private static final LineRequest 신분당선 = LineRequest.of(
            "신분당선", "bg-red-600", 또다른지하철역_아이디, 새로운지하철역_아이디, 신분당선_거리);
    private static final SectionRequest 새로운구간 = SectionRequest.of(
            지하철역_아이디, 또다른지하철역_아이디, 새로운구간_거리);


    @BeforeEach
    void setUp() {
        createStationByName("지하철역");
        createStationByName("새로운지하철역");
        createStationByName("또다른지하철역");
    }

    /**
     * Given 지하철 노선을 하나 생성한다.
     * When 해당 노선에서 구간 목록을 조회한다.
     * Then 해당 노선에서 구간이 생성된 것을 확인한다.
     */
    @DisplayName("노선을 생성하면 구간이 자동 생성된 것을 확인한다.")
    @Test
    void 노선_생성시_구간_자동_생성() {
        //Given
        long lineId = createLineAndGetId(신분당선);

        //When
        ExtractableResponse<Response> response = readSectionsOfLine(lineId);

        //Then
        assertThat(response.jsonPath().
                getList("sections.downStation.id", Long.class)).contains(또다른지하철역_아이디);
        assertThat(response.jsonPath().
                getList("sections.upStation.id", Long.class)).contains(새로운지하철역_아이디);

    }

    /**
     * Given 지하철 노선을 하나 생성한다.
     * When 지하철 구간을 생성하고 노선에 등록한다. 생성한 구간의 상행역은 노선의 하행 종점역이다.
     * Then 해당 노선에서 구간 조회 시 구간을 찾을 수 있다.
     * Then 노선의 하행 종점역이 새로운 구간의 하행 종점역이다.
     * Then 노선의 거리가 새로운 구간의 거리만큼 늘어난다.
     */
    @DisplayName("구간의 상행역을 하행종점역에 등록한다.")
    @Test
    void 구간의_상행역을_노선의_하행종점역에_등록() {
        //Given
        long lineId = createLineAndGetId(신분당선);

        //When
        ExtractableResponse<Response> sectionResponse = createSection(lineId);

        //Then
        ExtractableResponse<Response> sectionsResponse = readSectionsOfLine(lineId);
        List<Long> sectionIds = sectionsResponse.jsonPath().getList("sections.id", Long.class);
        assertThat(sectionIds).contains(sectionResponse.jsonPath().getLong("section.id"));

        //Then
        ExtractableResponse<Response> lineResponse = readLine(lineId);
        List<Long> stationIds = lineResponse.jsonPath().getList("stations.id", Long.class);
        assertThat(stationIds).contains(지하철역_아이디).doesNotContain(또다른지하철역_아이디);

        //Then
        assertThat(lineResponse
                .jsonPath().getLong("distance")).isEqualTo(신분당선_거리 + 새로운구간_거리);
    }

    /**
     * Given 지하철 노선을 생성한다.
     * When 지하철 구간을 생성하고, 새로운 구간의 상행역이 노선의 하행 종점역이 아니다.
     * Then 노선에 등록시 오류가 난다.
     */
    @DisplayName("구간의 상행역을 노선의 상행종점역에 등록한다.")
    @Test
    void 구간_상행역을_노선의_상행종점역에_등록() {

    }

    /**
     * Given 구간이 2개인 지하철 노선을 생성한다.
     * When 상행 종점역이 포함된 구간을 제거한다.
     * Then 오류가 발생한다.
     */
    @DisplayName("노선의 상행종점역을 제거한다.")
    @Test
    void 노선의_상행종점역을_제거() {

    }

    /**
     * Given 구간이 1개인 지하철 노선을 생성한다.
     * When 하행 종점역을 제거한다.
     * Then 오류가 발생한다.
     */
    @DisplayName("구간이 한 개인 노선의 하행종점역을 제거한다.")
    @Test
    void 구간이_한개인_노선의_하행종점역을_제거() {

    }

    /**
     * Given 구간이 2개인 지하철 노선을 생성한다.
     * When 노선에 등록된 하행 종점역을 제거한다.
     * Then 오류가 발생한다.
     */
    @DisplayName("구간이 두 개인 노선의 하행종점역을 제거한다.")
    @Test
    void 노선의_하행종점역을_제거() {

    }

    private static ExtractableResponse<Response> readSectionsOfLine(long lineId) {
        return RestAssured.given().log().all()
                .when().get("/lines/{lineId}/sections", lineId)
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> createSection(long lineId) {
        return RestAssured.given().log().all()
                .body(새로운구간)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines/{lineId}/sections", lineId)
                .then().log().all()
                .extract();
    }
}
