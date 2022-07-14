package nextstep.subway.factory;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import nextstep.subway.domain.Line;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LineFactory {
    public static Line mock(String name, String color) {
        return Line.builder()
                   .name(name)
                   .color(color)
                   .downStationId(11L)
                   .upStationId(12L)
                   .distance(10).build();
    }

}
