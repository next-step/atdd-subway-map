package subway;

import subway.model.Line;
import subway.model.Station;

public class UnitTestConstant {
    public static final Station GANG_NAM_STATION = new Station("강남역");
    public static final Station YEOK_SAM_STATION = new Station("역삼역");
    public static final Line SHIN_BUN_DANG_LINE = new Line("신분당선", "bg-red-600", GANG_NAM_STATION, YEOK_SAM_STATION);
    public static final Line BUN_DANG_LINE = new Line("분당선", "bg-red-500", GANG_NAM_STATION, YEOK_SAM_STATION);

}
