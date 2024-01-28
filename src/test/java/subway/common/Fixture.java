package subway.common;

import java.util.ArrayList;
import java.util.List;

public class Fixture {
    public static class Line {
        public final String name;
        public final String color;
        public final List<Station> stations = new ArrayList<>();
        public final String distance;

        private Line(String name, String color, Station upStation, Station downStation, Long distance) {
            this.name = name;
            this.color = color;
            this.stations.add(upStation);
            this.stations.add(downStation);
            this.distance = distance.toString();
        }
    }
    public static class Station {
        public final Long id;
        public final String name;

        private Station(Long id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    public static Line 신분당선(Station up, Station down) {
        final String name = "신분당선";
        final String color = "bg-red-600";
        final Long distance = 10L;

        return new Line(name, color, up, down, distance);
    }

    public static Line 분당선(Station up, Station down) {
        final String name = "분당선";
        final String color = "bg-greed-600";
        final Long distance = 10L;

        return new Line(name, color, up, down, distance);
    }

    public static Station 지하철역() {
        final String name = "지하철역";
        Long id = CommonApi.Station.createStationBy(name).jsonPath().getLong("id");
        return new Station(id, name);
    }

    public static Station 새로운지하철역() {
        final String name = "새로운지하철역";
        Long id = CommonApi.Station.createStationBy(name).jsonPath().getLong("id");
        return new Station(id, name);
    }

    public static Station 또다른지하철역() {
        final String name = "또다른지하철역";
        Long id = CommonApi.Station.createStationBy(name).jsonPath().getLong("id");
        return new Station(id, name);
    }
}
