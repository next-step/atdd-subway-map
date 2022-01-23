package nextstep.subway.domain;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Stations {
    private final List<Station> stations;

    public Stations() {
        this.stations = new ArrayList<>();
    }

    public Stations(List<Station> stations) {
        this.stations = stations;
    }

    public static Stations of(Station... stations) {
        return new Stations(Arrays.asList(stations));
    }
    public static Stations of(List<Station> stations) {
        return new Stations(stations);
    }

    public List<Station> getStations() {
        return stations;
    }
}
