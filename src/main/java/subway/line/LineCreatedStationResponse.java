package subway.line;

public class LineCreatedStationResponse {

    private final Long id;
    private final String name;

    public LineCreatedStationResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
