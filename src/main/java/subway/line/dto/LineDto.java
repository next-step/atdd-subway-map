package subway.line.dto;

public class LineDto {
    private Long id;
    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private Long distance;

    private LineDto(String name, String color, Long upStationId, Long downStationId, Long distance) {
        this(null, name, color, upStationId, downStationId, distance);
    }

    private LineDto(Long id, String name, String color, Long upStationId, Long downStationId, Long distance) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public static LineDto from(LineRequest lineRequest) {
        return new LineDto(
                lineRequest.getName(),
                lineRequest.getColor(),
                lineRequest.getUpStationId(),
                lineRequest.getDownStationId(),
                lineRequest.getDistance()
        );
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
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
