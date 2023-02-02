package subway;

public class LineResponse {
    private final Long id;
    private final String name;
    private final Long upStationId;
    private final Long downStationId;

    public LineResponse(Line line) {
        this.id = line.getId();
        this.name = line.getName();
        this.upStationId = line.getUpStationId();
        this.downStationId = line.getDownStationId();
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
