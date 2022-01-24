package nextstep.subway.exception.station;

import nextstep.subway.exception.ErrorCode;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

public enum StationErrorCode implements ErrorCode {
    DUPLICATE_NAME(StationDuplicateNameException.class, HttpStatus.CONFLICT, "duplicate station name occurred"),
    BLANK_NAME(StationBlankNameException.class, HttpStatus.BAD_REQUEST, "blank station name occurred");

    private final Class exceptionClass;
    private final HttpStatus httpStatus;
    private final String message;

    public static StationErrorCode values(final StationBusinessException exception) {
        final Class<? extends StationBusinessException> exceptionClass = exception.getClass();
        return Arrays.stream(values())
                .filter(userErrorCode -> userErrorCode.exceptionClass.equals(exceptionClass))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    StationErrorCode(final Class exceptionClass, final HttpStatus httpStatus, final String message) {
        this.exceptionClass = exceptionClass;
        this.httpStatus = httpStatus;
        this.message = message;
    }

    @Override
    public HttpStatus httpStatus() {
        return httpStatus;
    }

    @Override
    public String message() {
        return message;
    }
}
