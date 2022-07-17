package nextstep.subway.applicaion.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.domain.Station;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StationResponse {
    private Long id;
    private String name;

    public StationResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static StationResponse of(Station station) {
        return new StationResponse(
                station.getId(),
                station.getName()
        );
    }
}
