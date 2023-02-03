package subway.dto.line;

public class LineStationReadListResponse {
    private Long id;
    private String name;

    public LineStationReadListResponse() {
    }

    public LineStationReadListResponse(Long id, String name) {
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
