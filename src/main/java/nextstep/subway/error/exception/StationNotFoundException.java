package nextstep.subway.error.exception;

import nextstep.subway.error.ErrorCode;

public class StationNotFoundException extends RuntimeException {
    private final ErrorCode errorCode = ErrorCode.NOT_STATION_FOUND;
    private String errorMessage = "%s번 역은 존재하지 않습니다.";

    public StationNotFoundException(Long id) {
        this.errorMessage = String.format(errorMessage, id);
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
