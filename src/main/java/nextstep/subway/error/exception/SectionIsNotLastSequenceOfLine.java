package nextstep.subway.error.exception;

import nextstep.subway.error.ErrorCode;

public class SectionIsNotLastSequenceOfLine extends RuntimeException {
    private final ErrorCode errorCode = ErrorCode.MISMATCH_UPPER_SECTION_WITH_LOWER_LINE;
    private String errorMessage = "등록 구간의 상행역이 해당 노선의 하행 종점역과 다릅니다.";

    public SectionIsNotLastSequenceOfLine() {
        this.errorMessage = errorMessage;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
