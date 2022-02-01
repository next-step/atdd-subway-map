package nextstep.subway.domain.line;

import nextstep.subway.domain.section.Section;
import nextstep.subway.domain.station.Station;
import nextstep.subway.handler.error.custom.BusinessException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static nextstep.subway.domain.factory.EntityFactory.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("노선 단위 테스트")
class LineTest {

    /**
     * 노선이 생성되었을때,
     * 노선의 정보 변경이 가능합니다.
     */
    @Test
    @DisplayName("노선의 정보를 변경한다.")
    void modify() {
        // given
        Line line = createLine("2호선", "green");

        // when
        line.modify("3호선", "orange");

        // then
        assertThat(line.toString()).contains("3호선", "orange");
    }

    /**
     * 노선이 생성되었을때,
     * 노선의 정보 변경시 null 혹은 기존값을 입력하면 해당 값은 변경되지 않아야 합니다.
     */
    @Test
    @DisplayName("노선 정보 수정시 null 혹은 기존 값이 입력되면 변경을 하지 않는다.")
    void validationOfModify() {
        // given
        Line line = createLine("2호선", "green");

        // when
        line.modify("3호선", null);

        // then
        assertThat(line.toString()).contains("3호선", "green");
    }

    /**
     * 노선과 역이 최소 2개가 생성되면,
     * 구간을 추가할 수 있습니다.
     */
    @Test
    @DisplayName("노선에 구간을 등록한다.")
    void addSection() {
        // given
        Line 이호선 = createLine("2호선", "green");
        Station 강남역 = createStation("강남역");
        Station 선릉역 = createStation("선릉역");

        // when
        이호선.addSection(createSection(강남역, 선릉역, 10));

        // then
        assertThat(이호선.getStationList()).containsExactly(강남역, 선릉역);
    }

    /**
     * 노선과 구간이 생성되었을때,
     * 입력된 역이 노선에 존재하는지 확인합니다.
     */
    @Test
    @DisplayName("입력된 역이 노선에 존재하는지 확인한다.")
    void hasStation() {
        // given
        Station 강남역 = createStation("강남역");
        Station 선릉역 = createStation("선릉역");
        Line completeLine = createCompleteLine("2호선", "green", 강남역, 선릉역, 10);

        // when/then
        assertThat(completeLine.hasStation(강남역)).isTrue();
    }

    /**
     * 노선과 구간이 생성되었을때,
     * 입력된 역이 구간의 최 하행역과 같은지 확인합니다.
     */
    @Test
    @DisplayName("입력된 역이 노선의 최하행역과 같은지 확인한다.")
    void hasAnyMatchedDownStation() {
        // given
        Station 강남역 = createStation("강남역");
        Station 선릉역 = createStation("선릉역");
        Station 역삼역 = createStation("역삼역");
        Line 이호선 = createCompleteLine("2호선", "green", 강남역, 선릉역, 10);
        이호선.addSection(createSection(선릉역, 역삼역, 10));

        // when/then
        assertThat(이호선.hasAnyMatchedDownStation(선릉역)).isFalse();
        assertThat(이호선.hasAnyMatchedDownStation(역삼역)).isTrue();
    }

    /**
     * 노선이 생성되고 여러개의 구간이 생성되었을때,
     * 노선의 구간 목록들을 조회합니다.
     */
    @Test
    @DisplayName("노선의 구간목록을 조회한다.")
    void getSectionList() {
        // given
        Station 강남역 = createStation("강남역");
        Station 선릉역 = createStation("선릉역");
        Station 역삼역 = createStation("역삼역");

        Section 강남_선릉_구간 = createSection(강남역, 역삼역, 10);
        Section 선릉_역삼_구간 = createSection(선릉역, 역삼역, 10);

        Line 이호선 = createLine("2호선", "green");

        이호선.addSection(강남_선릉_구간);
        이호선.addSection(선릉_역삼_구간);

        // when
        List<Section> sectionList = 이호선.getSectionList();

        // then
        assertThat(sectionList.size()).isEqualTo(2);
        assertThat(sectionList.contains(선릉_역삼_구간)).isTrue();
    }

    /**
     * 노선이 생성되고 여러 구간에 대해서
     * 노선에 존재하는 구간을 삭제합니다.
     */
    @Test
    @DisplayName("노선의 구간을 삭제한다.")
    void deleteSection() {
        // given
        Station 강남역 = createStation("강남역");
        Station 선릉역 = createStation("선릉역");
        Station 역삼역 = createStation("역삼역");

        Line 이호선 = createCompleteLine("2호선", "green", 강남역, 선릉역, 10);
        이호선.addSection(createSection(선릉역, 역삼역, 10));

        // when
        이호선.deleteSection(역삼역);

        // then
        assertThat(이호선.getSectionList().size()).isEqualTo(1);
    }

    /**
     * 노선을 삭제할때,
     * 구간이 하나만 존재하면 구간 삭제가 불가능합니다.
     */
    @Test
    @DisplayName("노선에 구간이 하나만 있으면 구간 삭제가 불가능합니다.")
    void validationOfDeleteSection() {
        // given
        Station 강남역 = createStation("강남역");
        Station 선릉역 = createStation("선릉역");

        Line 이호선 = createCompleteLine("2호선", "green", 강남역, 선릉역, 10);

        // when / then
        assertThatThrownBy(() -> 이호선.deleteSection(선릉역))
                .isInstanceOf(BusinessException.class);
    }
}