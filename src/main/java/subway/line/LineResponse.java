package subway.line;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import subway.station.StationResponse;

@Getter
@Builder
public class LineResponse {

    private Long id;
    private String name;
    private String color;
    private List<StationResponse> stations;

    public static LineResponse of(Line line) {

        List<StationResponse> stations =
            List.of(StationResponse.of(line.getDownStation()),
                StationResponse.of(line.getUpStation()));

        return LineResponse.builder()
            .id(line.getId())
            .name(line.getName())
            .color(line.getColor())
            .stations(stations)
            .build();
    }

}
