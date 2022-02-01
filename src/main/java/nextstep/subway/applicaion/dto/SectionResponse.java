package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.Section;

public class SectionResponse {
    private Long id;
    private Long lineId;
    private StationResponse upStation;
    private StationResponse downStation;
    private int distance;

    public SectionResponse(Long id, Long lineId, StationResponse upStation, StationResponse downStation, int distance) {
        this.id = id;
        this.lineId = lineId;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public static SectionResponse of(Section section) {
        return new SectionResponse(
                section.getId(),
                section.getLine().getId(),
                StationResponse.of(section.getUpStation()),
                StationResponse.of(section.getDownStation()),
                section.getDistance()
        );
    }

    public Long getId() {
        return id;
    }

    public int getDistance() {
        return distance;
    }

    public StationResponse getDownStation() {
        return downStation;
    }

    public StationResponse getUpStation() {
        return upStation;
    }

    public Long getLineId() {
        return lineId;
    }
}
