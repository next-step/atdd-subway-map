package nextstep.subway.acceptance.testenum;

public enum TestLine {
    LINE_NEW_BOONDANG("신분당선", "bg-red-600");

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
