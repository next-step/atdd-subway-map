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
        Line line = new Line("8호선", "bg-pink-500");

        // then
        assertAll(() -> {
            assertThat(line.getName()).isEqualTo("8호선");
            assertThat(line.getColor()).isEqualTo("bg-pink-500");
        });
    }

    @Test
    void 노선의_정보를_변경_시킨다() {
        // given
        Line line = new Line("8호선", "bg-pink-500");

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
        Line line = new Line("8호선", "bg-pink-500");

        // then
        assertThatIllegalArgumentException().isThrownBy(() -> line.changeInfo(name, "bg-pink-500"));
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {" ", ""})
    void 노선_색상_변경시_빈칸이거나_널이면_예외를_발생시킨다(String color) {
        // given
        Line line = new Line("8호선", "bg-pink-500");

        // then
        assertThatIllegalArgumentException().isThrownBy(() -> line.changeInfo("2호선", color));
    }

    @Test
    void 구간을_추가한다() {
        // given
        final Station 모란역 = new Station(1L, "모란역");
        final Station 암사역 = new Station(2L, "암사역");
        Line line = new Line("8호선", "bg-pink-500");

        // when
        line.addSection(new Section(17L, 모란역, 암사역));

        // then
        assertThat(line.getStations()).containsExactly(모란역, 암사역);
    }

    @Test
    void 구간을_추가할_때_같은_구간이면_예외를_발생시킨다() {
        // given
        final Station 모란역 = new Station(1L, "모란역");
        final Station 암사역 = new Station(2L, "암사역");
        Line line = new Line("8호선", "bg-pink-500");
        line.addSection(new Section(17L, 모란역, 암사역));

        final Station 모란역1 = new Station(1L, "모란역");
        final Station 암사역2 = new Station(2L, "암사역");
        Section section = new Section(10L, 모란역1, 암사역2);

        // then
        assertThatIllegalArgumentException().isThrownBy(() ->
                line.addSection(section)
        );
    }

    @Test
    void 새로운_구간의_상행역은_해당_노선에_등록되어있는_하행_종점역이_아니면_예외를_발생시킨다() {
        // given
        final Station 모란역 = new Station(1L, "모란역");
        final Station 암사역 = new Station(2L, "암사역");
        Line line = new Line("8호선", "bg-pink-500");
        line.addSection(new Section(17L, 모란역, 암사역));

        final Station 모란역1 = new Station(1L, "모란역");
        final Station 송파역 = new Station(3L, "송파역");
        Section section = new Section(10L, 모란역1, 송파역);

        // then
        assertThatIllegalArgumentException().isThrownBy(() ->
                line.addSection(section)
        );
    }

    @Test
    void 새로운_구간의_하행역은_해당_노선에_등록되어있는_역이면_예외를_발생시킨다() {
        // given
        final Station 모란역 = new Station(1L, "모란역");
        final Station 암사역 = new Station(2L, "암사역");
        Line line = new Line("8호선", "bg-pink-500");
        line.addSection(new Section(17L, 모란역, 암사역));

        final Station 송파역 = new Station(3L, "송파역");
        final Station 모란역1 = new Station(4L, "모란역");
        Section section = new Section(10L, 모란역1, 송파역);

        // then
        assertThatIllegalArgumentException().isThrownBy(() ->
                line.addSection(section)
        );
    }

}