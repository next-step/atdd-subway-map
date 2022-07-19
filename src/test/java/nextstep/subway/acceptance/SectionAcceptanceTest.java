package nextstep.subway.acceptance;

import io.restassured.response.ValidatableResponse;
import nextstep.SpringBootTestConfig;
import nextstep.subway.acceptance.client.SubwayRestAssured;
import nextstep.subway.acceptance.enums.SubwayRequestPath;
import nextstep.subway.acceptance.fake.FakeLine;
import nextstep.subway.acceptance.fake.FakeSection;
import nextstep.subway.acceptance.fake.FakeStation;
import nextstep.subway.applicaion.dto.StationLineResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.Matchers.*;

@DisplayName("지하철 구간 인수테스트")
@ActiveProfiles("test")
class SectionAcceptanceTest extends SpringBootTestConfig {

    protected final SubwayRestAssured<Section> sectionRestAssured = new SubwayRestAssured<>();
    protected final SubwayRestAssured<Line> lineRestAssured = new SubwayRestAssured<>();
    protected final SubwayRestAssured<Station> stationRestAssured = new SubwayRestAssured<>();

    private StationLineResponse lineResponse;

    @Override
    @BeforeEach
    protected void setUp() {
        super.setUp();
        lineResponse = createLine();
    }

    /*
     * Given 2개의 지하철역을 등록한다.
     * When 2개의 지하철역을 노선에 추가하고 지하철 구간을 등록한다.
     * Then 지하철 구간 조회 시 생성한 구간을 찾을 수 있다
     */
    @Test
    @DisplayName("지하철 구간을 등록한다")
    void 지하철_구간등록_테스트() {
        //given
        역을_등록한다(FakeStation.강남역, FakeStation.선릉역);

        //when
        ValidatableResponse 구간등록_응답 = 구간을_등록한다(FakeSection.강남_선릉_구간);

        //then
        구간등록_응답.statusCode(HttpStatus.CREATED.value());
    }

    /*
     * Given 4개의 지하철역과 구간목록을 생성한다.
     * When 구간목록에 구되을 추가한다.
     * Then 동일한 지하철역을 추가했을 때 400 상태코드와 Exception Massage 를 응답 받는다.
     */
    @Test
    @DisplayName("지하철 구간 등록 실패 테스트 - 이미 등록된 하행역일 경우")
    void 지하철_구간_실패_등록된_하행역() {
        //given
        역을_등록한다(FakeStation.강남역, FakeStation.선릉역);
        구간을_등록한다(FakeSection.강남_선릉_구간);

        //when
        ValidatableResponse 구간_등록_응답 = 구간을_등록한다(FakeSection.선릉_강남_구간);

        //then
        assertAll(
                () -> 구간_등록_응답.statusCode(HttpStatus.BAD_REQUEST.value()),
                () -> 구간_등록_응답.body("message", equalTo("하행역이 이미 등록된 지하철역 입니다."))
        );
    }


    /*
     * Given 3개의 지하철 역과 구간 하나를 등록한다.
     * When 상행역이 하행 종점이 아닌 구간을 등록한다.
     * Then 새로 추가할 구간의 상행역이 하행 종점역과 동일하지 않으면 Exception
     */
    @Test
    @DisplayName("지하철 구간 등록 실패 테스트 - 새로운 구간의 상행역이 하행 종점이 아닌 경우")
    void 지하철_구간_실패_하행_종점() {
        //given
        역을_등록한다(FakeStation.강남역, FakeStation.선릉역, FakeStation.영통역);
        구간을_등록한다(FakeSection.강남_선릉_구간);

        //when
        ValidatableResponse 구간_등록_응답 = 구간을_등록한다(FakeSection.강남_선릉_구간);

        //then
        assertAll(
                () -> 구간_등록_응답.statusCode(HttpStatus.BAD_REQUEST.value()),
                () -> 구간_등록_응답.body("message", equalTo("구간의 상행역이 노선의 하행 종점역이 아닙니다."))

        );
    }


    /* Given 2개의 지하철역을 생성하고, 지하철 구간에 추가한다.
     * When 하행 종점의 지하철 구간을 삭제한다.
     * Then 하행 종점역이 비어있는지 확인한다.
     */
    @Test
    @DisplayName("지하철 구간 삭제 테스트")
    void 지하철_구간_삭제() {
        //given
        역을_등록한다(FakeStation.강남역, FakeStation.선릉역, FakeStation.영통역);
        구간을_등록한다(FakeSection.강남_선릉_구간, FakeSection.선릉_영통_구간);

        //when
        구간을_삭제한다(FakeSection.선릉_영통_구간_하행역_ID());

        //then
        List<String> sectionIds = sectionRestAssured.getRequest(SubwayRequestPath.LINE.getValue())
                                                    .extract().jsonPath().getList("sections.id", String.class);
        assertThat(sectionIds).hasSize(1);
    }


    /* Given 2개의 지하철역을 생성하고, 지하철 구간에 추가한다.
     * When 하행 종점의 지하철 구간을 삭제한다.
     * Then 구간이 하나만 등록되어있을 경우 Exception 발생
     */
    @Test
    @DisplayName("구간 삭제 실패 테스트 - 구간이 하나 뿐일때는 삭제할 수 없다")
    void 하나뿐민_구간을_삭제했을_경우() {
        //given
        역을_등록한다(FakeStation.강남역, FakeStation.선릉역);
        구간을_등록한다(FakeSection.강남_선릉_구간);

        //when
        ValidatableResponse 구간_삭제_응답 = 구간을_삭제한다(FakeSection.강남_선릉_구간_하행역_ID());

        //then
        assertAll(
                () -> 구간_삭제_응답.statusCode(HttpStatus.BAD_REQUEST.value()),
                () -> 구간_삭제_응답.body("message", equalTo("2개 이상의 구간이 등록되어야 구간을 제거할 수 있습니다."))
        );
    }

    /* Given 3개의 지하철역을 등록한 뒤 구간을 등록한다.
     * When 하행 종점이 아닌 지하철역을 삭제한다
     * Then 400 상태코드와 IllegalArgumentException 을 응답 받는다.
     */
    @Test
    @DisplayName("구간 삭제 실패 테스트 - 삭제 대상이 하행 종점역이 아닌 경우")
    void 하행_종점이_아닌_지하철역_삭제() {
        //given
        역을_등록한다(FakeStation.강남역, FakeStation.선릉역, FakeStation.영통역);
        구간을_등록한다(FakeSection.강남_선릉_구간, FakeSection.선릉_영통_구간);

        //when
        ValidatableResponse 구간_삭제_응답 = 구간을_삭제한다(FakeSection.강남_선릉_구간_상행역_ID());

        //then
        assertAll(
                () -> 구간_삭제_응답.statusCode(HttpStatus.BAD_REQUEST.value()),
                () -> 구간_삭제_응답.body("message", equalTo("하행 종점역만 제거할 수 있습니다."))
        );
    }


    private ValidatableResponse 구간을_등록한다(Section 구간) {
        String requestPath = SubwayRequestPath.SECTION.sectionsRequestPath(lineResponse.getId());
        return sectionRestAssured.postRequest(requestPath, 구간);
    }

    private void 구간을_등록한다(Section ... 구간) {
        String requestPath = SubwayRequestPath.SECTION.sectionsRequestPath(lineResponse.getId());
        sectionRestAssured.postRequest(requestPath, 구간);
    }

    private void 역을_등록한다(Station ... stations) {
        String stationRequestPath = SubwayRequestPath.STATION.getValue();
        stationRestAssured.postRequest(stationRequestPath, stations);
    }

    private ValidatableResponse 구간을_삭제한다(Long sectionId) {
        String sectionsRequestPath = SubwayRequestPath.SECTION.sectionsRequestPath(lineResponse.getId());
        return sectionRestAssured.deleteRequest(sectionsRequestPath, "stationId", sectionId);
    }

    private StationLineResponse createLine() {
      return lineRestAssured.postRequest(SubwayRequestPath.LINE.getValue(), FakeLine.경춘선)
              .extract().as(StationLineResponse.class);
    }

}
