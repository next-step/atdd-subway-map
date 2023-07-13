package subway.subway.domain;

import java.util.List;
import java.util.Objects;

public class SubwayLine {
    private final SubwayLine.Id id;

    private String name;

    private String color;
    private Station.Id startStationId;
    private final SubwaySections sections;

    public static SubwayLine register(String name, String color, SubwaySection subwaySection) {

        SubwayLine subwayLine = new SubwayLine(name, color, subwaySection);
        subwayLine.validate();
        return subwayLine;
    }

    public static SubwayLine of(SubwayLine.Id id, String name, String color, Station.Id startStationId, List<SubwaySection> sectionList) {
        return new SubwayLine(id, name, color, startStationId, new SubwaySections(sectionList));
    }

    private SubwayLine(Id id, String name, String color, Station.Id startStationId, SubwaySections sections) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.startStationId = startStationId;
        this.sections = sections;
    }

    private SubwayLine(String name, String color, SubwaySection section) {
        this.id = new SubwayLine.Id();
        this.name = name;
        this.color = color;
        this.startStationId = section.getUpStationId();
        this.sections = new SubwaySections(section);
    }

    private void validate() {
        sections.validate(startStationId);
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


    public Station.Id getStartStationId() {
        return startStationId;
    }

    public boolean isNew() {
        return id.isNew();
    }

    public void update(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public void addSection(SubwaySection subwaySection, SectionOperateManager manager) {
        sections.update(subwaySection, manager);
        validate();
    }

    public boolean existsUpStation(Station.Id stationId) {
        return sections.existsUpStation(stationId);
    }

    public List<SubwaySection> getSections() {
        return sections.getSections();
    }

    public int getSectionSize() {
        return sections.size();
    }

    public static class Id {
        private Long id;

        public Id(Long id) {
            this.id = id;
        }

        private Id() {
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
