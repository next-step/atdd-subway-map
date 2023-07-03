package subway.line.service;

import org.springframework.stereotype.Service;
import subway.line.repository.Line;
import subway.line.repository.LineRepository;
import subway.line.web.CreateLineRequest;
import subway.line.web.LineResponse;
import subway.station.repository.Station;
import subway.station.repository.StationRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LineService {
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public LineResponse createLine(CreateLineRequest request) {
        Line line = new Line(
                request.getName(),
                request.getColor()
        );

        Station upStation = stationRepository.findById(request.getUpStationId()).orElseThrow(RuntimeException::new);
        Station downStation = stationRepository.findById(request.getDownStationId()).orElseThrow(RuntimeException::new);

        line.addStation(upStation);
        line.addStation(downStation);

        Line newLine = lineRepository.save(line);

        return new LineResponse(newLine);
    }

    public List<LineResponse> getLines() {
        return lineRepository.findAll().stream()
                .map(LineResponse::new)
                .collect(Collectors.toList());
    }
}
