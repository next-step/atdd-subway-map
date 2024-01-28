package subway.controller.dto;

public class LineUpdateRequestBody {
    private String name;
    private String color;

    public LineUpdateRequestBody(String name, String color) {
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
