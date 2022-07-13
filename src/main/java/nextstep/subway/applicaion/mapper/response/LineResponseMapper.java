package nextstep.subway.applicaion.mapper.response;

import lombok.RequiredArgsConstructor;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class LineResponseMapper implements ResponseMapper<Line, LineResponse> {

    private final StationRepository stationRepository;
    private final StationResponseMapper stationResponseMapper;

    @Override
    public LineResponse map(Line line) {
        List<Station> stations = stationRepository.findAllById(List.of(line.getUpStationId(), line.getDownStationId()));

        return LineResponse.builder()
                .id(line.getId())
                .name(line.getName())
                .color(line.getColor())
                .stations(stations.stream().map(stationResponseMapper::map).collect(Collectors.toList()))
                .build();
    }
}
