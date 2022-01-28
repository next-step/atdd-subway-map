package nextstep.subway.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.domain.step_feature.SectionStepFeature.createSection;
import static nextstep.subway.domain.step_feature.StationStepFeature.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Sections(구간 일급 객체)의 테스트")
class SectionsTest {

    private Sections sections;
    private Section section1;
    private Section section2;

    @BeforeEach
    void setUp() {
        sections = new Sections();
        section1 = createSection(createGangnamStation(), createYeoksamStation());
        section2 = createSection(createYeoksamStation(), createPangyoStation());
    }

    @DisplayName("라인을 처음 생성 시에 생성하는 첫 구간만 등록한다.")
    @Test
    void addFirstSection() {
        // when
        sections.addFirstSection(section1);

        // then
        assertThat(sections.getSections().size()).isEqualTo(1);
    }

    @DisplayName("이미 구간이 등록된 경우 추가 불가능하다.")
    @Test
    void addFirstSection_fail() {
        // given
        sections.addFirstSection(section1);

        // then
        assertThatThrownBy(() -> sections.addFirstSection(section2))
                .isInstanceOf(RuntimeException.class);
    }

    @DisplayName("새로운 구간의 상행역은 현재 등록되어있는 하행 종점역이어야 한다.")
    @Test
    void validateAlreadyRegisteredStation() {
        // given
        sections.addFirstSection(section1);

        // when
        sections.addSection(section2);

        // then
        assertThat(sections.getSections()
                .size()).isEqualTo(2);
    }

    @DisplayName("새로운 구간의 상행역이 현재 등록되어있는 하행 종점역이 아니면 실패한다")
    @Test
    void validateAlreadyRegisteredStation_fail() {
        // given
        sections.addFirstSection(section1);
        Section addSection = createSection(createNonhyeonStation(), createPangyoStation());

        // then
        assertThatThrownBy(() -> sections.addSection(addSection))
                .isInstanceOf(RuntimeException.class);
    }

}