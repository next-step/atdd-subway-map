package subway.line.dto;

public class LineStationReadResponse {
    private Long id;
    private String name;

    public LineStationReadResponse() {
    }

    public LineStationReadResponse(Long id, String name) {
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
