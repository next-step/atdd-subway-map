package subway.common;

public enum DomainExceptionType {
    UNKNOWN_RULE("정의되지 않은 Domain 규칙입니다"),
    NO_LINE("존재하지 않는 노선입니다."),
    NO_STATION("존재하지 않는 역입니다.");

    private final String defaultMessage;

    DomainExceptionType(String defaultMessage) {
        this.defaultMessage = defaultMessage;
    }

    public String getCode() {
        return this.toString();
    }

    public String getMessage() {
        return defaultMessage;
    }
}
