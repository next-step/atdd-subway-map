package nextstep.subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class LineTest {

    @DisplayName("LineUpdateRequest 를 통해 line 을 바꿀 수 있다.")
    @Test
    void changeLine() {
        //given
        List<Station> stations = List.of(new Station("역삼역"));
        Line line = new Line("name", "color", stations);

        //when
        Line changedLine = line.changeBy("new name", "new color");

        //then
        assertAll(
                () -> assertThat(changedLine.getName()).isEqualTo("new name"),
                () -> assertThat(changedLine.getColor()).isEqualTo("new color")
        );
    }

}