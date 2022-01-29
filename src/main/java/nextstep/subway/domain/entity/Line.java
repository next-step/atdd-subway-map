package nextstep.subway.domain.entity;

import nextstep.subway.domain.service.LineValidator;

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

    public Line() {
    }

    public Line(final String name,
                final String color,
                final LineValidator lineValidator) {
        lineValidator.validateLine(name, color);

        this.name = name;
        this.color = color;
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

    public void change(final String name, final String color, final LineValidator lineValidator) {
        lineValidator.validateLine(name, color);

        this.name = name;
        this.color = color;
    }
}
