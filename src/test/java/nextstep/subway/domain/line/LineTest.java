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
    @DisplayName("Line 등록시 상행역 id는 필수 값이어야 한다.")
    void lineRequiredUpStationIdTest() {
        assertThatNullPointerException()
                .isThrownBy(() -> createMockLine("upStationId"));
    }

    @Test
    @DisplayName("Line 등록시 하행역 id는 필수 값이어야 한다.")
    void lineRequiredDownStationIdTest() {
        assertThatNullPointerException()
                .isThrownBy(() -> createMockLine("downStationId"));
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
                .upStationId(1L)
                .downStationId(2L)
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
            case "upStationId": {
                return line.toBuilder()
                        .upStationId(null)
                        .build();
            }
            case "downStationId": {
                return line.toBuilder()
                        .downStationId(null)
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