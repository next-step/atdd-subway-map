package subway.subway.domain;

import java.math.BigDecimal;

public class SubwaySection {

    private final SubwaySection.Id id;
    private final SubwaySectionStation startStation;
    private final SubwaySectionStation endStation;
    private final Kilometer distance;

    public static SubwaySection register(Station startStation, Station endStation, Kilometer kilometer) {
        SubwaySectionStation startSectionStation = SubwaySectionStation.from(startStation);
        SubwaySectionStation endSectionStation = SubwaySectionStation.from(endStation);
        return new SubwaySection(startSectionStation, endSectionStation, kilometer);
    }

    public static SubwaySection of(SubwaySection.Id id, SubwaySectionStation startStation, SubwaySectionStation endStation, Kilometer kilometer) {
        return new SubwaySection(id, startStation, endStation, kilometer);
    }

    private SubwaySection(SubwaySectionStation startStation, SubwaySectionStation endStation, Kilometer distance) {
        this.id = new SubwaySection.Id();
        this.startStation = startStation;
        this.endStation = endStation;
        this.distance = distance;
    }

    private SubwaySection(SubwaySection.Id id, SubwaySectionStation startStation, SubwaySectionStation endStation, Kilometer distance) {
        this.id = id;
        this.startStation = startStation;
        this.endStation = endStation;
        this.distance = distance;
    }

    public Station.Id getStartStationId() {
        return startStation.getId();
    }

    public String getStartStationName() {
        return startStation.getName();
    }

    public Station.Id getEndStationId() {
        return endStation.getId();
    }


    public String getEndStationName() {
        return endStation.getName();
    }

    public SubwaySection.Id getId() {
        if (id.isNew()) {
            throw new IllegalArgumentException("아직 저장되지 않은 지하철 역입니다.");
        }
        return id;
    }

    public Kilometer getDistance() {
        return distance;
    }

    public boolean isNew() {
        return id.isNew();
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
    }
}
