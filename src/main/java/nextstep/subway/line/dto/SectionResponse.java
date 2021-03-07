package nextstep.subway.line.dto;

import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

import java.time.LocalDateTime;
import java.util.List;

public class SectionResponse {
    private Long id;
    private Station upStation;
    private Station downStation;
    private int distance;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public SectionResponse() {
    }

    public SectionResponse(Long id, Station upStation, Station downStation, int distance, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.id = id;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

    public static SectionResponse of(Section section) {
        return new SectionResponse(section.getId(), section.getUpStation(), section.getDownStation(),section.getDistance(), section.getCreatedDate(), section.getModifiedDate());
    }

    public Long getId() {
        return id;
    }

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
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
