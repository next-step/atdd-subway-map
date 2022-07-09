package nextstep.subway.domain.line;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class LineFactory {

    public static Line getMockLine(final Long id, final String name, final String color, final Integer distance) {
        return new Line(id, name, color, distance, new ArrayList<>());
    }
}
