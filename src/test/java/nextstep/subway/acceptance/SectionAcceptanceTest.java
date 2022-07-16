package nextstep.subway.acceptance;

import io.restassured.response.ValidatableResponse;
import nextstep.SpringBootTestConfig;
import nextstep.subway.acceptance.client.SubwayRestAssured;
import nextstep.subway.acceptance.enums.SubwayRequestPath;
import nextstep.subway.acceptance.factory.LineFactory;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Sections;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DisplayName("지하철 구간 인수테스트")
class SectionAcceptanceTest extends SpringBootTestConfig {

    protected final SubwayRestAssured<Section> sectionRestAssured = new SubwayRestAssured<>();
    protected final SubwayRestAssured<Line> lineRestAssured = new SubwayRestAssured<>();

    private Line line;

    @Override
    @BeforeEach
    protected void setUp() {
        super.setUp();
        line = createLine();
    }

    /*
     * Given 2개의 지하철역과 하나의 노선을 생성하고, 지하철 구간에 추가한다.
     * When 지하철 구간을 등록한다.
     * Then 지하철 구간 조회 시 생성한 구간을 찾을 수 있다
     */
    @Test
    @DisplayName("지하철 구간을 등록한다")
    void 지하철_구간등록_테스트() {
        //given
        Station 강남역 = new Station(1L, "강남역");
        Station 선릉역 = new Station(2L, "선릉역");
        Section 구간 = new Section(강남역.getId(), 선릉역.getId(), 10);

        //when
        String requestPath = SubwayRequestPath.SECTION.sectionsRequestPath(line.getId());
        ValidatableResponse 구간등록_응답 = sectionRestAssured.postRequest(requestPath, 구간);

        //then
        구간등록_응답.statusCode(HttpStatus.CREATED.value());
    }

    /*
     * Given 4개의 지하철역과 지하철역 리스트를 생성한다.
     * When 지하철역 리스트에 역을 추가한다.
     * Then 동일한 지하철역을 추가했을 때 Exception 발생한다.
     */
    @Test
    @DisplayName("지하철 구간 실패 테스트 - 이미 등록된 하행역일 경우")
    void 지하철_구간_실패_등록된_하행역() {
        //given
        Station 강남역 = new Station(1L, "강남역");
        Station 선릉역 = new Station(2L, "선릉역");
        Section 첫번째_구간 = new Section(강남역.getId(), 선릉역.getId(), 10);

        Station 영통역 = new Station(3L, "영통역");
        Section 두번째_구간 = new Section(영통역.getId(), 선릉역.getId(), 10);

        //when
        Sections sections = new Sections(List.of(첫번째_구간));

        //then
        assertThatThrownBy(() -> sections.add(두번째_구간))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("하행역이 이미 등록된 지하철역 입니다.");
    }


    /* Given 2개의 지하철역을 생성하고, 지하철 구간에 추가한다. 지하철 구간을 노선에 추가한다.
     * When 하행 종점의 지하철 구간을 삭제한다.
     * Then 하행 종점역이 비어있는지 확인한다.
     */


    private Line createLine() {
      return lineRestAssured.postRequest(SubwayRequestPath.LINE.getValue(), LineFactory.경춘선())
              .extract().as(Line.class);
    }

}
