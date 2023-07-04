package subway.subway.domain;

import java.util.List;

public class SubwayLine {
    private SubwayLine.Id id;

    private final String name;

    private final String color;
    private final SubwaySectionList stations;

    private SubwayLine(String name, String color, SubwaySectionList stations) {
        this.name = name;
        this.color = color;
        this.stations = stations;
    }

    public static SubwayLine register(String name, String color, SubwaySectionList stations) {
        return new SubwayLine(name, color, stations);
    }

    public Long getId() {
        if (isNew()) {
            throw new IllegalArgumentException("아직 저장되지 않은 역입니다.");
        }
        return id.getId();
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public List<SubwaySection> getStations() {
        return stations.getSections();
    }

    public boolean isNew() {
        return id == null;
    }

    public static class Id {
        private final Long id;

        public Id(Long id) {
            this.id = id;
        }

        public Long getId() {
            return id;
        }
    }
}
