package nextstep.subway.error.exception;

import nextstep.subway.error.ErrorCode;

public class SectionAlreadyHasStationException extends RuntimeException {
    private final ErrorCode errorCode = ErrorCode.DUPLICATE_STATION_ERR;
    private String errorMessage = "구간의 하행역(%s번 역)은 이미 노선에 등록되어 있습니다.";

    public SectionAlreadyHasStationException(Long id) {
        this.errorMessage = String.format(errorMessage, id);
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
