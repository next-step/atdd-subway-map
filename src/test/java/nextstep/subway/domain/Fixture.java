package nextstep.subway.domain;

import nextstep.subway.domain.line.Line;
import nextstep.subway.domain.line.Section;
import nextstep.subway.domain.station.Station;

import java.util.Arrays;
import java.util.List;

public class Fixture {
    public static final Station 분당역 = new Station("분당역");
    public static final Station 강남역 = new Station("강남역");
    public static final List<Station> 역정보 = Arrays.asList(강남역, 분당역);
    public static final Section 상행종점역 = new Section(null, 강남역.getId(), 분당역.getId(), 10);
    public static final List<Section> 노선역목록 = Arrays.asList(상행종점역);
    public static final Line 신분당선 = new Line(null, "신분당선", "bg-red-600", 노선역목록);

}
