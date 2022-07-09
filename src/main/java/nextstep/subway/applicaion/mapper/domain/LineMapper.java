package nextstep.subway.applicaion.mapper.domain;

import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.domain.Line;
import org.springframework.stereotype.Component;

@Component
public class LineMapper implements DomainMapper<LineRequest, Line> {

    @Override
    public Line map(LineRequest lineRequest) {
        return new Line(
                lineRequest.getName(),
                lineRequest.getColor(),
                lineRequest.getUpStationId(),
                lineRequest.getDownStationId(),
                lineRequest.getDistance()
        );
    }
}
