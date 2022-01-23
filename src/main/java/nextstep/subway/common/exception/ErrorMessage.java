package nextstep.subway.common.exception;

public enum ErrorMessage {
    ENTITY_NOT_FOUND("요청하신 데이터를 찾을 수 없습니다."),
    DUPLICATE_COLUMN("이미 존재하는 %s 입니다."),
    NOT_FOUND_SECTION_DOCKING_POINT("새로운 구간의 상행역은 현재 등록되어있는 하행의 종점역이어야 합니다."),
    ALREADY_REGISTERED_STATION_IN_SECTION("새로운 구간의 하행역이 이미 등록되어 있습니다.");

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getMessage(Object... objs) {
        return String.format(message, objs);
    }
}
