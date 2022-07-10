package nextstep.subway.ui.dto.line;

public class UpdateLineRequest {
    private final String name;
    private final String color;

    public UpdateLineRequest(final String name, final String color) {
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
