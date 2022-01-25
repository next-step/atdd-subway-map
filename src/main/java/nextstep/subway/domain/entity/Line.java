package nextstep.subway.domain.entity;

import nextstep.subway.domain.service.LineNameValidator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Line extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private int distance;

    public Line() {
    }

    public Line(final String name,
                final String color,
                final Long upStationId,
                final Long downStationId,
                final int distance,
                final LineNameValidator lineNameValidator) {
        lineNameValidator.validate(name);
        validateColor(color);

        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    private void validateColor(final String color) {
        if (Objects.isNull(color)) {
            throw new IllegalArgumentException("노선의 색상은 필수 입니다.");
        }
        if (color.isEmpty()) {
            throw new IllegalArgumentException("노선의 색상은 1 자 이상 이어야 합니다.");
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

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public int getDistance() {
        return distance;
    }

    public void change(final String name, final String color, final LineNameValidator validator) {
        validator.validate(name);
        validateColor(color);

        this.name = name;
        this.color = color;
    }
}
