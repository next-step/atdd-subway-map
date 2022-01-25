package nextstep.subway.domain;

import nextstep.subway.exception.line.LineBlankColorException;
import nextstep.subway.exception.line.LineBlankNameException;
import org.apache.logging.log4j.util.Strings;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String color;

    protected Line() {
    }

    public Line(String name, String color) {
        validateBlankName(name);
        validateBlankColor(color);
        this.name = name;
        this.color = color;
    }

    private void validateBlankName(final String name) {
        if (Strings.isBlank(name)) {
            throw new LineBlankNameException();
        }
    }

    private void validateBlankColor(final String color) {
        if (Strings.isBlank(color)) {
            throw new LineBlankColorException();
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public void update(final String name, final String color) {
        this.name = name;
        this.color = color;
    }
}
