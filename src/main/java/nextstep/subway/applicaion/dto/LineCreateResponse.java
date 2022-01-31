package nextstep.subway.applicaion.dto;

import java.time.LocalDateTime;
import java.util.List;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.Section;

public class LineCreateResponse extends BaseLineResponse {
    public LineCreateResponse(
            Long id,
            String name,
            String color,
            List<Section> sections,
            LocalDateTime createdDate,
            LocalDateTime modifiedDate) {
        super(id, name, color, sections, createdDate, modifiedDate);
    }

    public static LineCreateResponse of(Line line) {
        return new LineCreateResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                line.getSections(),
                line.getCreatedDate(),
                line.getModifiedDate());
    }
}
