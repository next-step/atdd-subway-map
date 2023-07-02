package subway.line.dto;

public class StationOnLineResponse {
    private final Long id;
    private final String name;

    public StationOnLineResponse(Long id, String name) {
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
