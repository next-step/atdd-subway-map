package nextstep.subway.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class SectionTest {

    @Test
    void 구간을_생성한다() {
        // given
        final Station 모란역 = new Station(1L, "모란역");
        final Station 암사역 = new Station(2L, "암사역");

        // when
        Section section = new Section(10L, 모란역, 암사역);

        // then
        assertThat(section.getStations()).containsExactly(모란역, 암사역);
    }

    @ParameterizedTest
    @ValueSource(longs = {0L, -1L, -2L})
    void 거리가_1_미만인_경우_예외를_발생시킨다(long distance) {
        // given
        final Station 모란역 = new Station(1L, "모란역");
        final Station 암사역 = new Station(2L, "암사역");

        // then
        assertThatIllegalArgumentException().isThrownBy(() -> new Section(distance, 모란역, 암사역));
    }

    @Test
    void 새로운_구간의_상행역은_해당_노선에_등록되어있는_하행_종점역이면_참을_반환한다() {
        // given
        final Station 모란역 = new Station(1L, "모란역");
        final Station 암사역 = new Station(2L, "암사역");
        Section currentSection = new Section(10L, 모란역, 암사역);

        final Station 모란역1 = new Station(2L, "암사역");
        final Station 송파역 = new Station(3L, "송파역");
        Section newSection = new Section(10L, 모란역1, 송파역);

        // then
        assertThat(currentSection.matchDownStation(newSection)).isTrue();
    }

    @Test
    void 새로운_구간의_하행역은_해당_노선에_등록되어있는_역이면_참을_반환한다() {
        // given
        final Station 모란역 = new Station(1L, "모란역");
        final Station 암사역 = new Station(2L, "암사역");
        Section currentSection = new Section(10L, 모란역, 암사역);

        final Station 송파역 = new Station(3L, "송파역");
        final Station 모란역1 = new Station(1L, "모란역");
        Section newSection = new Section(10L, 송파역, 모란역1);

        // then
        assertThat(currentSection.matchStation(newSection)).isTrue();
    }


}