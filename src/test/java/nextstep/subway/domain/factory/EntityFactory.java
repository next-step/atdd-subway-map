package nextstep.subway.domain.factory;

import nextstep.subway.domain.line.Line;
import nextstep.subway.domain.section.Section;
import nextstep.subway.domain.station.Station;

public class EntityFactory {

    public static Line createLine(String name, String color, Station upStation, Station downStation) {
        return Line.of(name, color, upStation, downStation);
    }

    public static Station createStation(String name) {
        return new Station(name);
    }

    public static Section createSection(Station upStation, Station downStation, int distance) {
        return Section.of(upStation, downStation, 10);
    }

    public static Line createCompleteLine(String name, String color, Station upStation, Station downStation, int distance) {
        Line line = createLine(name, color, upStation, downStation);
        line.addSection(createSection(upStation, downStation, distance));

        return line;
    }
}
