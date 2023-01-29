package subway.line;

import lombok.Builder;
import lombok.Getter;
import subway.StationResponse;

import java.util.List;

/**
 * StationLineResponse
 *
 * @author JungGyun.Choi
 * @version 1.0.0
 * @since 2023. 01. 29.
 */
@Builder
@Getter
public class StationLineResponse {

    private Long id;

    private String name;

    private String color;

    private List<StationResponse> stations;

    public static StationLineResponse from(StationLine stationLine, List<StationResponse> stations) {
        return StationLineResponse.builder()
                .id(stationLine.getId())
                .name(stationLine.getName())
                .color(stationLine.getColor())
                .stations(stations)
                .build();
    }
}
