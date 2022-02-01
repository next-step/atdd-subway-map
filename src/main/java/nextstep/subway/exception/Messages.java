package nextstep.subway.exception;

public enum Messages {
    NOT_FOUND("Not Found"),
    DUPLICATE_CREATION("Duplication Creation"),
    ILLEGAL_DELETION("Deletion Forbidden"),
    DUPLICATE_SECTION_STATION("Section's Stations Duplicated")
    ;

    private final String message;

    Messages(String message) {
        this.message = message;
    }

    public String message() {
        return message;
    }
}
