package nextstep.subway.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;

class LineTest {

    @Test
    void 라인을_생성한다() {
        // given
        final Station 모란역 = new Station(1L, "모란역");
        final Station 암사역 = new Station(2L, "암사역");

        // when
        final Line line = new Line("8호선", "bg-pink-500", 17L, 모란역, 암사역);

        // then
        assertAll(() -> {
            assertThat(line.getName()).isEqualTo("8호선");
            assertThat(line.getColor()).isEqualTo("bg-pink-500");
            assertThat(line.getDistance()).isEqualTo(new Distance(17L));
            assertThat(line.getStations()).isEqualTo(new Stations(모란역, 암사역));
        });
    }

    @ParameterizedTest
    @ValueSource(longs = {0L, -1L, -2L})
    void 거리가_1_미만인_경우_예외를_발생시킨다(long distance) {
        // given
        final Station 모란역 = new Station(1L, "모란역");
        final Station 암사역 = new Station(2L, "암사역");

        // when
        assertThatIllegalArgumentException().isThrownBy(() -> new Line("8호선", "bg-pink-500", distance, 모란역, 암사역));


    }

}