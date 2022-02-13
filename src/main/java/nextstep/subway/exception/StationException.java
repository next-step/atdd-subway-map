package nextstep.subway.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum StationException implements BadRequestExceptionMessage {
    GIVEN_STATION_ID_IS_NOT_REGISTERED("등록되지 않은 역ID입니다."),
    STATION_NAME_IS_ALREADY_REGISTERED("이미 등록된 역 이름입니다.");

    @Getter
    private final String message;
}
