package subway.model;

import subway.entity.Section;

public class SectionResponse {

    private Long id;

    private Long upStationId;

    private Long downStationId;

    private Integer distance;


    public SectionResponse(Long id, Long upStationId, Long downStationId, Integer distance) {
        this.id = id;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public Long getId() {
        return id;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public Integer getDistance() {
        return distance;
    }


    static public SectionResponse from(Section section) {
        return new SectionResponse(
            section.getId(),
            section.getUpStation().getId(),
            section.getDownStation().getId(),
            section.getDistance()
        );
    }
}
