package nextstep.subway.domain.step_feature;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Station;

public class SectionStepFeature {

    private static Line defaultLine = new Line("2호선", "color");

    public static Section createSection(Station upStation, Station downStation) {
        return Section.of(defaultLine, upStation, downStation, 10);
    }

}
