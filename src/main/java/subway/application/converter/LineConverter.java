package subway.application.converter;

import java.util.List;
import org.springframework.stereotype.Component;
import subway.domain.Line;
import subway.domain.Station;
import subway.dto.LineRequest;

@Component
public class LineConverter {

    public Line lineBy(final LineRequest lineRequest, final List<Station> stations) {
        Line line = new Line(
                lineRequest.getName(),
                lineRequest.getColor(),
                stations,
                lineRequest.getDistance()
        );

        stations.forEach(station -> station.setLine(line));

        return line;
    }
}
