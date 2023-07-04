package subway.subway.domain;

public class SubwaySectionStation {

    private final Station.Id id;
    private final String name;

    public SubwaySectionStation(Station station) {
        this.id = new Station.Id(station.getId());
        this.name = station.getName();
    }

    public Long getId() {
        return id.getId();
    }

    public String getName() {
        return name;
    }
}
