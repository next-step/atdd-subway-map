package fixture;

import subway.station.Station;

public class StationFixture {

    private StationFixture() {
    }

    public static Station giveOne(String name) {
        return Station.builder()
            .name(name)
            .build();
    }

}
