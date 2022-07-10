package nextstep.subway.applicaion.dto.subwayLine;

public class UpdateSubwayLineRequest {

    private String name;

    private String color;

    public UpdateSubwayLineRequest(String name, String color) {
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
