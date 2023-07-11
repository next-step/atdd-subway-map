package subway.subway.domain;

import java.util.List;
import java.util.Objects;

public class SubwayLine {
    private final SubwayLine.Id id;

    private String name;

    private String color;
    private final SubwaySectionList sectionList;

    public static SubwayLine register(String name, String color, Station upStation, Station downStation, Kilometer kilometer) {
        SubwaySection subwaySection = SubwaySection.register(upStation, downStation, kilometer);

        return new SubwayLine(name, color, subwaySection);
    }

    public static SubwayLine of(SubwayLine.Id id, String name, String color, List<SubwaySection> sectionList) {
        return new SubwayLine(id, name, color, new SubwaySectionList(sectionList));
    }

    private SubwayLine(Id id, String name, String color, SubwaySectionList sectionList) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.sectionList = sectionList;
    }

    private SubwayLine(String name, String color, SubwaySection section) {
        this.id = new SubwayLine.Id();
        this.name = name;
        this.color = color;
        this.sectionList = new SubwaySectionList(section);
    }

    public SubwayLine.Id getId() {
        if (isNew()) {
            throw new IllegalArgumentException("아직 저장되지 않은 지하철 노선입니다.");
        }
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public List<SubwaySection> getSectionList() {
        return sectionList.getSections();
    }

    public boolean isNew() {
        return id.isNew();
    }

    public void update(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public static class Id {
        private Long id;

        public Id(Long id) {
            this.id = id;
        }

        public Id() {
        }

        public Long getValue() {
            return id;
        }

        public boolean isNew() {
            return id == null;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            SubwayLine.Id idObject = (SubwayLine.Id) o;
            return Objects.equals(id, idObject.id);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
        }
    }
}
