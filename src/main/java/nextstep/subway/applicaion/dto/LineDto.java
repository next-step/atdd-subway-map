package nextstep.subway.applicaion.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nextstep.subway.domain.Line;

import java.util.List;
import java.util.stream.Collectors;


@Getter
@Builder
@RequiredArgsConstructor
public class LineDto {

    private final Long id;
    private final String name;
    private final String color;

    private final List<StationDto> stations;

    public static LineDto of(final Line line) {
        return LineDto.builder()
                .id(line.getId())
                .name(line.getName())
                .color(line.getColor())
                .stations(
                        line.getStations().stream()
                                .map(StationDto::of)
                                .collect(Collectors.toUnmodifiableList()))
                .build();
    }
}
