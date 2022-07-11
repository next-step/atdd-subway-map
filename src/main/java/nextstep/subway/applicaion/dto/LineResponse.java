package nextstep.subway.applicaion.dto;

import lombok.Builder;
import lombok.Getter;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Station;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class LineResponse {

    private Long id;
    private String name;
    private String color;
    private List<StationResponse> stations;

    public static LineResponse from(Line line) {
        List<StationResponse> stationResponses = new ArrayList<>();
        for (Station station : line.stations()) {
            stationResponses.add(StationResponse.from(station));
        }
        return LineResponse.builder()
                .id(line.id())
                .name(line.content().name())
                .color(line.content().color())
                .stations(stationResponses)
                .build();
    }
}
