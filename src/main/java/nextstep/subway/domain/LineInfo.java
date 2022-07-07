package nextstep.subway.domain;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class LineInfo {
    private String name;
    private String color;
    private long distance;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LineInfo lineInfo = (LineInfo) o;
        return distance == lineInfo.distance && Objects.equals(name, lineInfo.name) && Objects.equals(color, lineInfo.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, color, distance);
    }
}
