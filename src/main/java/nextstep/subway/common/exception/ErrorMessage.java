package nextstep.subway.common.exception;

public class ErrorMessage {

  public static final String STATION_NOT_FOUND = "station not found";
  public static final String LINE_NOT_FOUND = "line not found";
  public static final String INVALID_STATION = "새로운 구간 상행역은 기존 라인의 하행선과 같아야 합니다.";
  public static final String LINE_CONTAINS_STATION = "새로운 구간 종점역이 기존 노선에 존재합니다.";
  public static final String SECTION_NO_LAST_DELETE = "구간 제거는 노선 하행 종점역만 제거 할 수 있습니다.";
  public static final String SECTION_ONE_NO_DELETE = "구간이 1개이면 역을 삭제할 수 없습니다.";
}
