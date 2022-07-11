package nextstep.subway.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class SectionsTest {

    @Test
    void 구간을_생성한다() {
        // given
        final Station 모란역 = new Station(1L, "모란역");
        final Station 암사역 = new Station(2L, "암사역");
        final Line line = new Line("8호선", "bg-pink-500", 17L, 모란역, 암사역);

        // when
        Sections sections = new Sections(line, 10L, 모란역, 암사역);

        // then
        assertThat(sections.getStation()).containsExactly(모란역, 암사역);
    }

    @ParameterizedTest
    @ValueSource(longs = {0L, -1L, -2L})
    void 거리가_1_미만인_경우_예외를_발생시킨다(long distance) {
        // given
        final Station 모란역 = new Station(1L, "모란역");
        final Station 암사역 = new Station(2L, "암사역");
        final Line line = new Line("8호선", "bg-pink-500", 17L, 모란역, 암사역);

        // then
        assertThatIllegalArgumentException().isThrownBy(() -> new Sections(line, distance, 모란역, 암사역));
    }

}