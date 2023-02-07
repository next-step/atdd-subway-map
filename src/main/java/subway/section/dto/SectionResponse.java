package subway.section.dto;

import subway.section.repository.Section;
import subway.station.dto.StationResponse;
import subway.station.repository.Station;

public class SectionResponse {

    private Long id;
    private StationResponse downStation;
    private StationResponse upStation;
    private int distance;

    private SectionResponse(Long id, Station downStation, Station upStation, int distance) {
        this.id = id;
        this.downStation = new StationResponse(downStation.getId(), downStation.getName());
        this.upStation = new StationResponse(upStation.getId(), upStation.getName());
        this.distance = distance;
    }

    public static SectionResponse of(Section section) {
        return new SectionResponse(
                section.getId(),
                section.getDownStation(),
                section.getUpStation(),
                section.getDistance()
        );
    }

    public Long getId() {
        return id;
    }

    public StationResponse getDownStation() {
        return downStation;
    }

    public StationResponse getUpStation() {
        return upStation;
    }

    public int getDistance() {
        return distance;
    }
}
