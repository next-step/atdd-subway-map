package subway.subway.domain;

import java.util.Arrays;
import java.util.List;

public class SubwaySectionList {

    private final List<SubwaySection> stations;

    public SubwaySectionList(SubwaySection... stations) {
        this.stations = Arrays.asList(stations);
    }

    public void add(SubwaySection station) {
        stations.add(station);
    }

    public List<SubwaySection> getSections() {
        return stations;
    }
}
