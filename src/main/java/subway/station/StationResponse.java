package subway.station;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import subway.line.station.LineStation;

@Getter
@Setter(AccessLevel.PROTECTED)
@NoArgsConstructor
@AllArgsConstructor
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

    public static StationResponse of(LineStation lineStation) {
        return of(lineStation.getStation());
    }

    public static StationResponse of(Station station) {
        return new StationResponse(
            station.getId(),
            station.getName()
        );
    }
}
