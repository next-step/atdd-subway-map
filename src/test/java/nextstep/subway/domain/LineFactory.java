package nextstep.subway.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class LineFactory {

    public static Line getMockLine(final Long id, final String name, final String color) {
        return new Line(id, name, color);
    }
}
