package nextstep.subway.applicaion.dto.line;

public class UpdateLineRequest {

    private final String name;

    private final String color;

    private UpdateLineRequest() {
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
