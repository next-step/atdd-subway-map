package subway.application.converter;

import java.util.List;
import org.springframework.stereotype.Component;
import subway.domain.Line;
import subway.domain.Station;
import subway.dto.LineCreateRequest;

@Component
public class LineConverter {

    public Line lineBy(final LineCreateRequest lineCreateRequest, final List<Station> stations) {
        Line line = new Line(
                lineCreateRequest.getName(),
                lineCreateRequest.getColor(),
                stations,
                lineCreateRequest.getDistance()
        );
        stations.forEach(station -> station.setLine(line));
        return line;
    }
}
