package subway;

public class LinesUpdateRequest {
    private String name;
    private String color;

    public LinesUpdateRequest(String name, String color) {
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
