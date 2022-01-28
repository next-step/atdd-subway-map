package nextstep.subway.common.exception;

public enum ColumnName {
    LINE_NAME("노선 이름"),
    STATION_NAME("지하철역 이름");

    private final String name;

    ColumnName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
