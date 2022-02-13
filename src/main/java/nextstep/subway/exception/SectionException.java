package nextstep.subway.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum SectionException implements BadRequestExceptionMessage {
    GIVEN_DOWN_STATION_IS_ALREADY_REGISTERED_IN_LINE("입력한 하행역이 노선에 이미 등록되어 있습니다."),
    UP_STATION_OF_NEW_SECTION_MUST_BE_DOWN_STATION_OF_LINE("새로운 구간의 상행역은 노선의 하행 종점역이어야만 합니다."),
    ONLY_LAST_STATION_OF_LINE_CAN_BE_DELETED("지하철 노선의 마지막 역만 삭제신청할 수 있습니다."),
    SECTION_CANNOT_BE_DELETED_WHEN_LINE_HAS_ONLY_ONE_SECTION("노선에 구간이 하나만 존재하는 경우, 구간은 삭제할 수 없습니다.");

    @Getter
    private final String message;
}
