package nextstep.subway.domain.line;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private Long distance;

    public Line() {
    }

    public Line(Builder builder) {
        this.name = builder.name;
        this.color = builder.color;
        this.upStationId = builder.upStationId;
        this.downStationId = builder.downStationId;
        this.distance = builder.distance;
    }

    public void update(String name, String color) {
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

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public Long getDistance() {
        return distance;
    }

    public static class Builder {
        private String name;
        private String color;
        private Long upStationId;
        private Long downStationId;
        private Long distance;

        public Builder() {
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Builder color(String val) {
            color = val;
            return this;
        }

        public Builder upStationId(Long val) {
            upStationId = val;
            return this;
        }

        public Builder downStationId(Long val) {
            downStationId = val;
            return this;
        }

        public Builder distance(Long val) {
            distance = val;
            return this;
        }

        public Line build() {
            return new Line(this);
        }
    }
}
