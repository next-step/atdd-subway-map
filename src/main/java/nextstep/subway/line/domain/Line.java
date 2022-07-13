package nextstep.subway.line.domain;

import nextstep.subway.section.domain.Section;
import nextstep.subway.section.domain.Sections;

import javax.persistence.*;

@Entity
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String color;

    @Embedded
    private final Sections sections = new Sections();

    protected Line() {
    }

    public Line(final Long id, final String name, final String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public void modify(final String name, final String color) {
        this.name = name;
        this.color = color;
    }

    public void addSection(final Section section) {
        sections.addSection(section);
        section.setLine(this);
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

    public boolean isConnectable(final Section section) {
        return sections.isConnectable(section);
    }

    public boolean hasCircularSection(final Section section) {
        return sections.hasCircularSection(section);
    }

    public void removeLastSection(final Long stationId) {
        sections.removeLastSection(stationId);
    }

    public Long getFirstUpStationId() {
        return sections.getFirstUpStationId();
    }

    public Long getLastDownStationId() {
        return sections.getLastDownStationId();
    }

    public static class LineBuilder {
        private Long id;
        private String name;
        private String color;

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

        public Line build() {
            return new Line(id, name, color);
        }

    }

    public static Line.LineBuilder builder() {
        return new Line.LineBuilder();
    }

}
