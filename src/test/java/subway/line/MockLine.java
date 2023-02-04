package subway.line;

import subway.common.Mock;

public enum MockLine implements Mock {
    신분당선("신분당선", "bg-red-600"),
    분당선("분당선", "bg-green-600"),
    ;

    private String name;
    private String color;

    MockLine(String name, String color) {
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
