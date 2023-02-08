package subway.common.constants;

public class ErrorConstant {

    private ErrorConstant() {
    }

    public static final String NOT_ADD_LAST_STATION = "새로운 구간의 상행 역은 노선에 등록된 하행 종점 역이어야 합니다.";
    public static final String ALREADY_ENROLL_STATION = "새로운 구간의 하행 역은 노선에 등록된 역일 수 없습니다.";
    public static final String NOT_DELETE_LAST_STATION = "노선에 등록된 하행 종점역만 제거할 수 있습니다.";
    public static final String ONLY_ONE_SECTION = "노선에 상행 종점역과 하행 종점역만 있는 경우 구간을 삭제할 수 없습니다.";
}
