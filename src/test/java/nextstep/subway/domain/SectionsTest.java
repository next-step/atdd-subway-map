package nextstep.subway.domain;

import nextstep.subway.exception.DuplicatedStationException;
import nextstep.subway.exception.NoLastStationException;
import nextstep.subway.exception.SectionRegistrationException;
import nextstep.subway.exception.SectionRemovalException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.List;
import java.util.stream.Collectors;

import static nextstep.subway.acceptance.util.GivenUtils.FIVE;
import static nextstep.subway.acceptance.util.GivenUtils.강남_역삼_구간;
import static nextstep.subway.acceptance.util.GivenUtils.강남역;
import static nextstep.subway.acceptance.util.GivenUtils.강남역_이름;
import static nextstep.subway.acceptance.util.GivenUtils.선릉역;
import static nextstep.subway.acceptance.util.GivenUtils.양재역;
import static nextstep.subway.acceptance.util.GivenUtils.역삼_선릉_구간;
import static nextstep.subway.acceptance.util.GivenUtils.역삼역;
import static nextstep.subway.acceptance.util.GivenUtils.역삼역_이름;
import static nextstep.subway.acceptance.util.GivenUtils.이호선;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SectionsTest {

    @Test
    @DisplayName("section 추가 - 성공적인 추가")
    void addSection() {
        // given
        int expectedSize = 2;
        Sections sections = new Sections();
        Section section = 강남_역삼_구간();

        // when
        sections.addSection(section);

        // then
        List<String> stationNames = getStationNames(sections);
        assertThat(stationNames).hasSize(expectedSize)
                .containsExactly(강남역_이름, 역삼역_이름);
    }

    @Test
    @DisplayName("section 추가 - sections 의 하행 종점역과 다른 upStationId")
    void addSectionWithInvalidUpStationId() {
        // given
        Sections sections = new Sections();
        sections.addSection(강남_역삼_구간());
        Section invalidSection = new Section(이호선(), 강남역(), 양재역(), FIVE);

        // when
        Executable executable = () -> sections.addSection(invalidSection);

        // then
        assertThrows(SectionRegistrationException.class, executable);
    }

    @Test
    @DisplayName("section 추가 - sections 에 이미 존재하는 downStationId")
    void addSectionWithInvalidDownStationId() {
        // given
        Sections sections = new Sections();
        sections.addSection(강남_역삼_구간());
        Section invalidSection = new Section(이호선(), 역삼역(), 강남역(), FIVE);

        // when
        Executable executable = () -> sections.addSection(invalidSection);

        // then
        assertThrows(DuplicatedStationException.class, executable);
    }

    @Test
    @DisplayName("section 제거 - 성공적인 제거")
    void removeSection() {
        // given
        int expectedSize = 2;
        Sections sections = new Sections();
        sections.addSection(강남_역삼_구간());
        sections.addSection(역삼_선릉_구간());

        // when
        sections.removeSection(선릉역());

        // then
        assertThat(getStationNames(sections)).hasSize(expectedSize)
                .containsExactly(강남역_이름, 역삼역_이름);
    }

    @Test
    @DisplayName("section 제거 - 하행 종점역이 아닌 다른 역 제거")
    void removeSectionWithInvalidLastStation() {
        // given
        Sections sections = new Sections();
        sections.addSection(강남_역삼_구간());
        sections.addSection(역삼_선릉_구간());

        // when
        Executable executable = () -> sections.removeSection(역삼역());

        // then
        assertThrows(NoLastStationException.class, executable);
    }

    @Test
    @DisplayName("section 제거 - 구간이 1개인 경우")
    void removeSingleSection() {
        // given
        Sections sections = new Sections();
        sections.addSection(강남_역삼_구간());

        // when
        Executable executable = () -> sections.removeSection(역삼역());

        // then
        assertThrows(SectionRemovalException.class, executable);
    }

    private List<String> getStationNames(Sections sections) {
        return sections.getStations()
                .stream()
                .map(Station::getName)
                .collect(Collectors.toList());
    }

}