package nextstep.subway.applicaion.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nextstep.subway.domain.entity.Line;

@Getter
@Builder
@RequiredArgsConstructor
public class LineUpdateDto {

    private final String name;
    private final String color;

    public Line toDomain(Line line) {
        return line.toBuilder()
                .name(this.name)
                .color(this.color)
                .build();
    }
}
