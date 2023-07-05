package subway.line;

public class LineUpdateRequest {
    private final String name;
    private final String color;

    public LineUpdateRequest(String name, String color) {
        this.name = name;
        this.color = color;
    }
}
