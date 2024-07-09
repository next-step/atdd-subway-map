package subway.domain.exception;

import lombok.Getter;

@Getter
public enum SubwayDomainExceptionType {
    IDENTITY_SERVER_ERROR(500, "IDENTITY_SERVER_ERROR", "internal server error"),
    NOT_FOUND_STATION(404, "NOT_FOUND_STATION", "not found station"),
    NOT_FOUND_LINE(404, "NOT_FOUND_LINE", "not found line"),
    ;

    private final int status;
    private final String name;
    private final String message;

    SubwayDomainExceptionType(int status, String name, String message) {
        this.status = status;
        this.name = name;
        this.message = message;
    }
}
