package subway.exception;

public enum ErrorMessage {
    LINE_NOT_FOUND("지하철 호선을 찾을 수 없습니다."),
    STATION_NOT_FOUND("지하철역을 찾을 수 없습니다"),
    UPSTATION_OF_NEW_SECTION_SHOULD_BE_DOWNSTATION_OF_THE_LINE("새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이어야 합니다."),
    DOWNSTATION_OF_NEW_SECTION_SHOULD_NOT_BE_REGISTERED_THE_LINE("새로운 구간의 하행역은 해당 노선에 등록되어있는 역일 수 없습니다."),
    SHOULD_DELETE_ONLY_DOWNSTATION_OF_LINE("마지막 구간만 제거할 수 있습니다."),
    CANNOT_DELETE_LINE_CONSIST_OF_ONE_SECTION("노선에 구간이 1개인 경우 역을 삭제할 수 없다.");

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
