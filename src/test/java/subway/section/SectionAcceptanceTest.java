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
import subway.AssertionUtils;
import subway.common.exception.line.DownStationCouldNotExistStationExcetion;
import subway.common.exception.line.UpStationMustTermianlStationException;
import subway.line.LineCreateRequest;
import subway.line.LineResponse;
import subway.line.SectionRequest;
import subway.station.StationResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.OK;
import static subway.AssertionUtils.목록은_다음을_정확하게_포함한다;
import static subway.line.LineApi.지하철노선_단건_조회후_응답객체반환;
import static subway.line.LineApi.지하철노선_생성;
import static subway.section.SectionApi.지하철구간_등록;
import static subway.station.StationApi.지하철역_생성;
import static subway.station.StationFixture.*;

@DisplayName("구간 관련 기능")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SectionAcceptanceTest {

    private StationResponse stationA;
    private StationResponse stationB;
    private StationResponse stationC;

    private LineResponse lineA;

    private SectionRequest sectionA;
    private SectionRequest sectionB;
    private SectionRequest sectionC;

    @BeforeEach
    void setUp() {
        stationA = 지하철역_생성(STATION_A).as(StationResponse.class);
        stationB = 지하철역_생성(STATION_B).as(StationResponse.class);
        stationC = 지하철역_생성(STATION_C).as(StationResponse.class);

        lineA = 지하철노선_생성(new LineCreateRequest("line-A", "line-A-color", stationA.getId(), stationB.getId(), 10)).as(LineResponse.class);

        sectionA = new SectionRequest(stationC.getId(), stationB.getId(), 5);
        sectionB = new SectionRequest(stationC.getId(), stationA.getId(), 5);
        sectionC = new SectionRequest(stationA.getId(), stationB.getId(), 5);
    }

    /**
     * given: 지하철 노선의 하행 종점역을 상행역으로 갖는 구간을 등록하고
     * when: 지하철 노선을 조회하면
     * then: 등록한 구간의 역들이 상행역 하행역 순서로 있는 것을 확인할 수 있다.
     */
    @DisplayName("지하철 구간을 등록한다.")
    @Test
    void addSectionTest() {
        // given
        지하철구간_등록(lineA.getId(), sectionA);

        // when
        final LineResponse lineResponse = 지하철노선_단건_조회후_응답객체반환(lineA.getId());

        // then
        목록은_다음을_정확하게_포함한다(lineResponse.getStations(), stationA, stationB, stationC);
    }

    /**
     * given: 지하철 노선의 하행 종점역을 상행역으로 갖지 않는 구간을
     * when: 등록하면
     * then: 오류가 발생한다
     */
    @DisplayName("지하철 구간의 상행역은 노선의 하행 종점역이어야 한다.")
    @Test
    void upStationMustBeTerminalStationTest() {
        ExtractableResponse<Response> response = 지하철구간_등록(lineA.getId(), sectionB);

        AssertionUtils.응답코드_400를_반환한다(response);
        AssertionUtils.응답메시지는_다음과_같다(response, UpStationMustTermianlStationException.MESSAGE);
    }

    /**
     * given: 이미 등록된 역을 하행역으로 갖는 구간을
     * when: 등록하면
     * then: 오류가 발생한다
     */
    @DisplayName("지하철 구간의 하행역은 노선에 등록된 역일 수 없다.")
    @Test
    void downStationMustNotBeExistStationTest() {
        ExtractableResponse<Response> response = 지하철구간_등록(lineA.getId(), sectionC);

        AssertionUtils.응답코드_400를_반환한다(response);
        AssertionUtils.응답메시지는_다음과_같다(response, DownStationCouldNotExistStationExcetion.MESSAGE);
    }

    /**
     * given: 지하철 노선의 하행 종점역을
     * when: 제거하면
     * then: 해당 역은 삭제된다.
     */
    @DisplayName("지하철 구간을 삭제하면 하행 종점은 삭제된다.")
    @Test
    void deleteSectionTest() {
        // given
        지하철구간_등록(lineA.getId(), sectionA);

        // when
        ExtractableResponse<Response> response = 지하철구간_삭제(lineA.getId(), stationC.getId());

        응답코드_200을_반환한다(response);
    }

    public static void 응답코드_200을_반환한다(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(OK.value());
    }

    private ExtractableResponse<Response> 지하철구간_삭제(Long lineId, Long stationId) {
        return RestAssured
                    .given()
                        .accept(MediaType.ALL_VALUE)
                    .when()
                        .delete("/lines/{lineId}/sections?stationId={stationId}", lineId, stationId)
                    .then()
                    .extract();
    }
}
