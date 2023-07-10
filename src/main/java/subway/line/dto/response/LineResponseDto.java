package subway.line.dto.response;

import lombok.Builder;
import lombok.Getter;
import subway.line.entity.Line;
import subway.station.dto.response.StationResponseDto;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
public class LineResponseDto {

    private Long id;

    private String name;

    private String color;

    private List<StationResponseDto> stations;

    public static LineResponseDto of(Line line) {
        return LineResponseDto.builder()
                .id(line.getId())
                .name(line.getName())
                .color(line.getColor())
                .stations(line
                        .getSections()
                        .getAllStations()
                        .stream()
                        .map(StationResponseDto::of)
                        .collect(Collectors.toList()))
                .build();
    }

}
