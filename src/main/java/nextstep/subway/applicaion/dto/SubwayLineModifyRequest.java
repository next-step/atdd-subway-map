package nextstep.subway.applicaion.dto;

public class SubwayLineModifyRequest {
    private String name;
    private String color;

    public SubwayLineModifyRequest(String name, String color) {
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
