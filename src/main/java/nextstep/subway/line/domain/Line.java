package nextstep.subway.line.domain;

import nextstep.subway.section.domain.Section;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String color;

    // TODO(MinKyu): fetch join
    @OneToMany(mappedBy = "line", cascade = CascadeType.ALL)
    private List<Section> sectionList = new ArrayList<>();

    protected Line() {
    }

    public Line(final Long id, final String name, final String color, final List<Section> sectionList) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.sectionList = sectionList;
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

    public List<Section> getSectionList() {
        return sectionList;
    }

    public boolean isConnectable(final Section section) {
        if (sectionList.isEmpty()) {
            return true;
        }

        return isConnectableSection(section);
    }

    private boolean isConnectableSection(final Section section) {
        return getLastDownStationId().equals(section.getUpStationId());
    }

    public Long getFirstUpStationId() {
        if (sectionList.isEmpty()) {
            throw new IllegalStateException("Section이 없습니다.");
        }
        return sectionList.get(0).getUpStationId();
    }

    public Long getLastDownStationId() {
        if (sectionList.isEmpty()) {
            throw new IllegalStateException("Section이 없습니다.");
        }
        return sectionList.get(sectionList.size() - 1).getDownStationId();
    }

    public void addSection(final Section section) {
        sectionList.add(section);
        section.setLine(this);
    }

    public void modify(final String name, final String color) {
        this.name = name;
        this.color = color;
    }

    public boolean hasCircularSection(final Section section) {
        return sectionList.stream()
                .anyMatch(section::isDuplicated);
    }

    public static class LineBuilder {
        private Long id;
        private String name;
        private String color;
        private List<Section> sectionList = new ArrayList<>();

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

        public Line.LineBuilder sectionList(final List<Section> sectionList) {
            this.sectionList = sectionList;
            return this;
        }

        public Line build() {
            return new Line(id, name, color, sectionList);
        }

    }

    public static Line.LineBuilder builder() {
        return new Line.LineBuilder();
    }

}
