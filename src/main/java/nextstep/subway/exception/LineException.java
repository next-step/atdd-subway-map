package nextstep.subway.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum LineException implements BadRequestExceptionMessage {
    LINE_NAME_IS_ALREADY_REGISTERED("이미 등록된 노선명입니다."),
    GIVEN_LINE_ID_IS_NOT_REGISTERED("등록되지 않은 노선 ID입니다.");

    @Getter
    private final String message;
}
