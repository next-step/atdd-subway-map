package nextstep.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Sections(구간 일급 객체)의 테스트")
class SectionsTest {

    @DisplayName("구간을 추가할 수 있다")
    @Test
    void addSection() {
        // given
        Sections sections = new Sections();
        Section section = new Section();

        // when
        sections.addSection(section);

        // then
        assertThat(sections.getSections().size()).isEqualTo(1);
    }

}