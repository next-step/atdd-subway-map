package nextstep.subway.domain;

import java.util.ArrayList;
import java.util.List;

public class Stations {
    private final List<Station> stations;

    public Stations() {
        this.stations = new ArrayList<>();
    }

    public Stations(List<Station> stations) {
        this.stations = stations;
    }
}
