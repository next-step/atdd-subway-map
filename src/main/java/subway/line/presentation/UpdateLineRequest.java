package subway.line.presentation;

import subway.common.Request;

public class UpdateLineRequest implements Request {
    private String name;
    private String color;

    public UpdateLineRequest(String name, String color) {
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
