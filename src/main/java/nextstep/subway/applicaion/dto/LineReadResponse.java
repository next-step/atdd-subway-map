package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.Section;

import java.time.LocalDateTime;
import java.util.List;

public class LineReadResponse extends BaseLineResponse {

    public LineReadResponse(
            Long id,
            String name,
            String color,
            List<Section> sections,
            LocalDateTime createdDate,
            LocalDateTime modifiedDate
            ) {
        super(id, name, color, sections, createdDate, modifiedDate);
    }

    public static LineReadResponse of(Line line) {
        return new LineReadResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                line.getSections(),
                line.getCreatedDate(),
                line.getModifiedDate()
        );
    }
}
