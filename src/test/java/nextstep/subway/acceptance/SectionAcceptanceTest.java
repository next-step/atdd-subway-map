package nextstep.subway.acceptance;

import io.restassured.response.ValidatableResponse;
import nextstep.SpringBootTestConfig;
import nextstep.subway.acceptance.client.SubwayRestAssured;
import nextstep.subway.acceptance.enums.SubwayRequestPath;
import nextstep.subway.acceptance.factory.LineFactory;
import nextstep.subway.applicaion.dto.StationLineResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Sections;
import nextstep.subway.domain.Station;
import nextstep.subway.exception.SubwayException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DisplayName("지하철 구간 인수테스트")
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
        String stationRequestPath = SubwayRequestPath.STATION.getValue();
        Station 강남역 = new Station(1L, "강남역");
        Station 선릉역 = new Station(2L, "선릉역");
        stationRestAssured.postRequest(stationRequestPath, 강남역, 선릉역);

        //when
        String requestPath = SubwayRequestPath.SECTION.sectionsRequestPath(lineResponse.getId());
        Section 구간 = new Section(강남역.getId(), 선릉역.getId(), 10);
        ValidatableResponse 구간등록_응답 = sectionRestAssured.postRequest(requestPath, 구간);

        //then
        구간등록_응답.statusCode(HttpStatus.CREATED.value());
    }

    /*
     * Given 4개의 지하철역과 구간목록을 생성한다.
     * When 구간목록에 구되을 추가한다.
     * Then 동일한 지하철역을 추가했을 때 Exception 발생한다.
     */
    @Test
    @DisplayName("지하철 구간 등록 실패 테스트 - 이미 등록된 하행역일 경우")
    void 지하철_구간_실패_등록된_하행역() {
        //given
        Station 강남역 = new Station(1L, "강남역");
        Station 선릉역 = new Station(2L, "선릉역");
        Section 첫번째_구간 = new Section(강남역.getId(), 선릉역.getId(), 10);

        Station 영통역 = new Station(2L, "영통역");
        Section 두번째_구간 = new Section(영통역.getId(), 강남역.getId(), 10);

        //when
        ArrayList<Section> sectionList = new ArrayList<>();
        sectionList.add(첫번째_구간);
        Sections sections = new Sections(sectionList);

        //then
        assertThatThrownBy(() -> sections.add(두번째_구간))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("하행역이 이미 등록된 지하철역 입니다.");
    }


    /*
     * Given 4개의 지하철역과 구간, 구간목록을 생성한다.
     * When 구간목록에 구간을 추가한다.
     * Then 새로 추가할 구간의 상행역이 하행 종점역과 동일하지 않으면 Exception
     */
    @Test
    @DisplayName("지하철 구간 등록 실패 테스트 - 새로운 구간의 상행역이 하행 종점이 아닌 경우")
    void 지하철_구간_실패_하행_종점() {
        //given
        Station 강남역 = new Station(1L, "강남역");
        Station 선릉역 = new Station(2L, "선릉역");
        Section 첫번째_구간 = new Section(강남역.getId(), 선릉역.getId(), 10);

        Station 영통역 = new Station(3L, "영통역");
        Station 판교역 = new Station(4L, "판교역");
        Section 두번째_구간 = new Section(영통역.getId(), 판교역.getId(), 10);

        //when
        ArrayList<Section> sectionList = new ArrayList<>();
        sectionList.add(첫번째_구간);
        Sections sections = new Sections(sectionList);

        //then
        assertThatThrownBy(() -> sections.add(두번째_구간))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("구간의 상행역이 노선의 하행 종점역이 아닙니다.");
    }


    /* Given 2개의 지하철역을 생성하고, 지하철 구간에 추가한다. 지하철 구간을 노선에 추가한다.
     * When 하행 종점의 지하철 구간을 삭제한다.
     * Then 하행 종점역이 비어있는지 확인한다.
     */
    @Test
    @DisplayName("지하철 구간 삭제 테스트")
    void 지하철_구간_삭제() {
        //given
        String sectionsRequestPath = SubwayRequestPath.SECTION.sectionsRequestPath(lineResponse.getId());
        String stationRootPath = SubwayRequestPath.STATION.getValue();
        String lineRootPath = SubwayRequestPath.LINE.getValue();

        Station 강남역 = new Station(1L, "강남역");
        Station 선릉역 = new Station(2L, "선릉역");
        stationRestAssured.postRequest(stationRootPath, 강남역, 선릉역);

        Station 정자역 = new Station(2L, "강남역");
        Station 판교역 = new Station(4L, "선릉역");
        stationRestAssured.postRequest(stationRootPath, 정자역, 판교역);


        Section 첫번째_구간 = new Section(강남역.getId(), 선릉역.getId(), 10);
        sectionRestAssured.postRequest(sectionsRequestPath, 첫번째_구간);

        Section 두번째_구간 = new Section(정자역.getId(), 판교역.getId(), 10);
        sectionRestAssured.postRequest(sectionsRequestPath, 두번째_구간);

        //when
        sectionRestAssured.deleteRequest(sectionsRequestPath, "stationId", 두번째_구간.getDownStationId());

        //then
        List<Section> sectionIds = sectionRestAssured.getRequest(lineRootPath).extract().jsonPath().get("sections");
        assertThat(sectionIds).hasSize(1);
    }

    /* Given 2개의 지하철역을 생성하고, 지하철 구간에 추가한다. 지하철 구간을 노선에 추가한다.
     * When 하행 종점의 지하철 구간을 삭제한다.
     * Then 구간이 하나만 등록되어있을 경우 Exception 발생
     */
    @Test
    @DisplayName("구간 삭제 실패 테스트 - 구간이 하나 뿐일때는 삭제할 수 없다")
    void 하나뿐민_구간을_삭제했을_경우() {
        //given
        Station 강남역 = new Station(1L, "강남역");
        Station 선릉역 = new Station(2L, "선릉역");
        Section 첫번째_구간 = new Section(강남역.getId(), 선릉역.getId(), 10);

        //when
        Sections sections = new Sections(List.of(첫번째_구간));

        //then
        assertThatThrownBy(() -> sections.deleteSection(2L))
                .isInstanceOf(SubwayException.class)
                .hasMessage("2개 이상의 구간이 등록되어야 구간을 제거할 수 있습니다.");
    }

    /* Given 4개의 지하철역을 생성하고, 지하철 구간에 추가한다. 지하철 구간을 노선에 추가한다.
     * When 구간이 등록된 노선을 조회한다.
     * Then 하행 종점이 아닌 지하철역을 삭제했기 때문에 Exception 발생
     */
    @Test
    @DisplayName("구간 삭제 실패 테스트 - 삭제 대상이 하행 종점역이 아닌 경우")
    void 하행_종점이_아닌_지하철역_삭제() {
        //given
        Station 강남역 = new Station(1L, "강남역");
        Station 선릉역 = new Station(2L, "선릉역");
        Station 영통역 = new Station(3L, "영통역");
        Station 판교역 = new Station(4L, "판교역");

        Section 첫번째_구간 = new Section(강남역.getId(), 선릉역.getId(), 10);
        Section 두번째_구간 = new Section(영통역.getId(), 판교역.getId(), 10);

        //when
        ArrayList<Section> sectionList = new ArrayList<>();
        sectionList.add(첫번째_구간);
        sectionList.add(두번째_구간);
        Sections sections = new Sections(sectionList);

        //then
        assertThatThrownBy(() -> sections.deleteSection(2L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("하행 종점역만 제거할 수 있습니다.");
    }


    private StationLineResponse createLine() {
      return lineRestAssured.postRequest(SubwayRequestPath.LINE.getValue(), LineFactory.경춘선())
              .extract().as(StationLineResponse.class);
    }

}
