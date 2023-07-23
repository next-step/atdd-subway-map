package subway.line.dto.response;

import subway.line.domain.Section;

public class SectionResponse {

    private final Long id;
    private final Long stationId;

    public SectionResponse(Long id, Long stationId) {
        this.id = id;
        this.stationId = stationId;
    }

    public static SectionResponse of(Section section) {
        return new SectionResponse(section.getId(), section.getStation().getId());
    }

    public Long getId() {
        return id;
    }

    public Long getStationId() {
        return stationId;
    }

    @Override
    public String toString() {
        return "SectionResponse{" +
                "id=" + id +
                ", stationId=" + stationId +
                '}';
    }

}
