package nextstep.subway.applicaion.mapper.domain;

import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.domain.Line;
import org.springframework.stereotype.Component;

@Component
public class LineMapper implements DomainMapper<LineRequest, Line> {

    @Override
    public Line map(LineRequest lineRequest) {
        return Line.builder()
                .name(lineRequest.getName())
                .color(lineRequest.getColor())
                .build();
    }
}
