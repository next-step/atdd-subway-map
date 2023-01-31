package subway.line;

public enum MockLine {
    신분당선("신분당선", "bg-red-600", 1L, 2L, 10),
    분당선("분당선", "bg-green-600", 2L, 3L, 5),
    ;

    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private int distance;

    MockLine(String name, String color, Long upStationId, Long downStationId, int distance) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
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

    public int getDistance() {
        return distance;
    }
}
