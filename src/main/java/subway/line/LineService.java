package subway.line;

import org.springframework.stereotype.Service;
import subway.station.StationService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LineService {
    private LineRepository lineRepository;
    private StationService stationService;

    public LineService(LineRepository lineRepository, StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    @Transactional
    public LineResponse createLine(LineDto lineDto) {
        var upStation = stationService.findById(lineDto.getUpStationId());
        var downStation = stationService.findById(lineDto.getDownStationId());

        var line = lineRepository.save(new Line(
                lineDto.getName(),
                lineDto.getColor(),
                lineDto.getDistance(),
                upStation,
                downStation
        ));

        return LineResponse.from(line);
    }

    public List<LineResponse> findAllLines() {
        var lines = lineRepository.findAll();
        return lines.stream()
                .map(LineResponse::from)
                .collect(Collectors.toList());
    }

    public LineResponse findLineById(long lineId) {
        Optional<Line> OptionalLine = lineRepository.findById(lineId);
        if (OptionalLine.isEmpty()) {
            throw new LineNotFoundException(lineId);
        }
        return LineResponse.from(OptionalLine.get());
    }
}
