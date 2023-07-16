package subway.dto;

public class SectionResponse {

    private Long id;
    private Long lineId;
    private Long upStationId;
    private Long downStationId;

    public SectionResponse() {

    }

    public Long getId() {
        return id;
    }

    public Long getLineId() {
        return lineId;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }
}
