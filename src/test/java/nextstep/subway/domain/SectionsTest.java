package nextstep.subway.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class SectionsTest {

    @Test
    void 구간목록을_생성한다() {
        // given
        Sections sections = new Sections();

        // then
        assertThat(sections.getStation()).isEmpty();
    }

    @ParameterizedTest
    @ValueSource(longs = {0L, -1L, -2L})
    void 구간목록에_구간을_추가시킬_경우_거리가_1_미만인_경우_예외를_발생시킨다(long distance) {
        // given
        final Station 모란역 = new Station(1L, "모란역");
        final Station 암사역 = new Station(2L, "암사역");
        final Line line = new Line("8호선", "bg-pink-500");
        Sections sections = new Sections();

        // then
        assertThatIllegalArgumentException().isThrownBy(() ->
                sections.addSection(line, new Section(distance, 모란역, 암사역))
        );
    }

    @Test
    void 새로운_구간의_상행역은_해당_노선에_등록되어있는_하행_종점역이_아니면_예외를_발생시킨다() {
        // given
        final Line line = new Line("8호선", "bg-pink-500");
        final Station 모란역 = new Station(1L, "모란역");
        final Station 암사역 = new Station(2L, "암사역");
        final Section firstSection = new Section(10L, 모란역, 암사역);
        final Sections sections = new Sections();
        sections.addSection(line, firstSection);

        final Station 송파역 = new Station(3L, "송파역");
        final Section secondSection = new Section(10L, 모란역, 송파역);

        // then
        assertThatIllegalArgumentException().isThrownBy(() ->
                sections.addSection(line, secondSection)
        );
    }

    @Test
    void 새로운_구간의_하행역은_해당_노선에_등록되어있는_역이면_예외를_발생시킨다() {
        // given
        final Line line = new Line("8호선", "bg-pink-500");
        final Station 모란역 = new Station(1L, "모란역");
        final Station 암사역 = new Station(2L, "암사역");
        final Section firstSection = new Section(10L, 모란역, 암사역);
        final Sections sections = new Sections();
        sections.addSection(line, firstSection);

        final Station 송파역 = new Station(3L, "송파역");
        Section secondSection = new Section(10L, 송파역, 모란역);

        // then
        assertThatIllegalArgumentException().isThrownBy(() ->
                sections.addSection(line, secondSection)
        );
    }

    @Test
    void 구간을_삭제한다() {
        // given
        final Station 모란역 = new Station(1L, "모란역");
        final Station 암사역 = new Station(2L, "암사역");
        final Line line = new Line("8호선", "bg-pink-500");
        final Section firstSection = new Section(10L, 모란역, 암사역);
        final Sections sections = new Sections();
        sections.addSection(line, firstSection);

        final Station 송파역 = new Station(4L, "송파역");
        Section section = new Section(10L, 암사역, 송파역);
        sections.addSection(line, section);

        final Station 백제고분로 = new Station(5L, "백제고분로");
        Section section2 = new Section(10L, 송파역, 백제고분로);
        sections.addSection(line, section2);

        final Station 강남역 = new Station(6L, "강남역");
        Section section3 = new Section(10L, 백제고분로, 강남역);
        sections.addSection(line, section3);

        // when
        sections.removeSection(강남역);

        // then
        assertThat(sections.getStation()).containsExactly(모란역, 암사역, 송파역, 백제고분로);
    }

    @Test
    void 구간을_삭제할_때_마지막_역이_아니면_예외를_발생시킨다() {
        // given
        final Station 모란역 = new Station(1L, "모란역");
        final Station 암사역 = new Station(2L, "암사역");
        final Line line = new Line("8호선", "bg-pink-500");
        final Section firstSection = new Section(10L, 모란역, 암사역);
        final Sections sections = new Sections();
        sections.addSection(line, firstSection);

        final Station 송파역 = new Station(4L, "송파역");
        Section section = new Section(10L, 암사역, 송파역);
        sections.addSection(line, section);

        final Station 백제고분로 = new Station(5L, "백제고분로");
        Section section2 = new Section(10L, 송파역, 백제고분로);
        sections.addSection(line, section2);

        final Station 강남역 = new Station(6L, "강남역");
        Section section3 = new Section(10L, 백제고분로, 강남역);
        sections.addSection(line, section3);

        // then
        assertThatIllegalArgumentException().isThrownBy(() ->
                sections.removeSection(백제고분로)
        );
    }

    @Test
    void 구간이_하나_남았을때는_삭제할_수_없다() {
        // given
        final Station 모란역 = new Station(1L, "모란역");
        final Station 암사역 = new Station(2L, "암사역");
        final Line line = new Line("8호선", "bg-pink-500");
        final Section firstSection = new Section(10L, 모란역, 암사역);
        final Sections sections = new Sections();
        sections.addSection(line, firstSection);

        assertThatIllegalArgumentException().isThrownBy(() ->
                sections.removeSection(암사역)
        );
    }
}