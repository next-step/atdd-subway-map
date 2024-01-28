package subway.line;

public class LineRequest {
    private Long id;
    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;

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
}
