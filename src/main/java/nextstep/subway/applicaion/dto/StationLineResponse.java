package nextstep.subway.applicaion.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.domain.StationLine;

import java.util.List;

@Getter
@NoArgsConstructor
public class StationLineResponse {
    private Long id;
    private String name;
    private String color;
    private List<StationResponse> stations;

    @Builder
    public StationLineResponse(Long id, String name, String color, List<StationResponse> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
    }

    public static StationLineResponse of(StationLine stationLine, List<StationResponse> stations) {
        return StationLineResponse.builder()
                .id(stationLine.getId())
                .name(stationLine.getName())
                .color(stationLine.getColor())
                .stations(stations)
                .build();
    }

}
