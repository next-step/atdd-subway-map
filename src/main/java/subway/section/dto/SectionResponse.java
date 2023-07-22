package subway.section.dto;

import subway.section.domain.Section;

public class SectionResponse {
    private final Long id;
    private final String downStationId;
    private final String upStationId;
    private final Integer distance;

    private SectionResponse(Long id, String downStationId, String upStationId, Integer distance) {
        this.id = id;
        this.downStationId = downStationId;
        this.upStationId = upStationId;
        this.distance = distance;
    }

    public static SectionResponse fromEntity(Section section){
        return new SectionResponse(
                section.getId(),
                String.valueOf(section.getDownStationId()),
                String.valueOf(section.getUpStationId()),
                section.getDistance());
    }

    public Long getId() {
        return id;
    }

    public String getDownStationId() {
        return downStationId;
    }

    public String getUpStationId() {
        return upStationId;
    }

    public Integer getDistance() {
        return distance;
    }
}
