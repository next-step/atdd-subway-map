package subway;

public class LineTestDto {
    private final String lineName = "신분당선";
    private final String lineColor = "bg-red-600";
    private final Long upStationId = 1L;
    private final Long downStationId = 2L;
    private final Long distance = 10L;

    public LineTestDto() {
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
