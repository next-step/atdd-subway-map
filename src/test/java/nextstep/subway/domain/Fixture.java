package nextstep.subway.domain;

import java.util.Arrays;
import java.util.List;

public class Fixture {
    public static final Station 분당역 = new Station("분당역");
    public static final Station 강남역 = new Station("강남역");
    public static final List<Station> 역정보 = Arrays.asList(강남역, 분당역);
}
