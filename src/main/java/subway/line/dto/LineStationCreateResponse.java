package subway.line.dto;

public class LineStationCreateResponse {
    private Long id;
    private String name;

    public LineStationCreateResponse() {
    }

    public LineStationCreateResponse(Long id, String name) {
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
