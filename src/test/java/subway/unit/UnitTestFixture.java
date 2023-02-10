package subway.unit;

import subway.model.Line;
import subway.model.Station;
import subway.model.Stations;

import java.util.List;

public class UnitTestFixture {
    public static final Station 강남역 = new Station(1L, "강남역");
    public static final Station 역삼역 = new Station(2L, "역삼역", 강남역.getId());
    public static final Station 선릉역 = new Station(3L, "선릉역", 역삼역.getId());
    public static final Station 삼성역 = new Station(4L, "삼성역", 선릉역.getId());
    public static final Line 신분당선 = new Line("신분당선", "bg-red-600", 강남역, 역삼역);
    public static final Line 분당선 = new Line("분당선", "bg-red-500", 강남역, 역삼역);

    public static final Stations 강남역_역삼역 = new Stations(List.of(강남역, 역삼역));
    public static final Stations 강남역_역삼역_선릉역 = new Stations(List.of(강남역, 역삼역, 선릉역));

    public static Stations 지하철역_리스트_생성(Station upStation, Station downStation) {
        return new Stations(List.of(upStation, downStation));
    }
}
