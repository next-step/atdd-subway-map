package subway;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class LineResponse {
    private Long id;
    private String name;
    private String color;
    private int distance;
    private List<StationResponse> stations;

    public static LineResponse from(Line line, List<StationResponse> stations) {
        return LineResponse.builder()
                .id(line.getId())
                .name(line.getName())
                .color(line.getColor())
                .distance(line.getDistance())
                .stations(stations)
                .build();
    }

}
