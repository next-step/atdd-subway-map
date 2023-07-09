package subway.controller.dto.line;

import lombok.Builder;
import lombok.Getter;
import subway.controller.dto.section.SectionResponse;
import subway.controller.dto.station.StationResponse;
import subway.model.line.Line;
import subway.model.section.Section;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class LineResponse {

    private Long id;
    private String name;
    private String color;
    private List<StationResponse> stations;

    public static LineResponse from(Line line) {

        return LineResponse.builder()
                           .id(line.getId())
                           .name(line.getName())
                           .color(line.getColor())
                           .stations(line.getStations()
                                         .stream()
                                         .map(StationResponse::from)
                                         .collect(Collectors.toList()))
                           .build();
    }
}
