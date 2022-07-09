package nextstep.subway.applicaion.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nextstep.subway.domain.line.Line;


@Getter
@Builder
@RequiredArgsConstructor
public class LineDto {

    private final Long id;
    private final String name;
    private final String color;

    public static LineDto of(final Line line) {
        return LineDto.builder()
                .id(line.getId())
                .name(line.getName())
                .color(line.getColor())
                .build();
    }
}
