package subway.station.presentation.response;

import subway.station.domain.Station;

public class CreateStationResponse {

    private Long id;
    private String name;

    public CreateStationResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static CreateStationResponse from(Station station) {
        return new CreateStationResponse(
                station.getId(),
                station.getName()
        );
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
