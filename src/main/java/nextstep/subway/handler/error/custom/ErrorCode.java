package nextstep.subway.handler.error.custom;

public enum ErrorCode {
    DISTANCE_CAN_NOT_SMALL_THAN_ONE(400, "[ERROR] 거리는 1보다 작을 수 없습니다."),
    REMAINED_SECTION_ONLY_ONE(400, "[ERROR] 구간이 하나 존재하여 삭제가 불가능합니다. "),
    STATION_IS_NOT_DOWN(400, "[ERROR] 입력된 역은 노선의 최하행역이 아닙니다."),
    INVALID_NEW_DISTANCE(400, "[ERROR] 입력된 구간의 거리는 기존 거리보다 작아야합니다."),


    LINE_NOT_FOUND_BY_ID(404, "[ERROR] 해당하는 id의 노선이 존재하지 않습니다."),
    STATION_NOT_FOUND_BY_ID(404, "[ERROR] 해당하는 id의 지하철 역이 존재하지 않습니다."),
    STATIONS_ALL_NOT_FOUND(404, "[ERROR] 입력된 구간의 모든 역이 노선에 존재하지 않습니다."),

    FOUND_DUPLICATED_NAME(409, "[ERROR] 중복된 이름이 존재합니다. "),
    SECTION_ALREADY_EXISTS(409, "[ERROR] 이미 존재하는 구간입니다. "),
    STATIONS_ALL_EXIST(409, "[ERROR] 입력된 구간의 모든 역이 이미 노선에 존재합니다."),
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
