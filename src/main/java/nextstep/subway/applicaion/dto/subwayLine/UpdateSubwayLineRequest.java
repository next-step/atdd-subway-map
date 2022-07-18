package nextstep.subway.applicaion.dto.subwayLine;

public class UpdateSubwayLineRequest {

    private final String name;

    private final String color;

    private UpdateSubwayLineRequest() {
        name = null;
        color = null;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }
}
