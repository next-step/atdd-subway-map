package nextstep.subway.handler.error.custom;

public enum ErrorCode {
    DISTANCE_CAN_NOT_SMALL_THAN_ONE(400, "[ERROR] 거리는 1보다 작을 수 없습니다."),
    NOT_VALID_UP_STATION(400, "[ERROR] 입력한 상행선은 기존 구간의 하행선이 아닙니다."),
    NOT_VALID_DOWN_STATION(400, "[ERROR] 입력한 하행선은 기존 노선에 이미 존재합니다."),
    STATION_IS_NOT_LATEST(400, "[ERROR] 입력한 지하철 역은 최하행선이 아니라 구간 삭제가 불가능합니다."),


    LINE_NOT_FOUND_BY_ID(404, "[ERROR] 해당하는 id의 노선이 존재하지 않습니다."),
    STATION_NOT_FOUND_BY_ID(404, "[ERROR] 해당하는 id의 지하철 역이 존재하지 않습니다."),

    FOUND_DUPLICATED_NAME(409, "[ERROR] 중복된 이름이 존재합니다. "),
    ;

    private final int status;
    private final String detail;

    ErrorCode(int status, String detail) {
        this.status = status;
        this.detail = detail;
    }

    public int getStatus() {
        return status;
    }

    public String getDetail() {
        return detail;
    }
}
