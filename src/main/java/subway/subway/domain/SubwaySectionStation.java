package subway.subway.domain;

public class SubwaySectionStation {

    private final Station.Id id;
    private final String name;

    public SubwaySectionStation(Station station) {
        this.id = station.getId();
        this.name = station.getName();
    }

    public SubwaySectionStation(Station.Id id, String name) {
        this.id = id;
        this.name = name;
    }

    public Station.Id getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
