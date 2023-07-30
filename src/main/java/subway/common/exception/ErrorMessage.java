package subway.common.exception;

public enum ErrorMessage {
    NOT_FOUND_LINE("없는 라인 정보입니다."), NOT_FOUND_STATION("없는 역 정보입니다."), NOT_FOUND_SECTION("없는 구간 정보입니다."),
    IS_NOT_DOWNSTAION("해당역은 하행선이 아닙니다."), IS_NOT_LAST_STATION("마지막 구간이 아닙니다."), THERE_IS_NO_SECTIONS("구간이 더이상 존재하지 않습니다.");
    private String message;

    ErrorMessage(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }
}
