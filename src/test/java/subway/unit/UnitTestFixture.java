package subway.unit;

import subway.model.Line;
import subway.model.Station;
import subway.model.Stations;

import java.util.List;

public class UnitTestFixture {
    public static final Station 강남역 = new Station("강남역");
    public static final Station 역삼역 = new Station("역삼역");
    public static final Line 신분당선 = new Line("신분당선", "bg-red-600", 강남역, 역삼역);
    public static final Line 분당선 = new Line("분당선", "bg-red-500", 강남역, 역삼역);

    public static final Stations 강남역_역삼역 = new Stations(List.of(강남역, 역삼역));
}
