package nextstep.subway.domain;

import nextstep.subway.acceptance.util.GivenUtils;
import nextstep.subway.exception.DuplicatedStationException;
import nextstep.subway.exception.SectionRegistrationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.List;
import java.util.stream.Collectors;

import static nextstep.subway.acceptance.util.GivenUtils.FIVE;
import static nextstep.subway.acceptance.util.GivenUtils.강남역_이름;
import static nextstep.subway.acceptance.util.GivenUtils.역삼역_이름;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SectionsTest {

    @Test
    @DisplayName("section 추가 - 성공적인 생성")
    void addSection() {
        // given
        int expectedSize = 2;
        Sections sections = new Sections();
        Section section = GivenUtils.강남_역삼_구간();

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
        sections.addSection(GivenUtils.강남_역삼_구간());
        Section invalidSection = new Section(GivenUtils.이호선(), GivenUtils.강남역(), GivenUtils.양재역(), FIVE);

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
        sections.addSection(GivenUtils.강남_역삼_구간());
        Section invalidSection = new Section(GivenUtils.이호선(), GivenUtils.역삼역(), GivenUtils.강남역(), FIVE);

        // when
        Executable executable = () -> sections.addSection(invalidSection);

        // then
        assertThrows(DuplicatedStationException.class, executable);
    }

    private List<String> getStationNames(Sections sections) {
        return sections.getStations()
                .stream()
                .map(Station::getName)
                .collect(Collectors.toList());
    }

}