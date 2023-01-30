package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Line;
import subway.domain.Station;
import subway.exception.LineNotFoundException;
import subway.repository.LineRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LineService {

    private LineRepository lineRepository;
    private StationService stationService;

    public LineService(LineRepository lineRepository, StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    @Transactional
    public Line saveLine(Line line, Long upStationId, Long downStationId) {

        Station upStation = stationService.findStation(upStationId);
        Station downStation = stationService.findStation(downStationId);

        line.setUpStation(upStation);
        line.setDownStation(downStation);

        return lineRepository.save(line);

    }

    public List<Line> findAllLines() {
        return lineRepository.findAll()
                .stream()
                .collect(Collectors.toUnmodifiableList());
    }

    public Line findLine(Long id) {
        return lineRepository.findById(id)
                .orElseThrow(LineNotFoundException::new);
    }

    @Transactional
    public void updateLine(Long id, String color, String name) {
        Line line = lineRepository.findById(id)
                .orElseThrow(LineNotFoundException::new);
        line.changeName(name);
        line.changeColor(color);
    }

    @Transactional
    public void deleteLine(Long id) {
        lineRepository.deleteById(id);
    }
}
