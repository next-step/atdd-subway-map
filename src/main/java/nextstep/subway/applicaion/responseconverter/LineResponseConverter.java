package nextstep.subway.applicaion.responseconverter;

import org.springframework.stereotype.Component;

import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.domain.Line;

@Component
public class LineResponseConverter {
    public LineResponse toResponse(final Line line) {
        return new LineResponse(
            line.getId(),
            line.getName(),
            line.getColor(),
            line.getCreatedDate(),
            line.getModifiedDate()
        );
    }
}
