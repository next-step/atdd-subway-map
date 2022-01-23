package nextstep.subway.domain;

import nextstep.subway.enums.Direction;
import nextstep.subway.exception.DuplicatedSectionException;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Stations {
    private final Set<Station> stations;

    public Stations() {
        this.stations = new HashSet<>();
    }

    public Stations(Set<Station> stations) {
        this.stations = stations;
    }

    public static Stations of(Station... stations) {
        return new Stations(new HashSet<>(Arrays.asList(stations)));
    }
    public static Stations of(Set<Station> stations) {
        return new Stations(stations);
    }

    public Set<Station> getStations() {
        return stations;
    }

    public Direction getDirection(Section section) {
        boolean existUpStation = stations.contains(section.getUpStation());
        boolean existDownStation = stations.contains(section.getDownStation());

        if (!existDownStation && !existUpStation) {
            return Direction.NEW;
        }

        if (existUpStation && existDownStation) {
            throw new DuplicatedSectionException();
        }

        return existUpStation ? Direction.DOWN : Direction.UP;
    }
}
