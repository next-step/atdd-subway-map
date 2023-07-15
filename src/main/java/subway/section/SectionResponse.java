package subway.section;

import subway.station.StationResponse;

public class SectionResponse {
    private Long id;
    private StationResponse upStation;
    private StationResponse downStation;

    public SectionResponse() {
    }

    public SectionResponse(Long id, StationResponse upStation, StationResponse downStation) {
        this.id = id;
        this.upStation = upStation;
        this.downStation = downStation;
    }

    public static SectionResponse of(Section section) {
        return new SectionResponse(section.getId(), StationResponse.of(section.getUpStation()), StationResponse.of(section.getDownStation()));
    }

    public Long getId() {
        return id;
    }

    public StationResponse getUpStation() {
        return upStation;
    }

    public StationResponse getDownStation() {
        return downStation;
    }
}
