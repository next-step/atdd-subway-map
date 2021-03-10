package nextstep.subway.line.dto;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

import java.time.LocalDateTime;

public class SectionResponse {
    private Long id;
    private Line line;
    private Station upStation;
    private Station downStation;
    private int distance;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public SectionResponse() {}

    public SectionResponse(Long id, Line line, Station upStation, Station downStation, int distance, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.id = id;
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

    public static SectionResponse of(Section section) {
        return new SectionResponse(section.getId(), section.getLine(), section.getUpStation(), section.getDownStation(), section.getDistance(), section.getCreatedDate(), section.getModifiedDate());
    }

    public Long getId() {
        return id;
    }
}
