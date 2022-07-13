package nextstep.subway.applicaion.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nextstep.subway.domain.entity.Line;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Getter
@Builder
@RequiredArgsConstructor
public class LineDto {

    private final Long id;
    private final String name;
    private final String color;
    private final Integer distance;

    private final List<StationDto> stations;

    public static LineDto of(final Line line) {
        return LineDto.builder()
                .id(line.getId())
                .name(line.getName())
                .color(line.getColor())
                .distance(line.getDistance())
                .stations(
                        line.getStations().stream()
                                .map(StationDto::of)
                                .sorted(Comparator.comparing(StationDto::getId))
                                .collect(Collectors.toUnmodifiableList()))
                .build();
    }
}
