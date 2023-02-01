package subway.dto;

public class LineEditRequest {

    private String name;
    private String color;
    private int distance;

    public LineEditRequest() {
    }

    public LineEditRequest(final String name, final String color, final int distance) {
        this.name = name;
        this.color = color;
        this.distance = distance;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public int getDistance() {
        return distance;
    }
}
