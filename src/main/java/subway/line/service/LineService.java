package subway.line.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.exception.LineNotFoundException;
import subway.line.repository.Line;
import subway.line.repository.LineRepository;
import subway.line.web.CreateLineRequest;
import subway.line.web.LineResponse;
import subway.line.web.UpdateLineRequest;
import subway.station.repository.Station;
import subway.station.repository.StationRepository;
import subway.station.service.StationService;
import subway.station.web.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class LineService {
    private final LineRepository lineRepository;
    private final StationService stationRepository;

    public LineService(LineRepository lineRepository, StationService stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public LineResponse createLine(CreateLineRequest request) {

        Station upStation = stationRepository.findStation(request.getUpStationId());
        Station downStation = stationRepository.findStation(request.getDownStationId());

        Line line = new Line(
                request.getName(),
                request.getColor()
        );

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

    public LineResponse getLine(Long id) {
        Line line = findLine(id);
        return new LineResponse(line);
    }
    @Transactional
    public void updateLine(Long id, UpdateLineRequest request) {
        Line line = findLine(id);
        line.updateName(request.getName(), request.getColor());
    }

    @Transactional
    public void deleteLine(Long id) {
        lineRepository.deleteById(id);
    }

    private Line findLine(Long id) {
        return lineRepository.findById(id)
                .orElseThrow(() -> new LineNotFoundException(id));
    }
}
