package subway.line.load;

public class LineLoadedStationResponse {

    private final Long id;
    private final String name;

    public LineLoadedStationResponse(Long id, String name) {
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
