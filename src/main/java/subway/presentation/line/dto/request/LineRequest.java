package subway.presentation.line.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.domain.line.Line;
import subway.domain.section.Sections;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LineRequest {

    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private Integer distance;

    public Line toEntity(Sections sections) {
        return Line.builder()
                .name(name)
                .color(color)
                .sections(sections)
                .distance(distance)
                .build();

    }
}