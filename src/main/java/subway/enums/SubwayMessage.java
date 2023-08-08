package subway.enums;

import lombok.Getter;

@Getter
public class SubwayMessage {

    public static final String NEW_SECTION_UP_STATION_ERROR = "새로운 구간은 상행역은 기존 노선의 하행종점역이어야합니다.";
    public static final String NEW_SECTION_NEW_STATION_ERROR ="새로운 구간의 하행역은 해당 노선에 등록되어있는 역일 수 없습니다.";
    public static final String DELETE_STATION_MUST_END = "삭제할 역은 하행종점역만 가능합니다.";
    public static final String SECTION_MORE_THAN_TWO = "구간이 2개 이상일 때 삭제 가능합니다.";
}
