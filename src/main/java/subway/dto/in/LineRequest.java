package subway.dto.in;

public class LineRequest {
    private Long id;
    private String name;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LineRequest() {}

    public LineRequest(String name) {
        this.name = name;
    }

    public LineRequest(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
