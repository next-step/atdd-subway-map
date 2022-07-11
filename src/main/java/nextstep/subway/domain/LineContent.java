package nextstep.subway.domain;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LineContent {

    private String name;
    private String color;

    private LineContent(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public static LineContent create(String name, String color) {
        return new LineContent(name, color);
    }

    public String name() {
        return name;
    }

    public String color() {
        return color;
    }
}
