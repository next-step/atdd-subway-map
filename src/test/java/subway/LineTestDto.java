package subway;

public class LineTestDto {
    private final String lineName;
    private final String lineColor;
    private final Long upStationId;
    private final Long downStationId;
    private final Long distance;

    public LineTestDto(String lineName, String lineColor, Long upStationId, Long downStationId, Long distance) {
        this.lineName = lineName;
        this.lineColor = lineColor;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public String getLineName() {
        return lineName;
    }

    public String getLineColor() {
        return lineColor;
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
