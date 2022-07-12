package nextstep.subway.domain.line;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class LineTest {

    @Test
    @DisplayName("Line 등록시 이름은 필수 값이어야 한다.")
    void lineRequiredNameTest() {

        assertThatNullPointerException()
                .isThrownBy(() -> createMockLine("name"));
    }

    @Test
    @DisplayName("Line 등록시 색상은 필수 값이어야 한다.")
    void lineRequiredColorTest() {
        assertThatNullPointerException()
                .isThrownBy(() -> createMockLine("color"));
    }

    @Test
    @DisplayName("Line 등록시 거리는 필수 값이어야 한다.")
    void lineRequiredDistanceTest() {
        assertThatNullPointerException()
                .isThrownBy(() -> createMockLine("distance"));
    }


    private Line createMockLine(String ignoreFieldName) {
        Line line = Line.builder()
                .name("name")
                .color("color")
                .distance(10)
                .build();

        switch (ignoreFieldName) {
            case "name": {
                return line.toBuilder()
                        .name(null)
                        .build();
            }
            case "color": {
                return line.toBuilder()
                        .color(null)
                        .build();
            }
            case "distance": {
                return line.toBuilder()
                        .distance(null)
                        .build();
            }
        }
        return line;
    }

}