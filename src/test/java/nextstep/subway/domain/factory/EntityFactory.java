package nextstep.subway.domain.factory;

import nextstep.subway.domain.line.Line;
import nextstep.subway.domain.section.Section;
import nextstep.subway.domain.station.Station;

public class EntityFactory {

    public static Station createStation(String name) {
        return new Station(name);
    }

    public static Section createSection(Station upStation, Station downStation, int distance) {
        return Section.of(upStation, downStation, distance);
    }

    public static Line createLine(String name, String color, Station upStation, Station downStation, int distance) {
        Line line = Line.of(name, color, upStation, downStation);
        line.addSection(createSection(upStation, downStation, distance));

        return line;
    }
}
