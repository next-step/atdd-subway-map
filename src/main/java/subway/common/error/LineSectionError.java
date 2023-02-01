package subway.common.error;

public enum LineSectionError {
    NO_REGISTER_UP_STATION("요청한 상행종점역은 해당 노선에 등록되어 있지 않아서 추가 불가능합니다."),
    NO_REGISTER_DOWN_STATION("요청한 하행종점역은 이미 노선에 등록되어 있어서 추가가 불가능합니다."),
    NO_DELETE_ONE_SECTION("노선의 구간 목록수가 1개인 경우 삭제할 수 없습니다."),
    NO_LAST_SECTION("노선의 구간이 존재하지 않아서 불가능합니다."),
    NO_REGISTER_LAST_LINE_STATION("요청한 역으로 등록된 마지막 구간이 존재하지 않습니다."),
    ;

    private final String message;

    LineSectionError(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
