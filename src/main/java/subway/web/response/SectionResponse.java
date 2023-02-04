package subway.web.response;

import subway.domain.Section;

import java.util.Objects;

public class SectionResponse {

    private Long id;
    private StationResponse downStation;
    private StationResponse upStation;
    private Long distance;
    private LineResponse lineResponse;

    public SectionResponse(Long id, StationResponse downStation, StationResponse upStation, Long distance, LineResponse lineResponse) {
        this.id = id;
        this.downStation = downStation;
        this.upStation = upStation;
        this.distance = distance;
        this.lineResponse = lineResponse;
    }

    public static SectionResponse from(Section section) {
        return new SectionResponse(section.getId(), StationResponse.from(section.getDownStation()), StationResponse.from(section.getUpStation()), section.getDistance(), LineResponse.from(section.getLine()));
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

    public Long getDistance() {
        return distance;
    }

    public LineResponse getLineResponse() {
        return lineResponse;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SectionResponse that = (SectionResponse) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
