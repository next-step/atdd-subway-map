package fixture;

import subway.line.Line;
import subway.station.Station;

public class LineFixture {

    private LineFixture() {
    }

    public static Line giveOne(String name, String color, Station upStation, Station downStation, Long distance) {
        return Line.builder()
            .name(name)
            .color(color)
            .upStation(upStation)
            .downStation(downStation)
            .distance(distance)
            .build();
    }

}
