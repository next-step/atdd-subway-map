package nextstep.subway.line.dto;

import nextstep.subway.line.domain.Section;

public class SectionResponse {
    private Long upStationId;
    private Long downStationId;
    private int distance;

    public SectionResponse() {
    }

    public SectionResponse(Long upStationId, Long downStationId, int distance) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public Long getUpStationId() { return this.upStationId; }

    public Long getDownStationId() { return this.downStationId; }

    public int getDistance() { return this.distance; }

    public static SectionResponse of(Section section) {
        //return new SectionResponse(section.getUpStation(), section.getDownStation(), section.getDistance());
        return null;
    }

    public Section toSection() {

        //return new Section(this.upStationId, this.downStationId, this.distance);
        return null;
    }
}
