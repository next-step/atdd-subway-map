package subway.line;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subway.exception.NoStationException;
import subway.station.Station;
import subway.station.StationRepository;

@Service
@RequiredArgsConstructor
public class LineService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    @Transactional
    public LineResponse createLine(LineRequest request) {

        Line line = lineBuilder(request);
        lineRepository.save(line);

        return LineResponse.of(line);
    }

    @Transactional(readOnly = true)
    public List<LineResponse> getLines() {
        return lineRepository.findAll()
            .stream()
            .map(LineResponse::of)
            .collect(Collectors.toList());
    }

    private Station getStationById(long id) {
        return stationRepository.findById(id)
            .orElseThrow(NoStationException::new);
    }

    public LineResponse getLine(Long id) {
        Line line = lineRepository.findById(id)
            .orElseThrow(NoStationException::new);

        return LineResponse.of(line);
    }

    public LineResponse updateLine(Long id, LineRequest request) {
        Line line = lineRepository.findById(id)
            .orElseThrow(NoStationException::new);

        line.modify(request.getName(), request.getColor());
        return LineResponse.of(line);
    }

    public void deleteLine(Long id) {
        lineRepository.deleteById(id);
    }

    private Line lineBuilder(LineRequest request) {
        Station downStation = getStationById(request.getDownStationId());
        Station upStation = getStationById(request.getUpStationId());

        return Line.GenerateLine()
            .name(request.getName())
            .color(request.getColor())
            .distance(request.getDistance())
            .downStation(downStation)
            .upStation(upStation)
            .build();
    }

}
