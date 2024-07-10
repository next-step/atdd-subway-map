package subway;

public final class SubwayLineUpdateRequest {
    private final String name;
    private final String color;

    public SubwayLineUpdateRequest(String name, String color) {
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
