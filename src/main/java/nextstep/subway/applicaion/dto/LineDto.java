package nextstep.subway.applicaion.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nextstep.subway.domain.line.Line;
import nextstep.subway.domain.station.Station;

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

    public static LineDto of(final Line line, final List<Station> stations) {
        return LineDto.builder()
                .id(line.getId())
                .name(line.getName())
                .color(line.getColor())
                .distance(line.getDistance())
                .stations(
                        stations.stream()
                                .map(StationDto::of)
                                .collect(Collectors.toUnmodifiableList()))
                .build();
    }
}
