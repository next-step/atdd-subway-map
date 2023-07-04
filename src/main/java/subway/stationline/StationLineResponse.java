package subway.stationline;

public class StationLineResponse {

    private Long id;
    private String name;
    private Long upStationId;
    private Long downStationId;
    private int distance;

    public StationLineResponse() {
    }

    public StationLineResponse(Long id, String name, Long upStationId, Long downStationId,
        int distance) {
        this.id = id;
        this.name = name;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public static StationLineResponse of(final StationLine entity) {
        return new StationLineResponse(
            entity.getId(),
            entity.getName(),
            entity.getUpStationId(),
            entity.getDownStationId(),
            entity.getDistance()
        );
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

    public int getDistance() {
        return distance;
    }
}
