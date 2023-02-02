package subway.application.converter;

import org.springframework.stereotype.Component;
import subway.domain.Line;
import subway.dto.LineCreateRequest;

@Component
public class LineConverter {

    public Line lineBy(final LineCreateRequest lineCreateRequest) {
        return new Line(
                lineCreateRequest.getName(),
                lineCreateRequest.getColor(),
                lineCreateRequest.getUpStationId(),
                lineCreateRequest.getDownStationId(),
                lineCreateRequest.getDistance()
        );
    }
}
