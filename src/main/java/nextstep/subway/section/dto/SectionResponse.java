package nextstep.subway.section.dto;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.section.domain.Section;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

import java.time.LocalDateTime;

public class SectionResponse {
    private final long id;
    private final LineResponse line;
    private final StationResponse upStation;
    private final StationResponse downStation;
    private final int distance;
    private final LocalDateTime createdDate;
    private final LocalDateTime modifiedDate;

    private SectionResponse(long id, LineResponse line, StationResponse upStation, StationResponse downStation, int distance, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.id = id;
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

    public static SectionResponse of(long id, Line line, Station upStation, Station downStation, int distance, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        return new SectionResponse(
            id,
            LineResponse.of(line),
            StationResponse.of(upStation),
            StationResponse.of(downStation),
            distance,
            createdDate,
            modifiedDate
        );
    }

    public static SectionResponse of(Section section) {
        return of(
            section.getId(),
            section.getLine(),
            section.getUpStation(),
            section.getDownStation(),
            section.getDistance(),
            section.getCreatedDate(),
            section.getModifiedDate()
        );
    }

    public long getId() {
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

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }


}
