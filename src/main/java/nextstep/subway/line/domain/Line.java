package nextstep.subway.line.domain;

import nextstep.subway.section.domain.Section;

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

    protected Line() {
    }

    public Line(final Long id, final String name, final String color, final Long upStationId, final Long downStationId, final Long distance) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
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

    public boolean isConnectableSection(final Section section) {
        return downStationId.equals(section.getUpStationId());
    }

    public static class LineBuilder {
        private Long id;
        private String name;
        private String color;
        private Long upStationId;
        private Long downStationId;
        private Long distance;

        public Line.LineBuilder id(final Long id) {
            this.id = id;
            return this;
        }

        public Line.LineBuilder name(final String name) {
            this.name = name;
            return this;
        }

        public Line.LineBuilder color(final String color) {
            this.color = color;
            return this;

        }
        public Line.LineBuilder upStationId(final Long upStationId) {
            this.upStationId = upStationId;
            return this;

        }

        public Line.LineBuilder downStationId(final Long downStationId) {
            this.downStationId = downStationId;
            return this;

        }

        public Line.LineBuilder distance(final Long distance) {
            this.distance = distance;
            return this;
        }

        public Line build() {
            return new Line(id, name, color, upStationId, downStationId, distance);
        }

    }

    public static Line.LineBuilder builder() {
        return new Line.LineBuilder();
    }

}
