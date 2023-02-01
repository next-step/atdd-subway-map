package subway.common.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * 에러의 응답 body를 공통 포맷에 맞게 변경하여 리턴합니다.
 */
@Getter
@Slf4j
public class ErrorResponse {
    private final String message;

    @Builder
    public ErrorResponse(String message) {
        this.message = message;
    }

    public static <T extends Exception> ErrorResponse from(final T exception) {
        return ErrorResponse.builder()
                .message(exception.getMessage())
                .build();
    }
}
