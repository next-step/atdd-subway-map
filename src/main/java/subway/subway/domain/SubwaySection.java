package subway.subway.domain;

import java.math.BigDecimal;

public class SubwaySection {

    private SubwaySection.Id id;
    private final SubwaySectionStation startStation;
    private final SubwaySectionStation endStation;
    private final Kilometer kilometer;

    public static SubwaySection register(Station startStation, Station endStation, Kilometer kilometer) {
        SubwaySectionStation startSectionStation = new SubwaySectionStation(startStation);
        SubwaySectionStation endSectionStation = new SubwaySectionStation(endStation);
        return new SubwaySection(startSectionStation, endSectionStation, kilometer);
    }

    public static SubwaySection of(SubwaySection.Id id, SubwaySectionStation startStation, SubwaySectionStation endStation, Kilometer kilometer) {
        return new SubwaySection(id, startStation, endStation, kilometer);
    }

    private SubwaySection(SubwaySectionStation startStation, SubwaySectionStation endStation, Kilometer kilometer) {
        this.startStation = startStation;
        this.endStation = endStation;
        this.kilometer = kilometer;
    }

    private SubwaySection(SubwaySection.Id id, SubwaySectionStation startStation, SubwaySectionStation endStation, Kilometer kilometer) {
        this.id = id;
        this.startStation = startStation;
        this.endStation = endStation;
        this.kilometer = kilometer;
    }

    public Long getStartStationId() {
        return startStation.getId();
    }

    public Long getEndStationId() {
        return endStation.getId();
    }

    public Long getId() {
        if (isNew()) {
            throw new IllegalArgumentException("아직 저장되지 않은 지하철 역입니다.");
        }
        return id.getId();
    }

    public BigDecimal getKilometer() {
        return kilometer.getKilometer();
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
