package subway.subway.domain;

import java.util.List;

public class SubwayLine {
    private SubwayLine.Id id;

    private final String name;

    private final String color;
    private final SubwaySectionList sectionList;

    private SubwayLine(String name, String color, SubwaySectionList sectionList) {
        this.name = name;
        this.color = color;
        this.sectionList = sectionList;
    }

    public static SubwayLine register(String name, String color, SubwaySection... sections) {
        return new SubwayLine(name, color, new SubwaySectionList(sections));
    }

    public SubwayLine.Id getId() {
        if (isNew()) {
            throw new IllegalArgumentException("아직 저장되지 않은 역입니다.");
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
