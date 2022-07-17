package nextstep.subway.domain;

import java.util.Objects;
import javax.persistence.Embeddable;

@Embeddable
public class LineContent {

    private String name;
    private String color;

    protected LineContent() {
    }

    public LineContent(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public String color() {
        return color;
    }

    public String name() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LineContent that = (LineContent) o;
        return Objects.equals(name, that.name) && Objects.equals(color, that.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, color);
    }
}
