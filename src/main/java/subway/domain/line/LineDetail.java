package subway.domain.line;

import subway.exception.ColorNotAvailableException;
import subway.exception.NameNotAvailableException;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class LineDetail {

    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 20, nullable = false)
    private String color;

    private int distance;

    protected LineDetail() {
    }

    public LineDetail(String name, String color, int distance) {
        validate(name, color);
        this.name = name;
        this.color = color;
        this.distance = distance;
    }

    public void modify(String name, String color) {
        validate(name, color);
        this.name = name;
        this.color = color;
    }

    private void validate(String name, String color) {
        validateName(name);
        validateColor(color);
    }

    private void validateName(String name) {
        if (name == null || name.isEmpty()) {
            throw new NameNotAvailableException();
        }
    }

    private void validateColor(String color) {
        if (color == null || color.isEmpty()) {
            throw new ColorNotAvailableException();
        }
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }
}
