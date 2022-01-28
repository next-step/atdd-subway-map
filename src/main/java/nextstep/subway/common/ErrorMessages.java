package nextstep.subway.common;

public enum ErrorMessages {
    NOT_FOUND_LINE("노선이 존재하지 않습니다."),
    NOT_FOUND_STATION("지하철역이 존재하지 않습니다."),
    NOT_FOUND_SECTION("지하철 구간이 존재하지 않습니다."),
    DUPLICATE_LINE_NAME("동일한 노선 이름이 존재 합니다."),
    DUPLICATE_STATION_NAME("동일한 지하철역 이름이 존재 합니다.");

    private String message;

    ErrorMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
