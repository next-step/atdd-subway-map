package nextstep.subway.acceptance.testenum;

public enum TestLine {
    신분당선("신분당선", "bg-red-600"),
    이호선("2호선", "bg-green-600"),
    ;

    private String name;
    private String color;

    TestLine(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return this.name;
    }

    public String getColor() {
        return this.color;
    }
}
