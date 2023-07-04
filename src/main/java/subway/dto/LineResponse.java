package subway.dto;

public class LineResponse {

    private Long id;

    private String color;

    private String name;

    private Long upStationId;

    private Long downStationId;

    private int distance;

    public LineResponse() {
    }

    public LineResponse(Long id, String color, String name, Long upStationId, Long downStationId, int distance) {
        this.id = id;
        this.color = color;
        this.name = name;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }
}
