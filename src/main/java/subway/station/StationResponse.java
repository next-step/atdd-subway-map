package subway.station;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode(of = {"id", "name"})
public class StationResponse {
    private Long id;
    private String name;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static StationResponse of(Station station) {
        return new StationResponse(
            station.getId(),
            station.getName()
        );
    }
}
