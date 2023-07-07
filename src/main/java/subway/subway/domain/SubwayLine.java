package subway.subway.domain;

import java.util.List;

public class SubwayLine {
    private SubwayLine.Id id;

    private String name;

    private String color;
    private final SubwaySectionList sectionList;

    public static SubwayLine register(String name, String color, SubwaySection... sections) {
        return new SubwayLine(name, color, new SubwaySectionList(sections));
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

    private SubwayLine(String name, String color, SubwaySectionList sectionList) {
        this.name = name;
        this.color = color;
        this.sectionList = sectionList;
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
        return id == null;
    }

    public void update(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public static class Id {
        private final Long id;

        public Id(Long id) {
            this.id = id;
        }

        public Long getValue() {
            return id;
        }
    }
}
