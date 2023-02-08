package subway.dto;

import subway.domain.Station;

public class StationRequest implements EntityTransformable<Station> {
    private String name;

    public String getName() {
        return name;
    }

    @Override
    public Station toEntity() {
        return new Station(this.getName());
    }
}
