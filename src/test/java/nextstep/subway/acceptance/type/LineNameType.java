package nextstep.subway.acceptance.type;

public enum LineNameType {
    NEW_BUN_DANG_LINE("신분당선", "bg-red-600"),
    SECOND_LINE("2호선", "bg-green-600");

    private final String lineName;
    private final String lineColor;

    LineNameType(String lineName, String lineColor) {
        this.lineName = lineName;
        this.lineColor = lineColor;
    }

    public String lineName() {
        return lineName;
    }

    public String lineColor() {
        return lineColor;
    }
}
