package subway.line.business.constant;

public class LineConstants {

    public static String LINE_NOT_EXIST = "존재하지 않는 노선입니다.";
    public static String INVALID_UP_STATION = "새로운 구간의 상행역이 해당 노선의 하행 종점역이 아닙니다.";
    public static String ALREADY_EXIST_DOWN_STATION = "새로운 구간의 하행역은 해당 노선에 등록되어있는 역일 수 없습니다.";
    public static String CANNOT_REMOVE_SECTION_WHEN_ONLY_ONE = "구간이 한 개인 노선의 하행 종점역은 제거할 수 없습니다.";
    public static String CANNOT_REMOVE_SECTION_WHEN_NOT_LAST_STATION = "하행 종점역이 아닌 역은 제거할 수 없습니다.";

}