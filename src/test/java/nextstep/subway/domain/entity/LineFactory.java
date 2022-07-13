package nextstep.subway.domain.entity;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class LineFactory {

    public static Line getMockLine(final Long id, final String name, final String color, final Integer distance) {
        return new Line(id, name, color, distance);
    }
}
