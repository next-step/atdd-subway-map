package nextstep.subway.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;

class LineTest {

    @Test
    void 노선을_생성한다() {
        // given
        final Station 모란역 = new Station(1L, "모란역");
        final Station 암사역 = new Station(2L, "암사역");

        // when
        final Line line = new Line("8호선", "bg-pink-500", 17L, 모란역, 암사역);

        // then
        assertAll(() -> {
            assertThat(line.getName()).isEqualTo("8호선");
            assertThat(line.getColor()).isEqualTo("bg-pink-500");
            assertThat(line.getStations()).isEqualTo(List.of(모란역, 암사역));
        });
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {" ", ""})
    void 노선_생성_시_이름이_빈칸이거나_널이면_예외를_발생시킨다(String name) {
        // given
        final Station 모란역 = new Station(1L, "모란역");
        final Station 암사역 = new Station(2L, "암사역");

        // then
        assertThatIllegalArgumentException().isThrownBy(() -> new Line(name, "bg-pink-500", 17L, 모란역, 암사역));
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {" ", ""})
    void 노선_생성_시_색상이_빈칸이거나_널이면_예외를_발생시킨다(String color) {
        // given
        final Station 모란역 = new Station(1L, "모란역");
        final Station 암사역 = new Station(2L, "암사역");

        // then
        assertThatIllegalArgumentException().isThrownBy(() -> new Line("호선", color, 17L, 모란역, 암사역));
    }

    @Test
    void 상행종점역과_하행종점역이_같은_경우_예외를_발생시킨다() {
        // given
        final Station 모란역1 = new Station(1L, "모란역");
        final Station 모란역2 = new Station(1L, "모란역");

        // then
        assertThatIllegalArgumentException().isThrownBy(() -> new Line("8호선", "bg-pink-500", 17L, 모란역1, 모란역2));
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

    @Test
    void 노선의_정보를_변경_시킨다() {
        // given
        final Station 모란역 = new Station(1L, "모란역");
        final Station 암사역 = new Station(2L, "암사역");
        Line line = new Line("8호선", "bg-pink-500", 17L, 모란역, 암사역);

        // when
        line.changeInfo("2호선", "bg-lime-400");

        // then
        assertThat(line.getName()).isEqualTo("2호선");
        assertThat(line.getColor()).isEqualTo("bg-lime-400");
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {" ", ""})
    void 노선_이름_변경시_빈칸이거나_널이면_예외를_발생시킨다(String name) {
        // given
        final Station 모란역 = new Station(1L, "모란역");
        final Station 암사역 = new Station(2L, "암사역");
        Line line = new Line("8호선", "bg-pink-500", 17L, 모란역, 암사역);

        // then
        assertThatIllegalArgumentException().isThrownBy(() -> line.changeInfo(name, "bg-pink-500"));
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {" ", ""})
    void 노선_색상_변경시_빈칸이거나_널이면_예외를_발생시킨다(String color) {
        // given
        final Station 모란역 = new Station(1L, "모란역");
        final Station 암사역 = new Station(2L, "암사역");
        Line line = new Line("8호선", "bg-pink-500", 17L, 모란역, 암사역);

        // then
        assertThatIllegalArgumentException().isThrownBy(() -> line.changeInfo("2호선", color));
    }

}