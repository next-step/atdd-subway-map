package subway;

public class LineUpdateRequest {
    private String name;
    private String color;

    public LineUpdateRequest(String name,
                             String color) {
        this.name = name;
        this.color = color;
    }
}
