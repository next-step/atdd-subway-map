package subway.dto;

public class StationLineResponse {
    private final Long id;
    private final String name;
    private final String color;
    private final int upStationId;

    private final int downStationId;

    private final int distance;

    public StationLineResponse(Long id, String name, String color, int upStationId, int downStationId, int distance) {
        this.id = id;
        this.name = name;
        this.color = color;
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

    public String getColor() {
        return color;
    }

    public int getUpStationId() {
        return upStationId;
    }

    public int getDownStationId() {
        return downStationId;
    }

    public int getDistance() {
        return distance;
    }
}
