package nextstep.subway.application.dto;

import nextstep.subway.domain.Section;

public class SectionResponse {
    private final Long id;
    private final LineResponse line;
    private final StationResponse upStation;
    private final StationResponse downStation;
    private final int distance;

    private SectionResponse(Long id, LineResponse line, StationResponse upStation, StationResponse downStation, int distance) {
        this.id = id;
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public static SectionResponse fromSection(Section section) {
        return new SectionResponse(
                section.getId(),
                LineResponse.fromLine(section.getLine()),
                StationResponse.fromStation(section.getUpStation()),
                StationResponse.fromStation(section.getDownStation()),
                section.getDistance()
        );
    }

    public Long getId() {
        return id;
    }

    public LineResponse getLine() {
        return line;
    }

    public StationResponse getUpStation() {
        return upStation;
    }

    public StationResponse getDownStation() {
        return downStation;
    }

    public int getDistance() {
        return distance;
    }
}
