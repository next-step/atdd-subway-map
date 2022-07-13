package nextstep.subway.domain.line;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.domain.line.Fixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class SectionsTest {
    @Test
    @DisplayName("구간 안에는 상행 종점역과 하행 종점역이 포함된다.")
    void test3() {
        // when
        Sections stations = new Sections(구간);

        // then
        assertAll(
            () -> assertThat(stations.getSections()).hasSize(1),
            () -> assertThat(stations.getSections()).contains(구간)
        );
    }

    @Test
    @DisplayName("구간을 추가할 수 있다.")
    void test4() {
        // given
        Section 구간 = new Section(1L, 강남역.getId(), 분당역.getId(), 10);
        Section 추가되는_구간 = new Section(1L, 분당역.getId(), 잠실역.getId(), 10);

        // when
        Sections sections = new Sections(구간);
        sections.addSection(추가되는_구간);

        // then
        assertAll(
            () -> assertThat(sections.getSections().size()).isEqualTo(2),
            () -> assertThat(sections.getSections()).contains(추가되는_구간)
        );
    }

    @Test
    @DisplayName("등록하는 구간의 상행역이 기존 노선의 하행 종점역이 아니면 예외가 발생한다.")
    void test5() {
        // given
        Long 존재하지않는역_ID = 9999L;
        // when
        Section 구간 = new Section(1L, 강남역.getId(), 분당역.getId(), 10);
        Section 추가되는_구간 = new Section(1L, 존재하지않는역_ID, 잠실역.getId(), 10);
        Sections sections = new Sections(구간);

        //then
        assertThatThrownBy(
            () -> sections.addSection(추가되는_구간)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("등록하는 구간의 하행 종점역이 노선에 포함되는 역이 되면 예외가 발생한다.")
    void test6() {
        // given
        Long 상행역_ID = 강남역.getId();
        Long 하행역_ID = 분당역.getId();
        Section 구간 = new Section(1L, 상행역_ID, 하행역_ID, 10);
        Section 추가되는_구간 = new Section(1L, 하행역_ID, 상행역_ID, 10);

        // when
        Sections sections = new Sections(구간);
        //then
        assertThatThrownBy(
            () -> sections.addSection(추가되는_구간)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("구간을 삭제한다.")
    void test7() {
        // given
        Section 구간 = new Section(1L, 1L, 강남역.getId(), 분당역.getId(), 10);
        Section 추가되는_구간 = new Section(2L, 1L, 분당역.getId(), 잠실역.getId(), 10);

        // when
        Sections sections = new Sections(구간);
        sections.addSection(추가되는_구간);
        sections.deleteSection(잠실역.getId());

        // then
        assertAll(
            () -> assertThat(sections.getSections().size()).isEqualTo(1),
            () -> assertThat(sections.getSections()).doesNotContain(추가되는_구간)
        );
    }

    @Test
    @DisplayName("노선에 존재하는 구간이 하나일 때 삭제하려 하면 예외가 발생한다.")
    void test8() {
        // given
        Section 구간 = new Section(1L, 1L, 강남역.getId(), 분당역.getId(), 10);

        // when
        Sections sections = new Sections(구간);

        // then
        assertThatThrownBy(
            () -> sections.deleteSection(분당역.getId())
        ).isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("하행 종점역이 존재하지 않는 구간을 삭제하는 경우 예외가 발생한다.")
    void test10() {
        // given
        Section 기존_구간 = new Section(1L, 1L, 강남역.getId(), 분당역.getId(), 10);
        Section 추가되는_구간 = new Section(2L, 1L, 분당역.getId(), 잠실역.getId(), 10);

        // when
        Sections sections = new Sections(기존_구간);
        sections.addSection(추가되는_구간);

        assertThatThrownBy(
            () -> sections.deleteSection(기존_구간.getId())
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
