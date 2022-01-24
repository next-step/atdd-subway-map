package nextstep.subway.model;

public enum Line {

    신분당선("bg-red-600"),
    이호선("bg-green-600"),
    ;

    private final String color;

    Line(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }
}
