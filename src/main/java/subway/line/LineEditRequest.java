package subway.line;

public class LineEditRequest {

    private String name;
    private String color;

    public LineEditRequest(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }
}
