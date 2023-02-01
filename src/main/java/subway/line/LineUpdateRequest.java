package subway.line;

public class LineUpdateRequest {
    private String name;
    private String color;

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public LineUpdateRequest(String name, String color, Long distance) {
        this.name = name;
        this.color = color;
    }
}
