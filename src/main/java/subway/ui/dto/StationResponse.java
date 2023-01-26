package subway.ui.dto;

import com.fasterxml.jackson.annotation.*;
import subway.domain.Station;

@JsonTypeName(value = "stations")
public class StationResponse {
    private Long id;
    private String name;

    public StationResponse(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public static StationResponse of(final Station station) {
        return new StationResponse(station.getId(), station.getName());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
