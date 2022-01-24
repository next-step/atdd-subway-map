package nextstep.subway.exception.line;

import nextstep.subway.exception.ErrorCode;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

public enum LineErrorCode implements ErrorCode {
    DUPLICATE_NAME(LineDuplicateNameException.class, HttpStatus.CONFLICT, "duplicate line name occurred");

    private final Class exceptionClass;
    private final HttpStatus httpStatus;
    private final String message;

    public static LineErrorCode values(final LineBusinessException exception) {
        final Class<? extends LineBusinessException> exceptionClass = exception.getClass();
        return Arrays.stream(values())
                .filter(userErrorCode -> userErrorCode.exceptionClass.equals(exceptionClass))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    LineErrorCode(final Class exceptionClass, final HttpStatus httpStatus, final String message) {
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
