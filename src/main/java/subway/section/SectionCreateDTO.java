package subway.section;

public class SectionCreateDTO {

    private Long lineId;
    private Long upStationId;
    private Long downStationId;
    private Long distance;

    public SectionCreateDTO() {
    }

    public SectionCreateDTO(Long lineId, SectionCreateRequest request) {
        this.lineId = lineId;
        this.upStationId = request.getUpStationId();
        this.downStationId = request.getDownStationId();
        this.distance = request.getDistance();
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

    public Long getDistance() {
        return distance;
    }
}
