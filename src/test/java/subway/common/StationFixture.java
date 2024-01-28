package subway.common;

public class StationFixture {
    public final Long id;
    public final String name;

    private StationFixture(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static StationFixture 지하철역() {
        final String name = "지하철역";
        Long id = CommonApi.Station.createStationBy(name).jsonPath().getLong("id");
        return new StationFixture(id, name);
    }

    public static StationFixture 새로운지하철역() {
        final String name = "새로운지하철역";
        Long id = CommonApi.Station.createStationBy(name).jsonPath().getLong("id");
        return new StationFixture(id, name);
    }

    public static StationFixture 또다른지하철역() {
        final String name = "또다른지하철역";
        Long id = CommonApi.Station.createStationBy(name).jsonPath().getLong("id");
        return new StationFixture(id, name);
    }

}
