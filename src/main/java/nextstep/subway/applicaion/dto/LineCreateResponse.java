package nextstep.subway.applicaion.dto;

import java.time.LocalDateTime;
import nextstep.subway.domain.Line;

public class LineCreateResponse extends BaseLineResponse {

    public LineCreateResponse(
            Long id,
            String name,
            String color,
            LocalDateTime createdDate,
            LocalDateTime modifiedDate) {
        super(id, name, color, createdDate, modifiedDate);
    }

    public static LineCreateResponse of(Line line) {
        return new LineCreateResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                line.getCreatedDate(),
                line.getModifiedDate());
    }
}
