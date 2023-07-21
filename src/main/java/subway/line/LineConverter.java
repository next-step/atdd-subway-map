package subway.line;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import subway.station.Station;
import subway.station.StationResponse;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class LineConverter {
    public LineResponse convert(Line line) {
        List<StationResponse> stationResponses = line.getSections().getStations()
                .stream()
                .map(station -> new StationResponse(station.getId(), station.getName()))
                .collect(Collectors.toList());

        return new LineResponse(line.getId(), line.getName(), line.getColor(), stationResponses);
    }

    public List<LineResponse> convertToList(List<Line> lines) {
        return lines.stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }
}
