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


}