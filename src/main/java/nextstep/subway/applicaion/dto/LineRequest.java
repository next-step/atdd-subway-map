package nextstep.subway.applicaion.dto;

public class LineRequest {
    private String name;
    private String color;

    public LineRequest() {
    }

    public LineRequest(LineRequest lineRequest) {
        this(lineRequest.getName(), lineRequest.getColor());
    }

    public LineRequest(String name, String color) {
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
