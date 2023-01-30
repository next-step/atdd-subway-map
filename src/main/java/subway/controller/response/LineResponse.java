package subway.controller.response;

import lombok.Builder;
import lombok.Getter;
import subway.repository.entity.Line;

import java.util.List;

@Builder
@Getter
public class LineResponse {

    private Long id;

    private String name;

    private String color;

    private List<StationResponse> stations;

    public static LineResponse from(final Line stationLine, final List<StationResponse> collect) {
        return LineResponse.builder()
                .id(stationLine.getId())
                .name(stationLine.getName())
                .color(stationLine.getColor())
                .stations(collect)
                .build();
    }
}
