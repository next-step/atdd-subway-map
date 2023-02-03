package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.exception.SectionException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static subway.fixture.SectionFixture.*;
import static subway.fixture.StationFixture.강남역;
import static subway.fixture.StationFixture.교대역;

@DisplayName("구간 일급 컬렉션 도메인")
class SectionsTest {

    @DisplayName("구간을 추가 할 수 있다.")
    @Test
    void addSectionTest() {
        Sections sections = new Sections();

        assertAll(() -> {
            assertDoesNotThrow(() -> sections.add(강남역_역삼역_구간));
            assertThat(sections.stations()).hasSize(2);
        });

    }

    @DisplayName("새로운 구간의 상행역이 하행 종점역과 동일하지 않은 경우 구간 추가 시 예외가 발생한다.")
    @Test
    void addExceptionTest() {
        Sections sections = new Sections();
        sections.add(강남역_역삼역_구간);

        assertThatThrownBy(() -> sections.add(교대역_양재역_구간))
                .isInstanceOf(SectionException.class);
    }

    @DisplayName("새로운 구간의 하행 종점역이 이미 포함되어 있는 경우 구간 추가 시 예외가 발생한다.")
    @Test
    void addException1Test() {
        Sections sections = new Sections();
        sections.add(강남역_역삼역_구간);
        sections.add(역삼역_교대역_구간);

        Section 교대역_강남역_구간 = new Section(교대역, 강남역, 10);
        assertThatThrownBy(() -> sections.add(교대역_강남역_구간))
                .isInstanceOf(SectionException.class);
    }

    @DisplayName("구간이 1개인 경우 삭제 시 예외가 발생한다.")
    @Test
    void deleteExceptionTest() {
        Sections sections = new Sections();
        sections.add(강남역_역삼역_구간);

        assertThatThrownBy(() -> sections.delete(getDownStationId(강남역_역삼역_구간)))
                .isInstanceOf(SectionException.class);
    }

    @DisplayName("마지막 구간이 아닌 경우 삭제 시 예외가 발생한다.")
    @Test
    void deleteException1Test() {
        Sections sections = new Sections();
        sections.add(강남역_역삼역_구간);
        sections.add(역삼역_교대역_구간);

        assertThatThrownBy(() -> sections.delete(getDownStationId(강남역_역삼역_구간)))
                .isInstanceOf(SectionException.class);
    }

    private long getDownStationId(Section section) {
        return section.getDownStation().getId();
    }
}
